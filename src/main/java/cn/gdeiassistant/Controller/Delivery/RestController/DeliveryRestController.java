package cn.gdeiassistant.Controller.Delivery.RestController;

import cn.gdeiassistant.Exception.DatabaseException.DataNotExistException;
import cn.gdeiassistant.Pojo.Entity.DeliveryOrder;
import cn.gdeiassistant.Pojo.Entity.DeliveryTrade;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Service.Profile.UserProfileService;
import cn.gdeiassistant.Service.Socialising.Delivery.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/delivery") // 统一API前缀
public class DeliveryRestController {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private UserProfileService userProfileService;

    /**
     * 获取全民快递主页所需数据 (头像和昵称)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/index/data")
    public ResponseEntity<Map<String, String>> getDeliveryIndexData(HttpServletRequest request) throws Exception {
        Map<String, String> data = new HashMap<>();
        String avatarUrl = userProfileService.GetSelfUserAvatar(request.getSession().getId());
        String nickname = userProfileService.GetSelfUserProfile(request.getSession().getId()).getNickname();
        data.put("avatarURL", avatarUrl);
        data.put("nickname", nickname);
        return ResponseEntity.ok(data);
    }

    /**
     * 获取快递订单详细信息API
     *
     * @param request
     * @param id
     * @return
     * @throws DataNotExistException
     */
    @GetMapping("/order/id/{id}")
    public ResponseEntity<Map<String, Object>> getDeliveryOrderDetailApi(HttpServletRequest request, @PathVariable("id") Integer id) throws DataNotExistException {
        Map<String, Object> responseData = new HashMap<>();
        DeliveryOrder deliveryOrder = deliveryService.QueryDeliveryOrderByOrderId(id);
        responseData.put("deliveryOrder", deliveryOrder);

        if (deliveryOrder.getState().equals(1)) {
            DeliveryTrade deliveryTrade = deliveryService.QueryDeliveryTradeByOrderId(deliveryOrder.getOrderId());
            responseData.put("deliveryTrade", deliveryTrade);
        }

        int detailType = deliveryService.QueryDeliveryOrderDetailType(request.getSession().getId(), id);
        responseData.put("detailType", detailType);
        return ResponseEntity.ok(responseData);
    }

    /**
     * 获取代收交易详细信息API
     *
     * @param request
     * @param tradeId
     * @return
     * @throws DataNotExistException
     */
    @GetMapping("/trade/id/{id}")
    public ResponseEntity<Map<String, Object>> getDeliveryTradeDetailApi(HttpServletRequest request, @PathVariable("id") Integer tradeId) throws DataNotExistException {
        Map<String, Object> responseData = new HashMap<>();
        DeliveryTrade deliveryTrade = deliveryService.QueryDeliveryTradeByTradeId(tradeId);
        int detailType = deliveryService.QueryDeliveryTradeDetailType(request.getSession().getId(), tradeId);
        responseData.put("deliveryTrade", deliveryTrade);
        responseData.put("detailType", detailType);
        return ResponseEntity.ok(responseData);
    }

    /**
     * 获取个人下单列表API
     *
     * @param request
     * @return
     */
    @GetMapping("/personal/orders")
    public ResponseEntity<DataJsonResult<List<DeliveryOrder>>> getPersonalDeliveryOrders(HttpServletRequest request) {
        List<DeliveryOrder> personalDeliveryOrderList = deliveryService.QueryPersonalDeliveryOrder(request.getSession().getId());
        return ResponseEntity.ok(new DataJsonResult<>(true, personalDeliveryOrderList));
    }

    /**
     * 获取个人接单列表API
     *
     * @param request
     * @return
     */
    @GetMapping("/personal/acceptedorders")
    public ResponseEntity<DataJsonResult<List<DeliveryOrder>>> getPersonalAcceptedDeliveryOrders(HttpServletRequest request) {
        List<DeliveryOrder> acceptedDeliveryOrderList = deliveryService.QueryPersonalAcceptedDeliveryOrder(request.getSession().getId());
        return ResponseEntity.ok(new DataJsonResult<>(true, acceptedDeliveryOrderList));
    }
}
