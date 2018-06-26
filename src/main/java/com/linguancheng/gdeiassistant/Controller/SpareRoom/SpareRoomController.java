package com.gdeiassistant.gdeiassistant.Controller.SpareRoom;

import com.gdeiassistant.gdeiassistant.Annotation.QueryLog;
import com.gdeiassistant.gdeiassistant.Annotation.RestQueryLog;
import com.gdeiassistant.gdeiassistant.Enum.Base.ServiceResultEnum;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.SpareRoom;
import com.gdeiassistant.gdeiassistant.Pojo.Result.BaseResult;
import com.gdeiassistant.gdeiassistant.Pojo.Result.DataJsonResult;
import com.gdeiassistant.gdeiassistant.Pojo.SpareRoomQuery.SpareRoomQuery;
import com.gdeiassistant.gdeiassistant.Service.SpareRoom.SpareRoomService;
import com.gdeiassistant.gdeiassistant.Tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class SpareRoomController {

    @Autowired
    private SpareRoomService spareRoomService;

    /**
     * 进入空课表查询页面
     *
     * @return
     */
    @RequestMapping(value = "/spare")
    public ModelAndView ResolveSparePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Spare/spare");
        //获取可用的日期列表
        LocalDate localDate = LocalDate.now();
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i == 0) {
                Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
                Date date = Date.from(instant);
                dateList.add(date);
            } else {
                localDate = localDate.plusDays(1);
                Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
                Date date = Date.from(instant);
                dateList.add(date);
            }
        }
        modelAndView.addObject("DateList", dateList);
        return modelAndView;
    }

    /**
     * 查询空课表信息
     *
     * @param request
     * @param spareRoomQuery
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/sparequery", method = RequestMethod.POST)
    @QueryLog
    @ResponseBody
    public DataJsonResult<List<SpareRoom>> QuerySpareRoomList(HttpServletRequest request
            , @Validated SpareRoomQuery spareRoomQuery, BindingResult bindingResult) {
        DataJsonResult<List<SpareRoom>> jsonResult = new DataJsonResult<>();
        if (bindingResult.hasErrors()) {
            jsonResult.setSuccess(false);
            jsonResult.setErrorMessage("请求参数不合法");
        } else {
            String username = (String) WebUtils.getSessionAttribute(request, "username");
            String keycode = (String) WebUtils.getSessionAttribute(request, "keycode");
            String number = (String) WebUtils.getSessionAttribute(request, "number");
            if (!StringUtils.isBlank(username) && !StringUtils.isBlank(keycode) && !StringUtils.isBlank(number)) {
                BaseResult<List<SpareRoom>, ServiceResultEnum> baseResult = spareRoomService
                        .QuerySpareRoom(request, username, keycode, number, spareRoomQuery);
                switch (baseResult.getResultType()) {
                    case SUCCESS:
                        jsonResult.setSuccess(true);
                        jsonResult.setData(baseResult.getResultData());
                        break;

                    case EMPTY_RESULT:
                    case ERROR_CONDITION:
                        jsonResult.setSuccess(false);
                        jsonResult.setErrorMessage("没有空闲的课室");
                        break;

                    case TIME_OUT:
                        jsonResult.setSuccess(false);
                        jsonResult.setErrorMessage("网络连接超时，请重试");
                        break;

                    case SERVER_ERROR:
                        jsonResult.setSuccess(false);
                        jsonResult.setErrorMessage("教务系统异常，请稍后再试");
                        break;

                    case PASSWORD_INCORRECT:
                        jsonResult.setSuccess(false);
                        jsonResult.setErrorMessage("你的密码已更新，请重新登录");
                        break;
                }
            } else {
                jsonResult.setSuccess(false);
                jsonResult.setErrorMessage("用户身份凭证已过期，请重新登录");
            }
        }
        return jsonResult;
    }
}