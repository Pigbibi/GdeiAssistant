package com.gdeiassistant.gdeiassistant.Service.ScheduleQuery;

import com.gdeiassistant.gdeiassistant.Enum.Base.ServiceResultEnum;
import com.gdeiassistant.gdeiassistant.Exception.CommonException.PasswordIncorrectException;
import com.gdeiassistant.gdeiassistant.Exception.CommonException.ServerErrorException;
import com.gdeiassistant.gdeiassistant.Factory.HttpClientFactory;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.Schedule;
import com.gdeiassistant.gdeiassistant.Pojo.Result.BaseResult;
import com.gdeiassistant.gdeiassistant.Tools.ScheduleColorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 广东二师助手团队 林冠成 版权所有
 * All rights reserved © 2016 - 2018
 * Author:林冠成
 * Date:2018/3/4
 */
@Service
public class ScheduleQueryService {

    private String url;

    private int startYear;

    private int startMonth;

    private int startDate;

    private int year;

    private int term;

    private int timeout;

    @Value("#{propertiesReader['schedule.start.date']}")

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    @Value("#{propertiesReader['schedule.start.month']}")
    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    @Value("#{propertiesReader['schedule.start.year']}")
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    @Value("#{propertiesReader['schedule.year']}")
    public void setYear(int year) {
        this.year = year;
    }

    @Value("#{propertiesReader['schedule.term']}")
    public void setTerm(int term) {
        this.term = term;
    }

    @Value("#{propertiesReader['education.system.url']}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("#{propertiesReader['timeout.schedulequery']}")
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Autowired
    private HttpClientFactory httpClientFactory;

    private Log log = LogFactory.getLog(ScheduleQueryService.class);

    /**
     * 获取当前周数
     *
     * @return
     */
    public int getCurrentWeek() {
        //当前日期
        LocalDate current = LocalDate.now();
        //开学日期
        LocalDate start = LocalDate.of(startYear, startMonth, startDate);
        //计算当前周数
        int result = (int) (start.until(current, ChronoUnit.WEEKS) + 1);
        if (result < 1) {
            return 1;
        }
        if (result > 20) {
            return 20;
        }
        return result;
    }

    /**
     * 获取指定周数的课表信息，过滤多余的课表信息
     *
     * @param scheduleList
     * @param week
     * @return
     */
    public List<Schedule> getSpecifiedWeekSchedule(List<Schedule> scheduleList, int week) {
        List<Schedule> list = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            String scheduleWeek = schedule.getScheduleWeek();
            String weekString[] = scheduleWeek.split("-");
            int minWeekNumber;
            int maxWeekNumber;
            if (weekString[1].contains("|")) {
                String s1[] = weekString[1].split("\\|");
                minWeekNumber = Integer.valueOf(weekString[0].substring(1, weekString[0].length()));
                maxWeekNumber = Integer.valueOf(s1[0].substring(0, s1[0].length() - 1));
            } else {
                minWeekNumber = Integer.valueOf(weekString[0].substring(1, weekString[0].length()));
                maxWeekNumber = Integer.valueOf(weekString[1].substring(0, weekString[1].length() - 1));
            }
            //判断当前周是否在此课表信息的周数范围内
            if (week >= minWeekNumber && week <= maxWeekNumber) {
                //当前周数在课表周数范围内,则显示当前课表
                list.add(schedule);
            }
        }
        return list;
    }


    /**
     * 查询课表信息
     *
     * @param request
     * @return
     */
    public BaseResult<List<Schedule>, ServiceResultEnum> ScheduleQuery(HttpServletRequest request, String username, String keycode, String number) {
        BaseResult<List<Schedule>, ServiceResultEnum> result = new BaseResult<>();
        CloseableHttpClient httpClient = null;
        try {
            httpClient = httpClientFactory.getHttpClient(request.getSession(), timeout);
            HttpGet httpGet = new HttpGet(url + "cas_verify.aspx?i=" + username + "&k=" + keycode);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //进入教务系统个人主页
                httpGet = new HttpGet(url + "xs_main.aspx?xh=" + number);
                httpResponse = httpClient.execute(httpGet);
                Document document = Jsoup.parse(EntityUtils.toString(httpResponse.getEntity()));
                if (httpResponse.getStatusLine().getStatusCode() == 200 && document.title().equals("正方教务管理系统")) {
                    //成功进入学生个人主页,进行课表查询操作
                    httpGet = new HttpGet(url + "xskbcx.aspx?xh=" + number);
                    httpResponse = httpClient.execute(httpGet);
                    document = Jsoup.parse(EntityUtils.toString(httpResponse.getEntity()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        Elements xqdOptions = document.getElementsByAttributeValue("name", "xqd").first().select("option");
                        //默认的学年学期是否与配置文件中配置的学年学期相同
                        boolean isDefaultEqualsCurrent = false;
                        int defaultTerm = 0;
                        int defaultYear = 0;
                        for (Element option : xqdOptions) {
                            if (option.hasAttr("selected")) {
                                defaultTerm = Integer.valueOf(option.attr("value"));
                            }
                        }
                        Elements xndOptions = document.getElementsByAttributeValue("name", "xnd").first().select("option");
                        for (Element option : xndOptions) {
                            if (option.hasAttr("selected")) {
                                defaultYear = Integer.parseInt((option.attr("value").split("-"))[0]);
                            }
                        }
                        if (defaultYear == year && defaultTerm == term) {
                            isDefaultEqualsCurrent = true;
                        }
                        if (!isDefaultEqualsCurrent) {
                            HttpPost httpPost = new HttpPost(url + "xskbcx.aspx?xh=" + number);
                            List<BasicNameValuePair> basicNameValuePairList = new ArrayList<>();
                            basicNameValuePairList.add(new BasicNameValuePair("__EVENTTARGET", ""));
                            basicNameValuePairList.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                            basicNameValuePairList.add(new BasicNameValuePair("__VIEWSTATE", document.getElementsByAttributeValue("name", "__VIEWSTATE").first().val()));
                            basicNameValuePairList.add(new BasicNameValuePair("xnd", document.getElementsByAttributeValue("name", "xnd").first().select("option").first().attr("value")));
                            basicNameValuePairList.add(new BasicNameValuePair("xqd", document.getElementsByAttributeValue("name", "xqd").first().select("option").get(term - 1).attr("value")));
                            httpPost.setEntity(new UrlEncodedFormEntity(basicNameValuePairList, StandardCharsets.UTF_8));
                            httpResponse = httpClient.execute(httpPost);
                            document = Jsoup.parse(EntityUtils.toString(httpResponse.getEntity()));
                            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                                throw new ServerErrorException("教务系统异常");
                            }
                        }
                        //获取存放课表信息的表格
                        Element table = document.getElementById("Table1");
                        //获取表格中的所有行
                        Elements trs = table.select("tr");
                        //特殊的空课程,用于标记当前单元格被上边的课程信息占用,即上边同列课程的课程时长大于1
                        Schedule specialEmptySchedule = new Schedule();
                        //存放课表信息的数组,包含特殊的空课程对象
                        Schedule[][] schedulesWithSpecialEmptySchedule = new Schedule[70][10];
                        //存放课表信息的列表,过滤特殊的空课程对象后添加入该列表
                        List<Schedule> schedulesWithoutSpecialEmptySchedule = new ArrayList<>();
                        //当前单元格位置position值
                        int currentPosition = 0;
                        //当前行最大位置position值
                        int currentRowMaxPosition = 0;
                        //前两行为行列信息,从第三行开始获取信息
                        for (int row = 2; row < trs.size(); row++) {
                            //获取该行内所有列
                            Elements tds = trs.get(row).select("td");
                            //记录当前访问列数据的游标,若当前为第3行或第7行或第11行,初始列游标值为2,否则初始值为1
                            //因为特殊行里面的第一列包含上午/下午的信息提示
                            int currentColumnIndexInThisRow;
                            if (row == 2 || row == 6 || row == 10) {
                                currentColumnIndexInThisRow = 2;
                            } else {
                                currentColumnIndexInThisRow = 1;
                            }
                            //当前行的列最大游标
                            int maxColumnIndex;
                            if (tds.size() < 7) {
                                maxColumnIndex = tds.size() - 1;
                            } else {
                                maxColumnIndex = 6;
                            }
                            //遍历当前行,获取课表信息
                            for (currentRowMaxPosition = currentRowMaxPosition + 7; currentPosition < currentRowMaxPosition; currentPosition++) {
                                if (schedulesWithSpecialEmptySchedule[currentPosition][0] == specialEmptySchedule) {
                                    //当前position指向特殊空Schedule对象,跳过当前单元格
                                } else {
                                    //判断当前行是否已经遍历完
                                    if (currentPosition % 7 <= maxColumnIndex) {
                                        //判断当前position的课程信息是否为空课表信息
                                        //下面的字符非空格而是一个特殊的Unicode字符
                                        if (tds.get(currentColumnIndexInThisRow).text().equals(" ")) {
                                            //不存在课表信息
                                        } else {
                                            //当前td标签属性,代表课程时长
                                            int rowspan;
                                            //判断td标签有无rowspan属性,该属性代表课程时长
                                            if (tds.get(currentColumnIndexInThisRow).hasAttr("rowspan")) {
                                                //通过rowspan属性得到课程时长
                                                rowspan = Integer.parseInt(tds.get(currentColumnIndexInThisRow).attr("rowspan"));
                                                //将当前单元格下方对应的原单元格的课程信息标记为特殊空课程
                                                switch (rowspan) {
                                                    case 2:
                                                        schedulesWithSpecialEmptySchedule[currentPosition + 7][0] = specialEmptySchedule;
                                                        break;

                                                    case 3:
                                                        schedulesWithSpecialEmptySchedule[currentPosition + 7][0] = specialEmptySchedule;
                                                        schedulesWithSpecialEmptySchedule[currentPosition + 14][0] = specialEmptySchedule;
                                                        break;

                                                    case 4:
                                                        schedulesWithSpecialEmptySchedule[currentPosition + 7][0] = specialEmptySchedule;
                                                        schedulesWithSpecialEmptySchedule[currentPosition + 14][0] = specialEmptySchedule;
                                                        schedulesWithSpecialEmptySchedule[currentPosition + 21][0] = specialEmptySchedule;
                                                        break;
                                                }
                                            } else {
                                                //如果td标签没有rowspan属性,则该课程课程时长为默认的1
                                                rowspan = 1;
                                            }
                                            //将单元格里的课表信息按独立行进行分割单独处理
                                            String string[] = tds.get(currentColumnIndexInThisRow).text().split(" ");
                                            //记录单元格中的独立课表信息下标
                                            int n = 0;
                                            if (string.length == 0) {
                                                schedulesWithSpecialEmptySchedule[currentPosition][0] = specialEmptySchedule;
                                            } else {
                                                for (int j = 0; j < string.length; j++) {
                                                    if (string[j].isEmpty() || string[j].substring(0, 1).equals("<") || string[j].equals(" ") || string[j].substring(0, 1).equals("(")) {
                                                        //不是有效的课表头信息,跳过并查询下一个独立行的信息,直到得到有效的课表头信息
                                                    } else {
                                                        //有效的课表头信息,进行信息处理
                                                        //检查当前独立课表信息中课表名称有无空格
                                                        boolean isThisScheduleNameContainSpace = false;
                                                        if (!string[j + 2].contains("{")) {
                                                            j++;
                                                            isThisScheduleNameContainSpace = true;
                                                        }
                                                        String time[] = string[j + 2].split("[{]");
                                                        String week = time[1].substring(0, time[1].length() - 1);
                                                        //课程名称
                                                        String name = null;
                                                        //若当前独立课表信息中，课表名称存在空格，则将空格去除合并
                                                        if (isThisScheduleNameContainSpace) {
                                                            name = string[j - 1] + string[j];
                                                        } else {
                                                            name = string[j];
                                                        }
                                                        //课程类型
                                                        String type = string[j + 1];
                                                        //课程节数
                                                        String lesson = time[0];
                                                        //任课教师
                                                        String teacher = string[j + 3];
                                                        //上课地点
                                                        String location = "";
                                                        //检查是否已经安排课程
                                                        if (string.length <= 4 || string[j + 4] == null) {
                                                            location = "暂未安排";
                                                        } else {
                                                            if (string[j + 4].contains("（")) {
                                                                String locations[] = string[j + 4].split("（");
                                                                location = locations[0];
                                                            } else {
                                                                location = string[j + 4];
                                                            }
                                                        }
                                                        Schedule schedule = new Schedule();
                                                        schedule.setPosition(currentPosition);
                                                        schedule.setScheduleLength(rowspan);
                                                        schedule.setScheduleName(name);
                                                        schedule.setScheduleType(type);
                                                        schedule.setScheduleLesson(lesson);
                                                        schedule.setScheduleWeek(week);
                                                        schedule.setScheduleTeacher(teacher);
                                                        schedule.setScheduleLocation(location);
                                                        schedule.setRow(row - 2);
                                                        if (row == 2 || row == 6 || row == 10) {
                                                            schedule.setColumn(currentColumnIndexInThisRow - 2);
                                                        } else {
                                                            schedule.setColumn(currentColumnIndexInThisRow - 1);
                                                        }
                                                        schedule.setColorCode(ScheduleColorUtils.getScheduleColor(currentPosition));
                                                        schedulesWithSpecialEmptySchedule[currentPosition][n] = schedule;
                                                        n++;
                                                        j = j + 4;
                                                    }
                                                }
                                            }
                                        }
                                        currentColumnIndexInThisRow = currentColumnIndexInThisRow + 1;
                                    }
                                }
                            }
                        }
                        for (Schedule[] schedules : schedulesWithSpecialEmptySchedule) {
                            for (Schedule schedule : schedules) {
                                if (schedule != null && schedule != specialEmptySchedule) {
                                    schedulesWithoutSpecialEmptySchedule.add(schedule);
                                }
                            }
                        }
                        result.setResultData(schedulesWithoutSpecialEmptySchedule);
                        result.setResultType(ServiceResultEnum.SUCCESS);
                        return result;
                    } else if (httpResponse.getStatusLine().getStatusCode() == 302) {
                        throw new PasswordIncorrectException("账号密码错误");
                    }
                    throw new ServerErrorException("教务系统异常");
                }
                throw new ServerErrorException("教务系统异常");
            } else if (httpResponse.getStatusLine().getStatusCode() == 302) {
                throw new PasswordIncorrectException("账号密码错误");
            }
            throw new ServerErrorException("教务系统异常");
        } catch (ServerErrorException e) {
            log.error("查询课表异常：" + e);
            result.setResultType(ServiceResultEnum.SERVER_ERROR);
        } catch (PasswordIncorrectException e) {
            log.error("查询课表异常：" + e);
            result.setResultType(ServiceResultEnum.PASSWORD_INCORRECT);
        } catch (IOException e) {
            log.error("查询课表异常：" + e);
            result.setResultType(ServiceResultEnum.TIME_OUT);
        } catch (Exception e) {
            log.error("查询课表异常：" + e);
            result.setResultType(ServiceResultEnum.SERVER_ERROR);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
