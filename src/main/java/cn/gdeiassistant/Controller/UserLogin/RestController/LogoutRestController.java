package cn.gdeiassistant.Controller.UserLogin.RestController;

import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.UserLogin.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/userlogin") // 统一API前缀
public class LogoutRestController {

    @Autowired
    private UserLoginService userLoginService;

    /**
     * 退出登录API
     *
     * @param request
     * @return
     */
    @PostMapping("/logout") // 使用POST请求进行退出登录操作
    public ResponseEntity<JsonResult> logoutApi(HttpServletRequest request, HttpServletResponse response) {
        //清除Session
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        //清除Cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath(request.getContextPath() + "/");
            response.addCookie(cookie);
        }
        return ResponseEntity.ok(new JsonResult(true, "退出登录成功"));
    }
}
