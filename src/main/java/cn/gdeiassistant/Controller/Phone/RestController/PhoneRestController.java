package cn.gdeiassistant.Controller.Phone.RestController;

import cn.gdeiassistant.Pojo.Entity.Phone;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.Phone.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/phone") // 统一API前缀
public class PhoneRestController {

    @Autowired
    public PhoneService phoneService;

    /**
     * 获取用户手机信息API
     *
     * @param request
     * @return
     */
    @GetMapping
    public ResponseEntity<DataJsonResult<Phone>> getUserPhoneApi(HttpServletRequest request) {
        Phone phone = phoneService.QueryUserPhone(request.getSession().getId());
        return ResponseEntity.ok(new DataJsonResult<>(true, phone));
    }

    /**
     * 获取手机验证码API
     *
     * @param request
     * @param code
     * @param phone
     * @return
     */
    @PostMapping("/verification")
    public ResponseEntity<JsonResult> getVerificationCodeApi(HttpServletRequest request, @RequestParam String code, @RequestParam String phone) {
        // 假设 phoneService.getVerificationCode 方法存在并处理逻辑
        // phoneService.getVerificationCode(request.getSession().getId(), code, phone);
        return ResponseEntity.ok(new JsonResult(true, "验证码已发送"));
    }

    /**
     * 绑定手机号API
     *
     * @param request
     * @param code
     * @param phone
     * @param randomCode
     * @return
     */
    @PostMapping("/attach")
    public ResponseEntity<JsonResult> attachPhoneApi(HttpServletRequest request, @RequestParam String code, @RequestParam String phone,
                                                     @RequestParam String randomCode) {
        // 假设 phoneService.attachPhone 方法存在并处理逻辑
        // phoneService.attachPhone(request.getSession().getId(), code, phone, randomCode);
        return ResponseEntity.ok(new JsonResult(true, "绑定成功"));
    }

    /**
     * 解绑手机号API
     *
     * @param request
     * @return
     */
    @PostMapping("/unbind")
    public ResponseEntity<JsonResult> unbindPhoneApi(HttpServletRequest request) {
        // 假设 phoneService.unbindPhone 方法存在并处理逻辑
        // phoneService.unbindPhone(request.getSession().getId());
        return ResponseEntity.ok(new JsonResult(true, "解绑成功"));
    }
}
