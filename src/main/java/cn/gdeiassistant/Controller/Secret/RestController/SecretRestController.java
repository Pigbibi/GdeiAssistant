package cn.gdeiassistant.Controller.Secret.RestController;

import cn.gdeiassistant.Annotation.RecordIPAddress;
import cn.gdeiassistant.Constant.ValueConstantUtils;
import cn.gdeiassistant.Enum.IPAddress.IPAddressEnum;
import cn.gdeiassistant.Exception.DatabaseException.DataNotExistException;
import cn.gdeiassistant.Pojo.Entity.Secret;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.Socialising.Secret.SecretService;
import cn.gdeiassistant.Service.ThirdParty.Wechat.WechatService;
import cn.gdeiassistant.Tools.Utils.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/secret") // 统一API前缀
public class SecretRestController {

    @Autowired
    private SecretService secretService;

    @Autowired
    private WechatService wechatService;

    /**
     * 获取更多的树洞消息 (原有API，修改返回类型为ResponseEntity)
     *
     * @param start
     * @param size
     * @return
     */
    @RequestMapping(value = "/info/start/{start}/size/{size}", method = RequestMethod.GET) // 保留原有路径
    public ResponseEntity<DataJsonResult<List<Secret>>> getMoreSecretApi(HttpServletRequest request
            , @PathVariable("start") int start, @PathVariable("size") int size) throws Exception {
        List<Secret> secretList = secretService.GetSecretInfo(start, size, request.getSession().getId());
        return ResponseEntity.ok(new DataJsonResult<>(true, secretList));
    }

    /**
     * 发布树洞消息 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param secret
     * @param voiceId
     * @param file
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST) // 保留原有路径
    @RecordIPAddress(type = IPAddressEnum.POST)
    public ResponseEntity<JsonResult> addSecretInfoApi(HttpServletRequest request, @Validated Secret secret, String voiceId
            , @RequestParam(value = "voice", required = false) MultipartFile file) throws Exception {
        if (secret.getType() == 0 && StringUtils.isBlank(secret.getContent())) {
            return ResponseEntity.ok(new JsonResult(false, "树洞信息不能为空"));
        }
        if (secret.getType().equals(0)) {
            //文字树洞信息
            secretService.AddSecretInfo(request.getSession().getId(), secret);
            return ResponseEntity.ok(new JsonResult(true));
        } else if (secret.getType().equals(1)) {
            //语音树洞信息
            if (file == null || file.isEmpty() || file.getSize() == 0) {
                return ResponseEntity.ok(new JsonResult(false, "语音内容不能为空"));
            } else if (file.getSize() > ValueConstantUtils.MAX_VOICE_SIZE) {
                return ResponseEntity.ok(new JsonResult(false, "语音文件大小过大"));
            } else {
                //插入树洞信息记录
                Integer id = secretService.AddSecretInfo(request.getSession().getId(), secret);
                //上传录音文件
                secretService.UploadVoiceSecret(id, file.getInputStream());
                return ResponseEntity.ok(new JsonResult(true));
            }
        } else if (secret.getType().equals(2)) {
            //微信树洞信息
            InputStream inputStream = wechatService.DownloadWechatVoiceRecord(voiceId);
            if (inputStream != null) {
                //插入树洞信息记录
                Integer id = secretService.AddSecretInfo(request.getSession().getId(), secret);
                //上传录音文件
                secretService.UploadVoiceSecret(id, inputStream);
                return ResponseEntity.ok(new JsonResult(true));
            }
            return ResponseEntity.ok(new JsonResult(false, "音频的服务器端ID无效"));
        } else {
            return ResponseEntity.ok(new JsonResult(false, "树洞信息类型不合法"));
        }
    }

    /**
     * 添加树洞评论 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param id
     * @param comment
     * @return
     */
    @RequestMapping(value = "/id/{id}/comment", method = RequestMethod.POST) // 保留原有路径
    @RecordIPAddress(type = IPAddressEnum.POST)
    public ResponseEntity<JsonResult> addSecretCommentApi(HttpServletRequest request, @PathVariable("id") int id
            , @Validated @NotBlank @Length(min = 1, max = 50) String comment) throws Exception {
        if (!secretService.CheckSecretInfoExist(id)) {
            throw new DataNotExistException("查询的校园树洞信息不存在");
        }
        secretService.AddSecretComment(id, request.getSession().getId(), comment);
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 更改点赞状态 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param id
     * @param like
     * @return
     */
    @RequestMapping(value = "/id/{id}/like", method = RequestMethod.POST) // 保留原有路径
    public ResponseEntity<JsonResult> updateSecretLikeStateApi(HttpServletRequest request
            , @PathVariable("id") int id, @Validated @Range(min = 0, max = 1) int like) throws Exception {
        if (secretService.CheckSecretInfoExist(id)) {
            switch (like) {
                case 0:
                    //取消点赞
                    secretService.ChangeUserLikeState(false, id, request.getSession().getId());
                    return ResponseEntity.ok(new JsonResult(true));

                case 1:
                    //点赞
                    secretService.ChangeUserLikeState(true, id, request.getSession().getId());
                    return ResponseEntity.ok(new JsonResult(true));

                default:
                    return ResponseEntity.ok(new JsonResult(false, "请求参数不合法"));
            }
        }
        throw new DataNotExistException("查询的校园树洞信息不存在");
    }

    // --- 从 SecretController 迁移过来的 API ---

    /**
     * 获取树洞详细信息API
     *
     * @param request
     * @param id
     * @return
     * @throws DataNotExistException
     */
    @GetMapping("/detail/id/{id}") // 新增API路径
    public ResponseEntity<DataJsonResult<Secret>> getSecretDetailApi(HttpServletRequest request, @PathVariable("id") int id) throws Exception {
        Secret secret = secretService.GetSecretDetailInfo(id, request.getSession().getId());
        return ResponseEntity.ok(new DataJsonResult<>(true, secret));
    }

    /**
     * 获取用户发布的树洞列表API
     *
     * @param request
     * @return
     */
    @GetMapping("/profile/items") // 新增API路径
    public ResponseEntity<DataJsonResult<List<Secret>>> getSecretProfileItemsApi(HttpServletRequest request) throws Exception {
        List<Secret> secretList = secretService.GetSecretInfo(request.getSession().getId());
        return ResponseEntity.ok(new DataJsonResult<>(true, secretList));
    }
}
