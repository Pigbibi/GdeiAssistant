package cn.gdeiassistant.Controller.SpareRoom.RestController;

import cn.gdeiassistant.Annotation.QueryLogPersistence;
import cn.gdeiassistant.Annotation.TrialData;
import cn.gdeiassistant.Pojo.Entity.SpareRoom;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Pojo.SpareRoomQuery.SpareRoomQuery;
import cn.gdeiassistant.Service.SpareRoom.SpareRoomService;
import cn.gdeiassistant.Tools.Utils.WeekUtils; // 导入 WeekUtils
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // 导入 ResponseEntity
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping; // 导入 GetMapping
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod; // 保持原有 RequestMethod 兼容
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/spareroom") // 统一API前缀
public class SpareRoomRestController {

    @Autowired
    private SpareRoomService spareRoomService;

    /**
     * 查询空课表信息 (原有API，修改返回类型为ResponseEntity)
     *
     * @param request
     * @param spareRoomQuery
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST) // 调整路径
    @QueryLogPersistence
    @TrialData(value = "spare", rest = false)
    public ResponseEntity<DataJsonResult<List<SpareRoom>>> querySpareRoomListApi(HttpServletRequest request
            , @Validated SpareRoomQuery spareRoomQuery) throws Exception {
        List<SpareRoom> spareRoomList = spareRoomService.QuerySpareRoom(request.getSession().getId()
                , spareRoomQuery);
        if (spareRoomList == null || spareRoomList.isEmpty()) {
            return ResponseEntity.ok(new DataJsonResult<>(new JsonResult(false, "没有空闲的课室")));
        }
        return ResponseEntity.ok(new DataJsonResult<>(true, spareRoomList));
    }

    /**
     * 获取可用的周数列表API (从SpareRoomController迁移)
     *
     * @return
     */
    @GetMapping("/weeks") // 新增API路径
    public ResponseEntity<DataJsonResult<List<Integer>>> getAvailableWeeksApi() {
        List<Integer> weekList = new ArrayList<>();
        for (int i = WeekUtils.GetCurrentWeek(); i <= 20; i++) {
            weekList.add(i);
        }
        return ResponseEntity.ok(new DataJsonResult<>(true, weekList));
    }
}
