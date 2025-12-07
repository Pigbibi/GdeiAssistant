package cn.gdeiassistant.Controller.LostAndFound.RestController;

import cn.gdeiassistant.Annotation.RecordIPAddress;
import cn.gdeiassistant.Constant.ValueConstantUtils;
import cn.gdeiassistant.Enum.IPAddress.IPAddressEnum;
import cn.gdeiassistant.Exception.DatabaseException.ConfirmedStateException;
import cn.gdeiassistant.Exception.DatabaseException.NotAvailableStateException;
import cn.gdeiassistant.Pojo.Entity.LostAndFoundInfo;
import cn.gdeiassistant.Pojo.Entity.LostAndFoundItem;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.Socialising.LostAndFound.LostAndFoundService;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lostandfound") // 统一API前缀
public class LostAndFoundRestController {

    @Autowired
    private LostAndFoundService lostAndFoundService;

    /**
     * 分页查询失物信息 (原有API)
     *
     * @param start
     * @return
     */
    @RequestMapping(value = "/lostitem/start/{start}", method = RequestMethod.GET) // 保留原有路径
    public ResponseEntity<DataJsonResult<List<LostAndFoundItem>>> getLostItemApi(@PathVariable("start") int start) throws Exception {
        List<LostAndFoundItem> lostItemList = lostAndFoundService.QueryLostItems(start);
        return ResponseEntity.ok(new DataJsonResult<>(true, lostItemList));
    }

    /**
     * 分页查询招领信息 (原有API)
     *
     * @param start
     * @return
     */
    @RequestMapping(value = "/founditem/start/{start}", method = RequestMethod.GET) // 保留原有路径
    public ResponseEntity<DataJsonResult<List<LostAndFoundItem>>> getFoundInfoApi(@PathVariable("start") int start) throws Exception {
        List<LostAndFoundItem> foundItemList = lostAndFoundService.QueryFoundItems(start);
        return ResponseEntity.ok(new DataJsonResult<>(true, foundItemList));
    }

    /**
     * 关键字查询失物招领信息 (原有API，但路径和方法名需要调整以避免冲突和更清晰)
     * 注意：原有API的路径是 /api/lostandfound/lostitem/type/{type}/start/{start}，但方法名是SearchLostAndFoundInfo，且是POST
     * 我将创建一个新的GET API来处理关键字搜索，并保留原有POST API的逻辑
     *
     * @param lostType 0 for lost, 1 for found
     * @param keywords
     * @param start
     * @return
     */
    @GetMapping("/search/keyword/{keywords}/type/{lostType}/start/{start}") // 新增GET API for search
    public ResponseEntity<DataJsonResult<List<LostAndFoundItem>>> searchLostAndFoundInfoByKeywordApi(
            @PathVariable("lostType") @Range(min = 0, max = 1) Integer lostType,
            @PathVariable("keywords") @NotBlank @Length(min = 1, max = 50) String keywords,
            @PathVariable("start") int start) throws Exception {
        List<LostAndFoundItem> lostAndFoundItemList = null;
        if (lostType.equals(0)) { // 失物
            lostAndFoundItemList = lostAndFoundService.QueryLostItemsWithKeyword(keywords, start);
        } else { // 招领
            lostAndFoundItemList = lostAndFoundService.QueryFoundItemsWithKeyword(keywords, start);
        }
        return ResponseEntity.ok(new DataJsonResult<>(true, lostAndFoundItemList));
    }

    /**
     * 获取失物招领物品预览图 (原有API)
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/item/id/{id}/preview", method = RequestMethod.GET) // 保留原有路径
    public ResponseEntity<DataJsonResult<String>> getLostAndFoundItemPreviewImageApi(HttpServletRequest request, @PathVariable("id") int id) {
        List<String> list = lostAndFoundService.GetLostAndFoundItemPictureURL(id);
        if (list != null && !list.isEmpty()) {
            String previewImageURl = list.get(0);
            return ResponseEntity.ok(new DataJsonResult<>(true, previewImageURl));
        } else {
            return ResponseEntity.ok(new DataJsonResult<>(false, "获取失物招领商品预览图失败"));
        }
    }

    /**
     * 根据分类查找失物信息 (原有API，但路径和方法名需要调整以避免冲突和更清晰)
     *
     * @param type
     * @param start
     * @return
     */
    @GetMapping("/lostitem/type/{type}/start/{start}") // 标准化为 @GetMapping
    public ResponseEntity<DataJsonResult<List<LostAndFoundItem>>> searchLostInfoByTypeApi(
            @PathVariable("type") @Range(min = 0, max = 11) Integer type,
            @PathVariable("start") Integer start) throws Exception {
        List<LostAndFoundItem> lostAndFoundItemList = lostAndFoundService.QueryLostItemsByType(type, start);
        return ResponseEntity.ok(new DataJsonResult<>(true, lostAndFoundItemList));
    }

    /**
     * 根据分类查找招领信息 (原有API)
     *
     * @param type
     * @param start
     * @return
     */
    @RequestMapping(value = "/founditem/type/{type}/start/{start}", method = RequestMethod.GET) // 保留原有路径
    public ResponseEntity<DataJsonResult<List<LostAndFoundItem>>> searchFoundInfoByTypeApi(
            @PathVariable("type") @Range(min = 0, max = 11) Integer type,
            @PathVariable("start") Integer start) throws Exception {
        List<LostAndFoundItem> lostAndFoundItemList = lostAndFoundService.QueryFoundItemsByType(type, start);
        return ResponseEntity.ok(new DataJsonResult<>(true, lostAndFoundItemList));
    }

    /**
     * 确认寻回物品 (原有API)
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/item/id/{id}/didfound", method = RequestMethod.POST) // 保留原有路径
    public ResponseEntity<JsonResult> didFoundItemApi(HttpServletRequest request, @PathVariable("id") Integer id) throws Exception {
        lostAndFoundService.VerifyLostAndFoundInfoEditAccess(request.getSession().getId(), id);
        lostAndFoundService.UpdateLostAndFoundItemState(id, 1); // 1表示已寻回
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 更新失物招领信息 (原有API)
     *
     * @param id
     * @param lostAndFoundItem
     * @return
     */
    @RequestMapping(value = "/item/id/{id}", method = RequestMethod.POST) // 保留原有路径
    @RecordIPAddress(type = IPAddressEnum.POST)
    public ResponseEntity<JsonResult> updateLostAndFoundInfoApi(HttpServletRequest request, @PathVariable("id") Integer id,
                                                                @Validated LostAndFoundItem lostAndFoundItem) throws Exception {
        lostAndFoundService.VerifyLostAndFoundInfoEditAccess(request.getSession().getId(), id);
        lostAndFoundItem.setId(id); // 确保ID设置正确
        lostAndFoundService.UpdateLostAndFoundItem(lostAndFoundItem, id);
        return ResponseEntity.ok(new JsonResult(true));
    }

    /**
     * 添加失物招领信息 (原有API)
     *
     * @param request
     * @param lostAndFoundItem
     * @param image1
     * @param image2
     * @param image3
     * @param image4
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/item", method = RequestMethod.POST) // 保留原有路径
    @RecordIPAddress(type = IPAddressEnum.POST)
    public ResponseEntity<JsonResult> addLostAndFoundInfoApi(HttpServletRequest request,
                                                             @Validated LostAndFoundItem lostAndFoundItem,
                                                             @RequestParam("image1") MultipartFile image1,
                                                             @RequestParam(value = "image2", required = false) MultipartFile image2,
                                                             @RequestParam(value = "image3", required = false) MultipartFile image3,
                                                             @RequestParam(value = "image4", required = false) MultipartFile image4) throws Exception {
        if (image1 == null || image1.getSize() <= 0 || image1.getSize() >= ValueConstantUtils.MAX_IMAGE_SIZE) {
            return ResponseEntity.ok(new JsonResult(false, "不合法的图片文件"));
        }
        lostAndFoundItem = lostAndFoundService.AddLostAndFoundItem(lostAndFoundItem, request.getSession().getId());
        //添加二手交易数据成功，进行图片上传
        lostAndFoundService.UploadLostAndFoundItemPicture(lostAndFoundItem.getId(), 1, image1.getInputStream());
        if (image2 != null && image2.getSize() > 0 && image2.getSize() < ValueConstantUtils.MAX_IMAGE_SIZE) {
            lostAndFoundService.UploadLostAndFoundItemPicture(lostAndFoundItem.getId(), 2, image2.getInputStream());
            if (image3 != null && image3.getSize() > 0 && image3.getSize() < ValueConstantUtils.MAX_IMAGE_SIZE) {
                lostAndFoundService.UploadLostAndFoundItemPicture(lostAndFoundItem.getId(), 3, image3.getInputStream());
                if (image4 != null && image4.getSize() > 0 && image4.getSize() < ValueConstantUtils.MAX_IMAGE_SIZE) {
                    lostAndFoundService.UploadLostAndFoundItemPicture(lostAndFoundItem.getId(), 4, image4.getInputStream());
                }
            }
        }
        return ResponseEntity.ok(new JsonResult(true));
    }

    // --- 从 LostAndFoundController 迁移过来的 API ---

    /**
     * 获取个人失物招领物品列表API
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/personal/items") // 新增API路径
    public ResponseEntity<DataJsonResult<Map<String, List<LostAndFoundItem>>>> getPersonalLostAndFoundItemsApi(HttpServletRequest request) throws Exception {
        List<LostAndFoundItem> lostAndFoundItemList = lostAndFoundService.QueryPersonalLostAndFoundItems(request.getSession().getId());
        List<LostAndFoundItem> lostItemList = new ArrayList<>();
        List<LostAndFoundItem> foundItemList = new ArrayList<>();
        List<LostAndFoundItem> didFoundItemList = new ArrayList<>();
        for (LostAndFoundItem lostAndFoundItem : lostAndFoundItemList) {
            if (lostAndFoundItem.getState().equals(1)) { // 1表示已寻回
                didFoundItemList.add(lostAndFoundItem);
            } else {
                if (lostAndFoundItem.getLostType().equals(0)) { // 0表示失物
                    lostItemList.add(lostAndFoundItem);
                } else { // 1表示招领
                    foundItemList.add(lostAndFoundItem);
                }
            }
        }
        Map<String, List<LostAndFoundItem>> data = new HashMap<>();
        data.put("lost", lostItemList);
        data.put("found", foundItemList);
        data.put("didFound", didFoundItemList);
        return ResponseEntity.ok(new DataJsonResult<>(true, data));
    }

    /**
     * 获取失物招领商品详细信息API (用于编辑和详情页)
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/item/id/{id}/detail") // 新增API路径
    public ResponseEntity<DataJsonResult<LostAndFoundInfo>> getLostAndFoundItemDetailApi(@PathVariable("id") int id) throws Exception {
        LostAndFoundInfo lostAndFoundInfo = lostAndFoundService.QueryLostAndFoundInfoByID(id);
        // 业务逻辑判断
        if (lostAndFoundInfo.getLostAndFoundItem().getState().equals(0)) {
            throw new NotAvailableStateException("已下架的失物招领信息不能查看");
        } else if (lostAndFoundInfo.getLostAndFoundItem().getState().equals(2)) {
            throw new ConfirmedStateException("已寻回的失物招领信息不能查看");
        }
        return ResponseEntity.ok(new DataJsonResult<>(true, lostAndFoundInfo));
    }
}
