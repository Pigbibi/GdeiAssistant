package com.gdeiassistant.gdeiassistant.Controller.RSAEncryption;

import com.gdeiassistant.gdeiassistant.Pojo.Result.DataJsonResult;
import com.gdeiassistant.gdeiassistant.Tools.RSAUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RSAEncryptionRestController {

    /**
     * 获取RSA公钥密钥Base64编码字符串
     *
     * @return
     */
    @RequestMapping(value = "/rest/encryption/rsa/publickey", method = RequestMethod.GET)
    public DataJsonResult<String> GetRSAPublicKey() {
        return new DataJsonResult<>(true, RSAUtils.GetPublicKey());
    }


}