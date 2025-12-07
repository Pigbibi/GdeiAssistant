package cn.gdeiassistant.Controller.CollectionQuery.RestController;

import cn.gdeiassistant.Exception.CommonException.NetWorkTimeoutException;
import cn.gdeiassistant.Exception.CommonException.ServerErrorException;
import cn.gdeiassistant.Exception.QueryException.ErrorQueryConditionException;
import cn.gdeiassistant.Pojo.CollectionQuery.CollectionDetailQuery;
import cn.gdeiassistant.Pojo.CollectionQuery.CollectionQuery;
import cn.gdeiassistant.Pojo.CollectionQuery.CollectionQueryResult;
import cn.gdeiassistant.Pojo.Entity.CollectionDetail;
import cn.gdeiassistant.Service.CollectionQuery.CollectionQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collection") // 统一API前缀
public class CollectionRestController {

    @Autowired
    private CollectionQueryService collectionQueryService;

    /**
     * 查询图书详细信息API
     *
     * @param collectionDetailQuery
     * @return
     */
    @GetMapping("/detail") // 明确指定GET方法和路径
    public ResponseEntity<CollectionDetail> collectionDetailQueryApi(@Validated CollectionDetailQuery collectionDetailQuery) throws ServerErrorException, NetWorkTimeoutException {
        CollectionDetail collectionDetail = collectionQueryService.CollectionDetailQuery(collectionDetailQuery);
        return ResponseEntity.ok(collectionDetail);
    }

    /**
     * 查询馆藏信息API
     *
     * @param collectionQuery
     * @return
     */
    @PostMapping("/query") // 明确指定POST方法和路径
    public ResponseEntity<CollectionQueryResult> collectionQueryApi(@Validated CollectionQuery collectionQuery) throws NetWorkTimeoutException, ServerErrorException, ErrorQueryConditionException {
        if (collectionQuery.getPage() == null) {
            collectionQuery.setPage(1);
        }
        CollectionQueryResult collectionQueryResult = collectionQueryService.CollectionQuery(collectionQuery.getPage(), collectionQuery.getBookname());
        return ResponseEntity.ok(collectionQueryResult);
    }
}
