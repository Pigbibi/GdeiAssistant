package cn.gdeiassistant.Controller.Cron.RestController;

import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.Account.GraduatedAccountService;
import cn.gdeiassistant.Service.SchoolNews.SchoolNewsService;
import cn.gdeiassistant.Service.Secret.SecretService;
import cn.gdeiassistant.Service.Wechat.WechatService;
import cn.gdeiassistant.Service.GradeQuery.GradeService;
import cn.gdeiassistant.Service.ScheduleQuery.ScheduleService;
import cn.gdeiassistant.Service.Wechat.WechatAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class CronRestController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SecretService secretService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private WechatAccountService wechatAccountService;

    @Autowired
    private GraduatedAccountService graduatedAccountService;

    @Autowired
    private SchoolNewsService schoolNewsService;

    @RequestMapping(value = "/cron/grade", method = RequestMethod.GET)
    public JsonResult CacheGradeData(HttpServletRequest request) {
        gradeService.SynchronizeGradeData();
        return new JsonResult(true);
    }

    @RequestMapping(value = "/cron/schedule", method = RequestMethod.GET)
    public JsonResult CacheScheduleData(HttpServletRequest request) {
        scheduleService.SynchronizeScheduleData();
        return new JsonResult(true);
    }

    @RequestMapping(value = "/cron/wechat/reading", method = RequestMethod.GET)
    public JsonResult SyncWechatReadingData(HttpServletRequest request) {
        wechatService.SyncWechatReadingItem();
        return new JsonResult(true);
    }

    @RequestMapping(value = "/cron/wechat/account", method = RequestMethod.GET)
    public JsonResult UpdateWechatAccountData(HttpServletRequest request) {
        wechatAccountService.UpdateAccountData();
        return new JsonResult(true);
    }

    @RequestMapping(value = "/cron/secret", method = RequestMethod.GET)
    public JsonResult DeleteSecretData(HttpServletRequest request) throws Exception {
        secretService.DeleteTimerSecretInfos();
        return new JsonResult(true);
    }

    @RequestMapping(value = "/cron/graduation", method = RequestMethod.GET)
    public JsonResult ProceedGraduationProgram(HttpServletRequest request) throws Exception {
        graduatedAccountService.ProceedGraduationProgram();
        return new JsonResult(true);
    }

    @RequestMapping(value = "/cron/news", method = RequestMethod.GET)
    public JsonResult CollectNews(HttpServletRequest request) throws InterruptedException, ExecutionException, IOException {
        schoolNewsService.CollectNews();
        return new JsonResult(true);
    }

}
