package cn.gdeiassistant.Service.Account;

import cn.gdeiassistant.Pojo.Entity.*;
import cn.gdeiassistant.Tools.StringEncryptUtils;
import cn.gdeiassistant.Exception.CloseAccountException.ItemAvailableException;
import cn.gdeiassistant.Exception.CloseAccountException.UserStateErrorException;
import cn.gdeiassistant.Exception.CommonException.PasswordIncorrectException;
import cn.gdeiassistant.Pojo.Entity.*;
import cn.gdeiassistant.Repository.Mongodb.Grade.GradeDao;
import cn.gdeiassistant.Repository.Mongodb.Schedule.ScheduleDao;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.Authentication.AuthenticationMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.Cet.CetMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.Delivery.DeliveryMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.Ershou.ErshouMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.Gender.GenderMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.Graduation.GraduationMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.LostAndFound.LostAndFoundMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.Phone.PhoneMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.Privacy.PrivacyMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.Profile.ProfileMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.User.UserMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistant.WechatUser.WechatUserMapper;
import cn.gdeiassistant.Repository.SQL.Mysql.Mapper.GdeiAssistantLogs.Close.CloseMapper;
import cn.gdeiassistant.Service.Profile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CloseAccountService {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private GradeDao gradeDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ErshouMapper ershouMapper;

    @Autowired
    private LostAndFoundMapper lostAndFoundMapper;

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Autowired
    private CetMapper cetMapper;

    @Autowired
    private PhoneMapper phoneMapper;

    @Autowired
    private GenderMapper genderMapper;

    @Autowired
    private WechatUserMapper wechatUserMapper;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private AuthenticationMapper authenticationMapper;

    @Autowired
    private GraduationMapper graduationMapper;

    @Autowired
    private PrivacyMapper privacyMapper;

    @Autowired
    private CloseMapper closeMapper;

    /**
     * 关闭待处理的社区功能信息
     */
    @Transactional("appTranscationManager")
    public void CloseSocialDataState(String username) throws Exception {
        List<ErshouItem> ershouItemList = ershouMapper
                .selectItemsByUsername(StringEncryptUtils.encryptString(username));
        for (ErshouItem ershouItem : ershouItemList) {
            if (ershouItem.getState().equals(1)) {
                ershouMapper.updateItemState(ershouItem.getId(), 3);
            }
        }
        List<LostAndFoundItem> lostAndFoundItemList = lostAndFoundMapper
                .selectItemByUsername(StringEncryptUtils.encryptString(username));
        for (LostAndFoundItem lostAndFoundItem : lostAndFoundItemList) {
            if (lostAndFoundItem.getState().equals(0)) {
                lostAndFoundMapper.updateItemState(lostAndFoundItem.getId(), 2);
            }
        }
        List<DeliveryOrder> deliveryOrderList = deliveryMapper
                .selectDeliveryOrderByUsername(StringEncryptUtils.encryptString(username));
        for (DeliveryOrder deliveryOrder : deliveryOrderList) {
            if (deliveryOrder.getState().equals(0)) {
                //下单者有未删除的快递代收订单信息
                deliveryMapper.updateOrderState(deliveryOrder.getOrderId(), 3);
            } else if (deliveryOrder.getState().equals(1)) {
                //下单者有未确认交付的快递代收交易
                DeliveryTrade deliveryTrade = deliveryMapper.selectDeliveryTradeByOrderId(deliveryOrder.getOrderId());
                if (deliveryTrade != null && deliveryTrade.getState().equals(0)) {
                    deliveryMapper.updateTradeState(deliveryTrade.getTradeId(), 2);
                }
            }
        }
    }


    /**
     * 删除用户账号
     *
     * @param username
     * @param password
     * @throws Exception
     */
    @Transactional("appTransactionManager")
    public void CloseAccount(String username, String password) throws Exception {
        //检查用户账号状态
        User user = userMapper.selectUser(StringEncryptUtils.encryptString(username)).decryptUser();
        if (user == null || !user.getState().equals(1)) {
            //若账号状态异常，则抛出异常
            throw new UserStateErrorException("用户账号状态异常");
        }
        if (!user.getPassword().equals(password)) {
            //账号密码错误
            throw new PasswordIncorrectException("用户账号密码不匹配");
        }
        //检查有无待处理的社区功能信息
        List<ErshouItem> ershouItemList = ershouMapper
                .selectItemsByUsername(StringEncryptUtils.encryptString(username));
        for (ErshouItem ershouItem : ershouItemList) {
            if (ershouItem.getState().equals(1)) {
                throw new ItemAvailableException("请下架或确认出售账号下的所有二手交易物品");
            }
        }
        List<LostAndFoundItem> lostAndFoundItemList = lostAndFoundMapper
                .selectItemByUsername(StringEncryptUtils.encryptString(username));
        for (LostAndFoundItem lostAndFoundItem : lostAndFoundItemList) {
            if (lostAndFoundItem.getState().equals(0)) {
                throw new ItemAvailableException("请确认寻回账号下的所有失物招领物品");
            }
        }
        //检查有无待处理的全民快递订单和交易
        List<DeliveryOrder> deliveryOrderList = deliveryMapper
                .selectDeliveryOrderByUsername(StringEncryptUtils.encryptString(username));
        for (DeliveryOrder deliveryOrder : deliveryOrderList) {
            if (deliveryOrder.getState().equals(0)) {
                //下单者有未删除的快递代收订单信息
                throw new ItemAvailableException("请删除账号下的所有快递代收订单信息");
            } else if (deliveryOrder.getState().equals(1)) {
                //下单者有未确认交付的快递代收交易
                DeliveryTrade deliveryTrade = deliveryMapper.selectDeliveryTradeByOrderId(deliveryOrder.getOrderId());
                if (deliveryTrade != null && deliveryTrade.getState().equals(0)) {
                    throw new ItemAvailableException("请确认交付账号下的未交付的快递代收交易");
                }
            }
        }

        //开始进行账号关闭事务

        //删除四六级准考证号
        CetNumber cetNumber = cetMapper
                .selectNumber(StringEncryptUtils.encryptString(username));
        if (cetNumber != null) {
            cetMapper.updateNumber(StringEncryptUtils.encryptString(username), null);
        }
        //删除绑定的手机号
        Phone phone = phoneMapper.selectPhone(StringEncryptUtils.encryptString(username));
        if (phone != null) {
            phoneMapper.deletePhone(StringEncryptUtils.encryptString(username));
        }
        //删除自定义性别
        genderMapper.deleteCustomGender(StringEncryptUtils.encryptString(username));
        //删除教务缓存信息
        gradeDao.removeGrade(username);
        scheduleDao.removeSchedule(username);
        //移除易班和微信绑定状态
        wechatUserMapper.resetWechatUser(StringEncryptUtils.encryptString(username));
        //删除用户资料信息
        profileMapper.resetUserProfile(StringEncryptUtils.encryptString(username), "已注销");
        profileMapper.resetUserIntroduction(StringEncryptUtils.encryptString(username));
        //清除实名认证信息
        Authentication authentication = authenticationMapper.selectAuthentication(StringEncryptUtils.encryptString(username));
        if (authentication != null) {
            authenticationMapper.deleteAuthentication(StringEncryptUtils.encryptString(username));
        }
        //重置毕业用户账号处理方案
        graduationMapper.resetGraduation(StringEncryptUtils.encryptString(username));
        //重置用户隐私配置
        privacyMapper.resetPrivacy(StringEncryptUtils.encryptString(username));
        //删除用户头像
        userProfileService.DeleteAvatar(username);
        //删除用户账号信息
        Integer count = userMapper.selectDeletedUserCount("del_"
                + StringEncryptUtils.SHA1HexString(username).substring(0, 15));
        count = count == null ? 0 : count;
        userMapper.closeUser("del_" + StringEncryptUtils.SHA1HexString(username)
                .substring(0, 15) + "_" + count, StringEncryptUtils.encryptString(username));
        //保存注销日志
        SaveCloseLog(username, count);
    }

    /**
     * 记录账号关闭日志
     *
     * @param username
     * @param count
     */
    private void SaveCloseLog(String username, int count) {
        CloseLog closeLog = new CloseLog();
        closeLog.setUsername(username);
        closeLog.setResetname("del_" + StringEncryptUtils.SHA1HexString(username)
                .substring(0, 15) + "_" + count);
        closeMapper.insertCloseLog(closeLog);
    }
}
