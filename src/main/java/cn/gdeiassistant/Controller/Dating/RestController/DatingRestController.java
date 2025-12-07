package cn.gdeiassistant.Controller.Dating.RestController;

import cn.gdeiassistant.Annotation.RecordIPAddress;
import cn.gdeiassistant.Constant.ValueConstantUtils;
import cn.gdeiassistant.Enum.IPAddress.IPAddressEnum;
import cn.gdeiassistant.Exception.DatabaseException.DataNotExistException;
import cn.gdeiassistant.Exception.DatabaseException.NoAccessException;
import cn.gdeiassistant.Exception.DatingException.RepeatPickException;
import cn.gdeiassistant.Exception.DatingException.SelfPickException;
import cn.gdeiassistant.Pojo.Entity.DatingMessage;
import cn.gdeiassistant.Pojo.Entity.DatingPick;
import cn.gdeiassistant.Pojo.Entity.DatingProfile;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.Socialising.Dating.DatingService;
import cn.gdeiassistant.Tools.Utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dating") // 统一API前缀
public class DatingRestController {

    @Autowired
    private DatingService datingService;

    /**
     * 更新撩一下记录的状态
     *
     * @param id
     * @param state
     * @return
     */
    @PostMapping("/pick/id/{id}")
    public ResponseEntity<JsonResult> updateDatingPickStateApi(HttpServletRequest request,
                                                               @PathVariable("id") Integer id,
                                                               @RequestParam Integer state) throws DataNotExistException, NoAccessException {
        JsonResult jsonResult = new JsonResult();
        if (!state.equals(-1) && !state.equals(1)) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("请求参数不合法");
        } else {
            datingService.VerifyDatingPickViewAccess(request.getSession().getId(), id);
            datingService.UpdateDatingPickState(id, state);
            jsonResult.setSuccess(true);
        }
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * 提交卖室友信息
     *
     * @param request
     * @param datingProfile
     * @param image
     * @return
     */
    @PostMapping("/profile")
    @RecordIPAddress(type = IPAddressEnum.POST)
    public ResponseEntity<JsonResult> addDatingProfileApi(HttpServletRequest request,
                                                          @Validated DatingProfile datingProfile,
                                                          @RequestParam("image") MultipartFile image) throws IOException {
        if (image == null || image.getSize() <= 0 || image.getSize() >= ValueConstantUtils.MAX_IMAGE_SIZE) {
            return ResponseEntity.ok(new JsonResult(false, "图片文件不能为空"));
        } else {
            Integer id = datingService.AddDatingProfile(request.getSession().getId(), datingProfile);
            datingService.UploadPicture(id, image.getInputStream());
            return ResponseEntity.ok(new JsonResult(true));
        }
    }

    /**
     * 提交撩一下请求
     *
     * @param request
     * @param datingPick
     * @param profileId
     * @return
     */
    @PostMapping("/pick")
    @RecordIPAddress(type = IPAddressEnum.POST)
    public ResponseEntity<JsonResult> addDatingPickApi(HttpServletRequest request,
                                                       @RequestBody DatingPick datingPick, // 使用 @RequestBody 接收JSON
                                                       @RequestParam Integer profileId) throws
            SelfPickException, RepeatPickException {
        DatingProfile datingProfile = new DatingProfile();
        datingProfile.setProfileId(profileId);
        datingPick.setDatingProfile(datingProfile);
        if (datingPick.getDatingProfile().getProfileId() == null) {
            return ResponseEntity.ok(new JsonResult(false, "请求参数不合法"));
        }
        if (datingPick.getContent().length() > 50) {
            return ResponseEntity.ok(new JsonResult(false, "文本内容超过限制"));
        }
        // datingService.VerifyDatingPickRequestAccess(request.getSession().getId(), datingPick.getPickId()); // pickId可能为空，需要调整业务逻辑
        datingService.AddDatingPick(request.getSession().getId(), datingPick);
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 分页查询用户卖室友消息
     *
     * @param request
     * @param start
     * @return
     */
    @GetMapping("/message/start/{start}")
    public ResponseEntity<DataJsonResult<List<DatingMessage>>> queryDatingMessageApi(HttpServletRequest request,
                                                                                     @PathVariable("start") Integer start) {
        List<DatingMessage> datingMessageList = datingService.QueryUserDatingMessage(request.getSession().getId(), start, 10);
        return ResponseEntity.ok(new DataJsonResult<>(true, datingMessageList));
    }

    /**
     * 获取卖室友信息列表
     *
     * @param start
     * @param area
     * @return
     */
    @GetMapping("/profile/area/{area}/start/{start}")
    public ResponseEntity<DataJsonResult<List<DatingProfile>>> queryDatingProfileApi(@PathVariable("start") Integer start,
                                                                                     @PathVariable("area") Integer area) {
        List<DatingProfile> datingProfileList = datingService.QueryDatingProfile(start, 10, area);
        return ResponseEntity.ok(new DataJsonResult<>(true, datingProfileList));
    }

    /**
     * 已读卖室友消息
     *
     * @param id
     * @return
     */
    @PostMapping("/message/id/{id}/read")
    public ResponseEntity<JsonResult> readDatingMessageApi(@PathVariable("id") Integer id) {
        datingService.UpdateDatingMessageState(id, 1);
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 获取卖室友信息的照片
     *
     * @param id
     * @return
     */
    @GetMapping("/profile/id/{id}/picture")
    public ResponseEntity<DataJsonResult<String>> getDatingProfilePictureApi(@PathVariable("id") Integer id) {
        String url = datingService.GetDatingProfilePictureURL(id);
        if (StringUtils.isNotBlank(url)) {
            return ResponseEntity.ok(new DataJsonResult<>(true, url));
        }
        return ResponseEntity.ok(new DataJsonResult<>(new JsonResult(false)));
    }

    /**
     * 查看撩一下记录详细信息API
     * @param request
     * @param id
     * @return
     * @throws DataNotExistException
     * @throws NoAccessException
     */
    @GetMapping("/pick/id/{id}")
    public ResponseEntity<DatingPick> getDatingPickDetailApi(HttpServletRequest request, @PathVariable("id") Integer id) throws DataNotExistException, NoAccessException {
        datingService.VerifyDatingPickViewAccess(request.getSession().getId(), id);
        DatingPick datingPick = datingService.QueryDatingPickById(id);
        return ResponseEntity.ok(datingPick);
    }

    /**
     * 查看卖室友详细信息API
     * @param request
     * @param id
     * @return
     * @throws DataNotExistException
     */
    @GetMapping("/profile/id/{id}")
    public ResponseEntity<Map<String, Object>> getDatingProfileDetailApi(HttpServletRequest request, @PathVariable("id") Integer id) throws DataNotExistException {
        Map<String, Object> responseData = new HashMap<>();
        DatingProfile datingProfile = datingService.QueryDatingProfile(id);
        String pictureUrl = datingService.GetDatingProfilePictureURL(id);

        responseData.put("datingProfile", datingProfile);
        responseData.put("pictureURL", pictureUrl);

        // 检查联系方式可见性逻辑
        DatingPick datingPick = datingService.QueryDatingPick(id, request.getSession().getId());
        if (datingPick != null) {
            boolean pickPageHidden = datingService.CheckIsPickPageHidden(request.getSession().getId(), datingPick.getPickId());
            if (pickPageHidden) {
                responseData.put("isContactVisible", true);
                responseData.put("isPickNotAvailable", true); // 假设这个字段表示是否可以再次撩
            }
        }
        return ResponseEntity.ok(responseData);
    }

    /**
     * 查询用户未读卖室友消息数量API
     * @param request
     * @return
     */
    @GetMapping("/message/unreadCount")
    public ResponseEntity<DataJsonResult<Integer>> queryUserUnReadDatingMessageCountApi(HttpServletRequest request) {
        Integer unreadCount = datingService.QueryUserUnReadDatingMessageCount(request.getSession().getId());
        return ResponseEntity.ok(new DataJsonResult<>(true, unreadCount != null ? unreadCount : 0));
    }
}
