package cn.gdeiassistant.Controller.Ershou.RestController;

import cn.gdeiassistant.Annotation.RecordIPAddress;
import cn.gdeiassistant.Constant.ValueConstantUtils;
import cn.gdeiassistant.Enum.IPAddress.IPAddressEnum;
import cn.gdeiassistant.Exception.DatabaseException.ConfirmedStateException;
import cn.gdeiassistant.Exception.DatabaseException.NotAvailableStateException;
import cn.gdeiassistant.Pojo.Entity.ErshouInfo;
import cn.gdeiassistant.Pojo.Entity.ErshouItem;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.Socialising.Ershou.ErshouService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ershou") // 统一API前缀
public class ErshouRestController {

    @Autowired
    private ErshouService ershouService;

    /**
     * 分页查询二手交易信息
     *
     * @param start
     * @return
     */
    @GetMapping("/item/start/{start}") // 使用 @GetMapping
    public ResponseEntity<DataJsonResult<List<ErshouItem>>> getErshouItemApi(@PathVariable("start") int start) throws Exception {
        List<ErshouItem> ershouItemList = ershouService.QueryErshouItems(start);
        return ResponseEntity.ok(new DataJsonResult<>(true, ershouItemList));
    }

    /**
     * 添加新的二手交易信息
     *
     * @param ershouItem
     * @param image1
     * @param image2
     * @param image3
     * @param image4
     * @return
     */
    @PostMapping("/item") // 使用 @PostMapping
    @RecordIPAddress(type = IPAddressEnum.POST)
    public ResponseEntity<JsonResult> addErshouInfoApi(HttpServletRequest request
            , @Validated ErshouItem ershouItem, @RequestParam("image1") MultipartFile image1
            , @RequestParam(value = "image2", required = false) MultipartFile image2
            , @RequestParam(value = "image3", required = false) MultipartFile image3
            , @RequestParam(value = "image4", required = false) MultipartFile image4) throws Exception {
        if (image1 == null || image1.getSize() <= 0 || image1.getSize() >= ValueConstantUtils.MAX_IMAGE_SIZE) {
            return ResponseEntity.ok(new JsonResult(false, "不合法的图片文件"));
        } else {
            ershouItem = ershouService.AddErshouItem(ershouItem, request.getSession().getId());
            //添加二手交易数据成功，进行图片上传
            ershouService.UploadErshouItemPicture(ershouItem.getId(), 1, image1.getInputStream());
            if (image2 != null && image2.getSize() > 0 && image2.getSize() < ValueConstantUtils.MAX_IMAGE_SIZE) {
                ershouService.UploadErshouItemPicture(ershouItem.getId(), 2, image2.getInputStream());
                if (image3 != null && image3.getSize() > 0 && image3.getSize() < ValueConstantUtils.MAX_IMAGE_SIZE) {
                    ershouService.UploadErshouItemPicture(ershouItem.getId(), 3, image3.getInputStream());
                    if (image4 != null && image4.getSize() > 0 && image4.getSize() < ValueConstantUtils.MAX_IMAGE_SIZE) {
                        ershouService.UploadErshouItemPicture(ershouItem.getId(), 4, image4.getInputStream());
                    }
                }
            }
            return ResponseEntity.ok(new JsonResult(true));
        }
    }

    /**
     * 查询指定关键词的二手交易信息
     *
     * @param keyword
     * @param start
     * @return
     */
    @GetMapping("/keyword/{keyword}/start/{start}") // 使用 @GetMapping
    public ResponseEntity<DataJsonResult<List<ErshouItem>>> getErshouItemWithKeywordApi(@PathVariable("keyword") String keyword
            , @PathVariable("start") int start) throws Exception {
        List<ErshouItem> ershouItemList = ershouService.QueryErshouItemsWithKeyword(keyword, start);
        return ResponseEntity.ok(new DataJsonResult<>(true, ershouItemList));
    }

    /**
     * 获取二手交易商品预览图
     *
     * @param id
     * @return
     */
    @GetMapping("/item/id/{id}/preview") // 使用 @GetMapping
    public ResponseEntity<DataJsonResult<String>> getErshouItemPreviewImageApi(@PathVariable("id") int id) {
        List<String> list = ershouService.GetErshouItemPictureURL(id);
        if (list != null && !list.isEmpty()) {
            String previewImageURl = list.get(0);
            return ResponseEntity.ok(new DataJsonResult<>(true, previewImageURl));
        }
        return ResponseEntity.ok(new DataJsonResult<>(false, "获取二手交易商品预览图失败"));
    }

    /**
     * 查询特殊类型的二手交易信息
     *
     * @param type
     * @param start
     * @return
     */
    @GetMapping("/item/type/{type}/start/{start}") // 使用 @GetMapping
    public ResponseEntity<DataJsonResult<List<ErshouItem>>> getErshouItemByTypeApi(@PathVariable("type") @Validated @Range(min = 0, max = 11) int type
            , @PathVariable("start") int start) throws Exception {
        List<ErshouItem> ershouItemList = ershouService.QueryErshouItemByType(type, start);
        return ResponseEntity.ok(new DataJsonResult<>(true, ershouItemList));
    }

    /**
     * 更新二手交易信息
     *
     * @param request
     * @param ershouItem
     * @param id
     * @return
     */
    @PostMapping("/item/id/{id}") // 使用 @PostMapping
    @RecordIPAddress(type = IPAddressEnum.POST)
    public ResponseEntity<JsonResult> updateErshouItemApi(HttpServletRequest request, @Validated ErshouItem ershouItem
            , @PathVariable("id") int id) throws Exception {
        if (ershouItem.getPrice() <= 0 || ershouItem.getPrice() > 9999.99) {
            return ResponseEntity.ok(new JsonResult(false, "商品价格不合法"));
        }
        ershouItem.setId(id);
        ershouService.VerifyErshouInfoEditAccess(request.getSession().getId(), id);
        ershouService.UpdateErshouItem(request.getSession().getId(), ershouItem, id);
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 修改指定ID的二手交易商品状态
     *
     * @param request
     * @param id
     * @param state
     * @return
     */
    @PostMapping("/item/state/id/{id}") // 使用 @PostMapping
    public ResponseEntity<JsonResult> updateErshouItemStateApi(HttpServletRequest request, @PathVariable("id") int id
            , @Validated @Range(min = 0, max = 2) int state) throws Exception {
        ershouService.UpdateErshouItemState(request.getSession().getId(), id, state);
        return ResponseEntity.ok(new JsonResult(true));
    }

    // --- 从 ErshouController 迁移过来的 API ---

    /**
     * 获取二手交易商品详细信息API (用于编辑和详情页)
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/item/id/{id}/detail") // 使用 @GetMapping
    public ResponseEntity<DataJsonResult<ErshouInfo>> getErshouItemDetailApi(@PathVariable("id") int id) throws Exception {
        ErshouInfo ershouInfo = ershouService.QueryErshouInfoByID(id);
        if (ershouInfo.getErshouItem().getState().equals(0)) {
            throw new NotAvailableStateException("已下架的二手交易信息不能查看");
        } else if (ershouInfo.getErshouItem().getState().equals(2)) {
            throw new ConfirmedStateException("已出售的二手交易信息不能查看");
        }
        return ResponseEntity.ok(new DataJsonResult<>(true, ershouInfo));
    }

    /**
     * 获取个人二手交易物品列表API
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/personal/items") // 使用 @GetMapping
    public ResponseEntity<DataJsonResult<Map<String, List<ErshouItem>>>> getPersonalErshouItemsApi(HttpServletRequest request) throws Exception {
        List<ErshouItem> ershouItemList = ershouService.QueryPersonalErShouItems(request.getSession().getId());
        List<ErshouItem> availableErshouItemList = new ArrayList<>();
        List<ErshouItem> soldedErshouItemList = new ArrayList<>();
        List<ErshouItem> notAvailableErshouItemList = new ArrayList<>();
        for (ErshouItem ershouItem : ershouItemList) {
            switch (ershouItem.getState()) {
                case 0: // 下架
                    notAvailableErshouItemList.add(ershouItem);
                    break;
                case 1: // 待出售
                    availableErshouItemList.add(ershouItem);
                    break;
                case 2: // 已出售
                    soldedErshouItemList.add(ershouItem);
                    break;
            }
        }
        Map<String, List<ErshouItem>> data = new HashMap<>();
        data.put("notAvailable", notAvailableErshouItemList);
        data.put("available", availableErshouItemList);
        data.put("solded", soldedErshouItemList);
        return ResponseEntity.ok(new DataJsonResult<>(true, data));
    }
}