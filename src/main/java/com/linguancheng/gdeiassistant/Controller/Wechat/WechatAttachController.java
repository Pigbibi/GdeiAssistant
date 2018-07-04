package com.gdeiassistant.gdeiassistant.Controller.Wechat;

import com.gdeiassistant.gdeiassistant.Enum.Base.LoginResultEnum;
import com.gdeiassistant.gdeiassistant.Exception.CommonException.SyncTransactionException;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.User;
import com.gdeiassistant.gdeiassistant.Pojo.Result.BaseJsonResult;
import com.gdeiassistant.gdeiassistant.Pojo.Result.BaseResult;
import com.gdeiassistant.gdeiassistant.Service.UserData.UserDataService;
import com.gdeiassistant.gdeiassistant.Service.UserLogin.UserLoginService;
import com.gdeiassistant.gdeiassistant.Service.Wechat.WechatService;
import com.gdeiassistant.gdeiassistant.Service.Wechat.WechatUserDataService;
import com.gdeiassistant.gdeiassistant.Tools.StringUtils;
import com.gdeiassistant.gdeiassistant.ValidGroup.User.UserLoginValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Controller
public class WechatAttachController {

    private String appid;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private WechatUserDataService wechatUserDataService;

    @Value("#{propertiesReader['wechat.account.appid']}")
    public void setAppid(String appid) {
        this.appid = appid;
    }

    @RequestMapping(value = "/wechat/attach", method = RequestMethod.GET)
    public ModelAndView ResolveWechatUserAttachPage(HttpServletRequest request, String code, String state) throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView();
        if (code == null) {
            //未得到OAuth2.0授权
            if (state == null) {
                //用户未登录授权
                String uuid = StringUtils.randomUUID();
                request.getSession().setAttribute("wechatUUID", uuid);
                modelAndView.setViewName("redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                        + appid + "&redirect_uri=" + URLEncoder.encode("https://www.gdeiassistant.cn/wechat/attach"
                        , "UTF-8") + "&response_type=code&scope=snsapi_userinfo&state=" + uuid + "#wechat_redirect");
                return modelAndView;
            }
            //用户拒绝授权
            modelAndView.setViewName("Error/commonError");
            modelAndView.addObject("ErrorTitle", "微信授权失败");
            modelAndView.addObject("ErrorMessage", "用户拒绝了微信登录授权");
            return modelAndView;
        }
        //得到OAuth2.0授权
        if (state != null && state.equals(request.getSession().getAttribute("wechatUUID"))) {
            //微信登录授权成功
            Map<String, String> authorizationMap = wechatService.GetAccessTokenAndOpenId(code);
            if (authorizationMap != null) {
                String access_token = authorizationMap.get("access_token");
                String openid = authorizationMap.get("openid");
                String unionid = wechatService.GetUserUnionID(access_token, openid);
                if (StringUtils.isNotBlank(unionid)) {
                    //获取UnionID成功
                    request.getSession().setAttribute("wechatUserID", unionid);
                    modelAndView.setViewName("Wechat/wechatAttach");
                    return modelAndView;
                }
                //获取UnionID失败
                modelAndView.setViewName("Error/commonError");
                modelAndView.addObject("ErrorTitle", "微信授权失败");
                modelAndView.addObject("ErrorMessage", "获取用户统一标识失败");
                return modelAndView;
            }
            //服务器异常
            modelAndView.setViewName("Error/commonError");
            modelAndView.addObject("ErrorTitle", "微信授权失败");
            modelAndView.addObject("ErrorMessage", "获取访问权限令牌失败");
            return modelAndView;
        }
        //请求回调状态码不匹配
        modelAndView.setViewName("Error/commonError");
        modelAndView.addObject("ErrorTitle", "微信授权失败");
        modelAndView.addObject("ErrorMessage", "请求回调状态码不匹配");
        return modelAndView;
    }

    @RequestMapping(value = "/wechat/userattach", method = RequestMethod.POST)
    @ResponseBody
    public BaseJsonResult WechatUserAttach(HttpServletRequest request, HttpServletResponse response
            , @Validated(value = UserLoginValidGroup.class) User user
            , boolean relink, BindingResult bindingResult) throws ServletException, IOException {
        BaseJsonResult result = new BaseJsonResult();
        if (bindingResult.hasErrors()) {
            result.setSuccess(false);
            result.setErrorMessage("请求参数异常");
            return result;
        }
        String wechatUserID = (String) request.getSession().getAttribute("wechatUserID");
        if (StringUtils.isBlank(wechatUserID)) {
            result.setSuccess(false);
            result.setErrorMessage("用户授权已过期，请重新登录并授权");
            return result;
        }
        //清除已登录用户的用户凭证记录
        userLoginService.ClearUserLoginCredentials(request);
        BaseResult<User, LoginResultEnum> userLoginResult = userLoginService.UserLogin(request, user, true);
        switch (userLoginResult.getResultType()) {
            case PASSWORD_ERROR:
                result.setSuccess(false);
                result.setErrorMessage("用户账户或密码错误，请检查并重试");
                break;

            case TIME_OUT:
                if (!relink) {
                    //如果第一次连接失败,则重新尝试一次
                    request.getRequestDispatcher("/wechat/userattach?relink=true").forward(request, response);
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("网络连接超时，请重试");
                }
                break;

            case SERVER_ERROR:
                result.setSuccess(false);
                result.setErrorMessage("教务系统维护中，请稍候再试");
                break;

            case LOGIN_SUCCESS:
                //同步用户教务系统账号信息到数据库
                User resultUser = userLoginResult.getResultData();
                //同步用户数据
                try {
                    userDataService.SyncUserData(resultUser);
                    //同步微信数据
                    if (wechatUserDataService.SyncWechatUserData(resultUser.getUsername(), wechatUserID)) {
                        //将用户信息数据写入Session
                        request.getSession().setAttribute("username", resultUser.getUsername());
                        request.getSession().setAttribute("password", resultUser.getPassword());
                        request.getSession().setAttribute("keycode", resultUser.getKeycode());
                        request.getSession().setAttribute("number", resultUser.getNumber());
                        result.setSuccess(true);
                        return result;
                    }
                } catch (SyncTransactionException e) {
                    result.setSuccess(false);
                    result.setErrorMessage("学院系统维护中，请稍候再试");
                }
                break;
        }
        return result;
    }
}