package cn.gdeiassistant.Controller.SchoolNews.RestController;

import cn.gdeiassistant.Exception.DatabaseException.DataNotExistException;
import cn.gdeiassistant.Pojo.Entity.NewInfo;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Service.Information.SchoolNews.SchoolNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // 导入 ResponseEntity
import org.springframework.web.bind.annotation.GetMapping; // 导入 GetMapping
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod; // 保持原有 RequestMethod 兼容
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schoolnews") // 统一API前缀，避免与原有/rest/new冲突
public class SchoolNewsRestController {

    @Autowired
    private SchoolNewsService schoolNewsService;

    /**
     * 分页查询新闻列表 (原有API，修改路径和返回类型为ResponseEntity)
     *
     * @param type
     * @param start
     * @param size
     * @return
     * @throws DataNotExistException
     */
    @RequestMapping(value = "/news/type/{type}/start/{start}/size/{size}", method = RequestMethod.GET) // 调整路径
    public ResponseEntity<DataJsonResult<List<NewInfo>>> queryNewInfoListApi(@PathVariable("type") Integer type
            , @PathVariable("start") Integer start, @PathVariable("size") Integer size) throws DataNotExistException {
        List<NewInfo> newInfoList = schoolNewsService.QueryNewInfoList(type, start, size);
        return ResponseEntity.ok(new DataJsonResult<>(true, newInfoList));
    }

    /**
     * 获取新闻通知详细信息API (从SchoolNewsController迁移)
     *
     * @param id
     * @return
     * @throws DataNotExistException
     */
    @GetMapping("/news/id/{id}/detail") // 新增API路径
    public ResponseEntity<DataJsonResult<NewInfo>> getNewInfoDetailApi(@PathVariable("id") String id) throws DataNotExistException {
        NewInfo newInfo = schoolNewsService.QueryNewDetailInfo(id);
        return ResponseEntity.ok(new DataJsonResult<>(true, newInfo));
    }
}
