package cn.gdeiassistant.Controller.Email.RestController;

import cn.gdeiassistant.Pojo.Entity.Email;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.Email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/email") // 统一API前缀
public class EmailRestController {

    @Autowired
    public EmailService emailService;

    /**
     * 获取用户邮箱信息API
     *
     * @param request
     * @return
     */
    @GetMapping
    public ResponseEntity<DataJsonResult<Email>> getUserEmailApi(HttpServletRequest request) {
        Email email = emailService.QueryUserEmail(request.getSession().getId());
        return ResponseEntity.ok(new DataJsonResult<>(true, email));
    }

    /**
     * 获取邮件验证码API
     *
     * @param request
     * @param email
     * @return
     */
    @PostMapping("/verification")
    public ResponseEntity<JsonResult> getVerificationCodeApi(HttpServletRequest request, @RequestParam String email) {
        // 假设 emailService.getVerificationCode 方法存在并处理逻辑
        // emailService.getVerificationCode(request.getSession().getId(), email);
        return ResponseEntity.ok(new JsonResult(true, "验证码已发送"));
    }

    /**
     * 绑定电子邮件地址API
     *
     * @param request
     * @param email
     * @param randomCode
     * @return
     */
    @PostMapping("/bind")
    public ResponseEntity<JsonResult> bindEmailApi(HttpServletRequest request, @RequestParam String email, @RequestParam String randomCode) {
        // 假设 emailService.bindEmail 方法存在并处理逻辑
        // emailService.bindEmail(request.getSession().getId(), email, randomCode);
        return ResponseEntity.ok(new JsonResult(true, "绑定成功"));
    }

    /**
     * 解绑电子邮件地址API
     *
     * @param request
     * @return
     */
    @PostMapping("/unbind")
    public ResponseEntity<JsonResult> unbindEmailApi(HttpServletRequest request) {
        // 假设 emailService.unbindEmail 方法存在并处理逻辑
        // emailService.unbindEmail(request.getSession().getId());
        return ResponseEntity.ok(new JsonResult(true, "解绑成功"));
    }
}
