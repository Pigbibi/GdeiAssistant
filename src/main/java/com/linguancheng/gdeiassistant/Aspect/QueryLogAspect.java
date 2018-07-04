package com.gdeiassistant.gdeiassistant.Aspect;

import com.gdeiassistant.gdeiassistant.Pojo.Entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Aspect
@Component
public class QueryLogAspect {

    private Log log = LogFactory.getLog(QueryLogAspect.class);

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Pointcut("@annotation(com.gdeiassistant.gdeiassistant.Annotation.QueryLog)")
    public void QueryAction() {

    }

    @Pointcut("@annotation(com.gdeiassistant.gdeiassistant.Annotation.RestQueryLog)")
    public void RestQueryAction() {

    }

    @After("RestQueryAction()")
    public void RestSaveQueryLog(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String functionName = joinPoint.getSignature().getName();
        String username = Optional.ofNullable((User) args[1]).map(User::getUsername).orElse("unknown");
        String dateTime = dateFormat.format(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        switch (functionName) {
            //成绩查询
            case "GradeQuery":
                log.info("用户" + username + "于" + dateTime + "查询了成绩信息");
                break;

            //课表查询
            case "ScheduleQuery":
                log.info("用户" + username + "于" + dateTime + "查询了课表信息");
                break;

            //消费记录查询
            case "CardQuery":
                log.info("用户" + username + "于" + dateTime + "查询了消费记录信息");
                break;
        }
    }

    @After("QueryAction()")
    public void SaveQueryLog(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String functionName = joinPoint.getSignature().getName();
        String username = Optional.ofNullable(String.valueOf(WebUtils.getSessionAttribute((HttpServletRequest) args[0]
                , "username"))).orElse("unknown");
        String dateTime = dateFormat.format(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        switch (functionName) {
            //成绩查询
            case "GradeQuery":
                log.info("用户" + username + "于" + dateTime + "查询了成绩信息");
                break;

            //课表查询
            case "ScheduleQuery":
                log.info("用户" + username + "于" + dateTime + "查询了课表信息");
                break;

            //消费查询
            case "CardQuery":
                log.info("用户" + username + "于" + dateTime + "查询了消费记录信息");
                break;

            //校园卡信息查询
            case "CardInfoQuery":
                log.info("用户" + username + "于" + dateTime + "查询了校园卡基本信息");
                break;

            //校园卡挂失
            case "CardLost":
                log.info("用户" + username + "于" + dateTime + "进行了校园卡挂失操作");
                break;

            //查询空课室信息
            case "QuerySpareRoomList":
                log.info("用户" + username + "于" + dateTime + "查询了空课室信息");
                break;
        }
    }
}