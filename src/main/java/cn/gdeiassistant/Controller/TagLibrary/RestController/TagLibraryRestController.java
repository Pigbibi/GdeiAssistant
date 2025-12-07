package cn.gdeiassistant.Controller.TagLibrary.RestController;

import cn.gdeiassistant.Pojo.Entity.Festival;
import cn.gdeiassistant.Pojo.Entity.WechatAccount;
import cn.gdeiassistant.Service.Profile.UserProfileService;
import cn.gdeiassistant.Tools.Utils.FestivalUtils;
import cn.gdeiassistant.Tools.Utils.SpringProfileUtils;
import cn.gdeiassistant.Tools.Utils.WechatAccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TagLibraryRestController {

    @Autowired
    private UserProfileService userProfileService;

    /**
     * 获取院系列表
     * @return
     */
    @GetMapping("/api/maps/faculties")
    public ResponseEntity<Map<Integer, String>> getFaculties() {
        Map<Integer, String> map = userProfileService.getFacultyMap();
        return ResponseEntity.ok(map);
    }

    /**
     * 获取职业列表
     * @return
     */
    @GetMapping("/api/maps/professions")
    public ResponseEntity<Map<Integer, String>> getProfessions() {
        Map<Integer, String> map = userProfileService.getProfessionMap();
        return ResponseEntity.ok(map);
    }

    /**
     * 获取学历列表
     * @return
     */
    @GetMapping("/api/maps/degrees")
    public ResponseEntity<Map<Integer, String>> getDegrees() {
        Map<Integer, String> map = userProfileService.getDegreeMap();
        return ResponseEntity.ok(map);
    }

    /**
     * 获取节日信息
     * @return
     */
    @GetMapping("/api/info/festival")
    public ResponseEntity<Festival> getFestivalInfo() {
        Festival festival = FestivalUtils.GetFestivalInfo();
        return ResponseEntity.ok(festival);
    }

    /**
     * 获取校园公众号列表
     * @return
     */
    @GetMapping("/api/info/wechataccounts")
    public ResponseEntity<List<WechatAccount>> getSchoolWechatAccountData() {
        List<WechatAccount> list = WechatAccountUtils.getWechatAccountList();
        return ResponseEntity.ok(list);
    }
}
