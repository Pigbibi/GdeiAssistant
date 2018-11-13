package com.gdeiassistant.gdeiassistant.Service.YiBan;

import com.google.gson.Gson;
import com.gdeiassistant.gdeiassistant.Enum.Base.BoolResultEnum;
import com.gdeiassistant.gdeiassistant.Enum.Base.LoginResultEnum;
import com.gdeiassistant.gdeiassistant.Exception.CommonException.ServerErrorException;
import com.gdeiassistant.gdeiassistant.Tools.HttpClientUtils;
import com.gdeiassistant.gdeiassistant.Pojo.UserLogin.UserCertificate;
import com.gdeiassistant.gdeiassistant.Repository.Mysql.GdeiAssistant.User.UserMapper;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.User;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.YiBanUser;
import com.gdeiassistant.gdeiassistant.Pojo.Result.BaseResult;
import com.gdeiassistant.gdeiassistant.Tools.StringEncryptUtils;
import com.gdeiassistant.gdeiassistant.Service.UserLogin.UserLoginService;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class YiBanLoginService {

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserLoginService userLoginService;

    private Log log = LogFactory.getLog(YiBanLoginService.class);

    /**
     * 通过Token获取用户UserID
     *
     * @param accessToken
     * @return
     */
    public BaseResult<YiBanUser, BoolResultEnum> getYiBanUserInfo(String accessToken) {
        BaseResult<YiBanUser, BoolResultEnum> result = new BaseResult<>();
        JSONObject jsonObject = restTemplate.getForObject("https://openapi.yiban.cn/user/me?access_token=" + accessToken, JSONObject.class);
        if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
            YiBanUser yibanUser = new Gson().fromJson(jsonObject.toString(), YiBanUser.class);
            result.setResultData(yibanUser);
            result.setResultType(BoolResultEnum.SUCCESS);
        } else {
            result.setResultType(BoolResultEnum.ERROR);
        }
        return result;
    }

    /**
     * 易班已绑定教务系统账号的用户通过用户名快速登入教务系统
     *
     * @param sessionId
     * @param username
     * @return
     */
    public User YiBanQuickLogin(String sessionId, String username) throws Exception {
        User user = userMapper.selectUser(StringEncryptUtils.encryptString(username));
        if (user != null) {
            user = user.decryptUser();
            UserCertificate userCertificate = userLoginService.UserLogin(sessionId, user, true);
            return userCertificate.getUser();
        }
        return null;
    }
}
