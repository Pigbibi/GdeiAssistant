package cn.gdeiassistant.Controller.Reading.RestController;

import cn.gdeiassistant.Pojo.Entity.Reading;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Service.Information.Reading.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reading") // 统一API前缀
public class ReadingRestController {

    @Autowired
    private ReadingService readingService;

    /**
     * 加载专题阅读列表信息 (原有API，修改返回类型为ResponseEntity)
     *
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET) // 路径调整为根路径，或者使用 @GetMapping("/")
    public ResponseEntity<DataJsonResult<List<Reading>>> LoadReadingListApi() { // 方法名调整为Api后缀
        List<Reading> readingList = readingService.LoadingLatestReadingList();
        return ResponseEntity.ok(new DataJsonResult<>(true, readingList));
    }
}
