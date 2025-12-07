package cn.gdeiassistant.Controller.UserProfile.RestController;

import cn.gdeiassistant.Annotation.RestAuthentication;
import cn.gdeiassistant.Pojo.Entity.Introduction;
import cn.gdeiassistant.Pojo.Entity.Profile;
import cn.gdeiassistant.Pojo.Entity.Region;
import cn.gdeiassistant.Pojo.Profile.LocationComparator;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.Profile.UserProfileService;
import cn.gdeiassistant.Tools.Utils.LocationUtils;
import cn.gdeiassistant.Tools.Utils.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api") // 统一API前缀
public class ProfileRestController {

    @Autowired
    private UserProfileService userProfileService;

    private final int AVATAR_MAX_SIZE = 1024 * 1024 * 2;

    /**
     * 获取头像URL信息 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/avatar", method = RequestMethod.GET)
    public ResponseEntity<DataJsonResult<String>> getUserAvatarApi(HttpServletRequest request) {
        String url = userProfileService.GetSelfUserAvatar(request.getSession().getId());
        if (StringUtils.isBlank(url)) {
            return ResponseEntity.ok(new DataJsonResult<>(true, ""));
        } else {
            return ResponseEntity.ok(new DataJsonResult<>(true, url));
        }
    }

    /**
     * 获取头像URL和高清头像URL信息 (新增API)
     *
     * @param request
     * @return
     */
    @GetMapping("/profile/avatar/urls")
    public ResponseEntity<DataJsonResult<Map<String, String>>> getUserAvatarUrlsApi(HttpServletRequest request) {
        Map<String, String> urls = new HashMap<>();
        String avatarUrl = userProfileService.GetSelfUserAvatar(request.getSession().getId());
        String avatarHDUrl = userProfileService.GetSelfUserHighDefinitionAvatar(request.getSession().getId());
        urls.put("avatarURL", avatarUrl);
        urls.put("avatarHDURL", avatarHDUrl);
        return ResponseEntity.ok(new DataJsonResult<>(true, urls));
    }

    /**
     * 删除用户头像图片 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/avatar/remove", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> deleteUserAvatarApi(HttpServletRequest request) {
        userProfileService.DeleteAvatar(request.getSession().getId());
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 更新用户头像图片 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param avatar
     * @param avatarHD
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> updateUserAvatarApi(HttpServletRequest request
            , @RequestParam("avatar") MultipartFile avatar, @RequestParam("avatar_hd") MultipartFile avatarHD) throws IOException {
        if (avatar == null || avatar.getSize() <= 0 || avatar.getSize() >= AVATAR_MAX_SIZE
                || avatarHD == null || avatarHD.getSize() <= 0 || avatarHD.getSize() >= AVATAR_MAX_SIZE) {
            return ResponseEntity.ok(new JsonResult(false, "上传的图片文件不合法"));
        } else {
            userProfileService.UpdateAvatar(request.getSession().getId(), avatar.getInputStream());
            userProfileService.UpdateHighDefinitionAvatar(request.getSession().getId(), avatarHD.getInputStream());
            return ResponseEntity.ok(new JsonResult(true));
        }
    }

    /**
     * 获取用户个人资料 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity<DataJsonResult> getUserProfileApi(HttpServletRequest request) throws Exception {
        Profile profile = userProfileService.GetSelfUserProfile(request.getSession().getId());
        if (profile != null) {
            // 简化LocationUtils的调用，直接返回原始代码，前端处理显示
            return ResponseEntity.ok(new DataJsonResult<>(true, profile));
        }
        return ResponseEntity.ok(new DataJsonResult<>(false, "获取个人资料失败"));
    }

    /**
     * 更新用户个人简介 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param introduction
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/introduction", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> updateIntroductionApi(HttpServletRequest request, @RequestParam String introduction) throws Exception {
        if (introduction != null && introduction.length() <= 80) {
            if (introduction.isEmpty()) {
                introduction = null;
            }
            userProfileService.UpdateIntroduction(request.getSession().getId(), introduction);
            return ResponseEntity.ok(new JsonResult(true));
        }
        return ResponseEntity.ok(new JsonResult(false, "个人简介长度不合法"));
    }

    /**
     * 获取用户个人简介 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/introduction", method = RequestMethod.GET)
    public ResponseEntity<DataJsonResult<String>> getUserIntroductionApi(HttpServletRequest request) throws Exception {
        Introduction introduction = userProfileService.GetSelfUserIntroduction(request.getSession().getId());
        return ResponseEntity.ok(new DataJsonResult<>(true, StringUtils.isBlank(introduction.getIntroductionContent()) ? "" : introduction.getIntroductionContent()));
    }

    /**
     * 加载地区列表 (原有API，修改返回类型为ResponseEntity)
     *
     * @return
     */
    @RequestMapping(value = "/locationList", method = RequestMethod.GET)
    public ResponseEntity<DataJsonResult<List<Region>>> getRegionListApi() {
        List<Region> locationList = new ArrayList<>(LocationUtils.getRegionMap().values());
        locationList.sort(new LocationComparator());
        return ResponseEntity.ok(new DataJsonResult<>(true, locationList));
    }

    /**
     * 更新生日日期 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param year
     * @param month
     * @param date
     * @return
     */
    @RequestMapping(value = "/profile/birthday", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> updateBirthdayApi(HttpServletRequest request, @Validated @Min(1900) @Max(2050) Integer year
            , @Validated @Min(1) @Max(12) Integer month, @Validated @Min(1) @Max(31) Integer date) throws Exception {
        if (year == null && month == null && date == null) {
            userProfileService.ResetBirthday(request.getSession().getId());
            return ResponseEntity.ok(new JsonResult(true));
        }
        if (year != null && month != null && date != null) {
            userProfileService.UpdateBirthday(request.getSession().getId(), year, month, date);
            return ResponseEntity.ok(new JsonResult(true));
        }
        return ResponseEntity.ok(new JsonResult(false, "请求参数不合法"));
    }

    /**
     * 更新用户院系 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param faculty
     * @return
     */
    @RequestMapping(value = "/profile/faculty", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> updateFacultyApi(HttpServletRequest request, @RequestParam int faculty) throws Exception {
        if (faculty >= 0 && faculty < UserProfileService.getFacultyMap().size()) {
            userProfileService.UpdateFaculty(request.getSession().getId(), faculty);
            return ResponseEntity.ok(new JsonResult(true));
        }
        return ResponseEntity.ok(new JsonResult(false, "请求参数异常"));
    }

    /**
     * 更新用户所在地 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param region
     * @param state
     * @param city
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/profile/location", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> updateLocationApi(HttpServletRequest request
            , @Validated @NotBlank @RequestParam("region") String region
            , @Nullable String state, @Nullable String city) throws Exception { // 允许state和city为空
        // 简化逻辑，直接调用Service，Service层处理校验
        userProfileService.UpdateLocation(request.getSession().getId(), region, state, city);
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 更新用户家乡 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param region
     * @param state
     * @param city
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/profile/hometown", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> updateHometownApi(HttpServletRequest request
            , @Validated @NotBlank @RequestParam("region") String region
            , @Nullable String state, @Nullable String city) throws Exception { // 允许state和city为空
        // 简化逻辑，直接调用Service，Service层处理校验
        userProfileService.UpdateHometown(request.getSession().getId(), region, state, city);
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 更新用户专业 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param major
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/profile/major", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> updateMajorApi(HttpServletRequest request, @Validated @NotBlank @Length(min = 1, max = 20) String major) throws Exception {
        userProfileService.UpdateMajor(request.getSession().getId(), major);
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 更新入学年份 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param enrollment
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/profile/enrollment", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> updateEnrollmentApi(HttpServletRequest request, @Validated @Min(1955) @RequestParam(value = "year", required = false) Integer enrollment) throws Exception {
        if (enrollment != null && enrollment > LocalDate.now().getYear()) {
            return ResponseEntity.ok(new JsonResult(false, "请求参数不合法"));
        }
        if (enrollment != null) {
            userProfileService.UpdateEnrollment(request.getSession().getId(), enrollment);
        } else {
            userProfileService.ResetEnrollment(request.getSession().getId());
        }
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 更新用户昵称 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param nickname
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/profile/nickname", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> updateNicknameApi(HttpServletRequest request, @Validated @NotBlank @Length(min = 1, max = 32) String nickname) throws Exception {
        userProfileService.UpdateNickname(request.getSession().getId(), nickname);
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 获取昵称信息接口 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/rest/profile/nickname", method = RequestMethod.GET)
    public ResponseEntity<DataJsonResult<String>> getUserNicknameRestApi(HttpServletRequest request
            , @RequestParam("token") String token) throws Exception {
        String sessionId = (String) request.getAttribute("sessionId");
        Profile profile = userProfileService.GetSelfUserProfile(sessionId);
        String nickname = profile.getNickname();
        return ResponseEntity.ok(new DataJsonResult<>(true, nickname));
    }

    /**
     * 获取头像信息接口 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param token
     * @return
     */
    @RequestMapping(value = "/rest/profile/avatar", method = RequestMethod.GET)
    public ResponseEntity<DataJsonResult<String>> getUserAvatarRestApi(HttpServletRequest request
            , @RequestParam("token") String token) {
        String sessionId = (String) request.getAttribute("sessionId");
        String url = userProfileService.GetSelfUserAvatar(sessionId);
        if (StringUtils.isBlank(url)) {
            return ResponseEntity.ok(new DataJsonResult<>(true, ""));
        } else {
            return ResponseEntity.ok(new DataJsonResult<>(true, url));
        }
    }

    /**
     * 获取用户个人资料Rest接口 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/rest/profile", method = RequestMethod.POST)
    @RestAuthentication
    public ResponseEntity<DataJsonResult<Profile>> getUserProfileRestApi(HttpServletRequest request
            , @RequestParam("token") String token) throws Exception {
        String sessionId = (String) request.getAttribute("sessionId");
        Profile profile = userProfileService.GetSelfUserProfile(sessionId);
        // 简化LocationUtils的调用，直接返回原始代码，前端处理显示
        return ResponseEntity.ok(new DataJsonResult<>(true, profile));
    }

    // --- 从 ProfileController 迁移过来的 API ---

    /**
     * 获取公开用户个人资料API
     *
     * @param request
     * @param username
     * @return
     * @throws Exception
     */
    @GetMapping("/profile/user/{username}")
    public ResponseEntity<DataJsonResult<Profile>> getPublicUserProfileApi(HttpServletRequest request, @PathVariable("username") String username) throws Exception {
        Profile profile = userProfileService.GetUserProfile(username);
        // 简化LocationUtils的调用，直接返回原始代码，前端处理显示
        return ResponseEntity.ok(new DataJsonResult<>(true, profile));
    }

    /**
     * 获取当前用户个人资料（访客视角）API
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/profile/personal")
    public ResponseEntity<DataJsonResult<Profile>> getPersonalProfileApi(HttpServletRequest request) throws Exception {
        Profile profile = userProfileService.GetSelfUserProfile(request.getSession().getId());
        // 简化LocationUtils的调用，直接返回原始代码，前端处理显示
        return ResponseEntity.ok(new DataJsonResult<>(true, profile));
    }
}
