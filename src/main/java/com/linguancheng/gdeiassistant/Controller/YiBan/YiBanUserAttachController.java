package com.gdeiassistant.gdeiassistant.Controller.YiBan;

import com.gdeiassistant.gdeiassistant.Enum.Base.LoginResultEnum;
import com.gdeiassistant.gdeiassistant.Exception.CommonException.SyncTransactionException;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.User;
import com.gdeiassistant.gdeiassistant.Pojo.Redirect.RedirectInfo;
import com.gdeiassistant.gdeiassistant.Pojo.Result.BaseJsonResult;
import com.gdeiassistant.gdeiassistant.Pojo.Result.BaseResult;
import com.gdeiassistant.gdeiassistant.Service.UserData.UserDataService;
import com.gdeiassistant.gdeiassistant.Service.UserLogin.UserLoginService;
import com.gdeiassistant.gdeiassistant.Service.YiBan.YiBanUserDataService;
import com.gdeiassistant.gdeiassistant.ValidGroup.User.UserLoginValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class YiBanUserAttachController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private YiBanUserDataService yiBanUserDataService;

    @RequestMapping("/yiban/attach")
    public ModelAndView ResolveYiBanUserAttachPage(HttpServletRequest request, RedirectInfo redirectInfo) {
        ModelAndView modelAndView = new ModelAndView();
        String userid = (String) request.getSession().getAttribute("yiBanUserID");
        if (userid == null || userid.trim().isEmpty()) {
            modelAndView.addObject("ErrorMessage", "用户授权已过期，请重新登录并授权");
            modelAndView.setViewName("YiBan/yibanError");
            return modelAndView;
        }
        if (redirectInfo.needToRedirect()) {
            modelAndView.addObject("RedirectURL", redirectInfo.getRedirect_url());
        }
        modelAndView.setViewName("YiBan/yibanAttach");
        return modelAndView;
    }

    @RequestMapping("/yiban/userattach")
    @ResponseBody
    public BaseJsonResult YiBanUserAttach(HttpServletRequest request, HttpServletResponse response
            , @Validated(value = UserLoginValidGroup.class) User user
            , boolean relink, BindingResult bindingResult) throws ServletException, IOException {
        BaseJsonResult result = new BaseJsonResult();
        if (bindingResult.hasErrors()) {
            result.setSuccess(false);
            result.setErrorMessage("请求参数异常");
            return result;
        }
        String yiBanUserID = (String) request.getSession().getAttribute("yiBanUserID");
        if (yiBanUserID == null || yiBanUserID.trim().isEmpty()) {
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
                    request.getRequestDispatcher("/yiban/userattach?relink=true").forward(request, response);
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
                    //同步易班数据
                    if (yiBanUserDataService.SyncYiBanUserData(resultUser.getUsername(), yiBanUserID)) {
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