package com.gdeiassistant.gdeiassistant.Service.GradeQuery;

import com.gdeiassistant.gdeiassistant.Enum.Base.ServiceResultEnum;
import com.gdeiassistant.gdeiassistant.Exception.CommonException.PasswordIncorrectException;
import com.gdeiassistant.gdeiassistant.Exception.QueryException.NotAvailableConditionException;
import com.gdeiassistant.gdeiassistant.Exception.CommonException.ServerErrorException;
import com.gdeiassistant.gdeiassistant.Factory.HttpClientFactory;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.Grade;
import com.gdeiassistant.gdeiassistant.Pojo.GradeQuery.GradeQueryResult;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gdeiassistant on 2017/7/22.
 */

@Service
public class GradeQueryService {

    private String url;

    private int currentTerm;

    @Autowired
    private HttpClientFactory httpClientFactory;

    @Value("#{propertiesReader['education.system.url']}")
    public void setUrl(String url) {
        this.url = url;
    }

    public int getCurrentTerm() {
        return currentTerm;
    }

    @Value("#{propertiesReader['schedule.term']}")
    public void setCurrentTerm(int currentTerm) {
        this.currentTerm = currentTerm;
    }

    private Log log = LogFactory.getLog(GradeQueryService.class);

    private int timeout;

    @Value("#{propertiesReader['timeout.gradequery']}")
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * 查询学科成绩
     *
     * @param request
     * @param username
     * @param keycode
     * @param number
     * @param year
     * @return
     */
    public GradeQueryResult GradeQuery(HttpServletRequest request, String username, String keycode, String number, int year) {
        GradeQueryResult gradeQueryResult = new GradeQueryResult();
        CloseableHttpClient httpClient = null;
        try {
            httpClient = httpClientFactory.getHttpClient(request.getSession(), timeout);
            //快速连接教务系统
            HttpGet httpGet = new HttpGet(url + "cas_verify.aspx?i=" + username + "&k=" + keycode);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                httpGet = new HttpGet(url + "xs_main.aspx?xh=" + number);
                httpResponse = httpClient.execute(httpGet);
                Document document = Jsoup.parse(EntityUtils.toString(httpResponse.getEntity()));
                if (httpResponse.getStatusLine().getStatusCode() == 200 && document.title().equals("正方教务管理系统")) {
                    //成功进入学生个人主页,进行成绩查询操作
                    httpGet = new HttpGet(url + "xscj_gc.aspx?xh=" + number);
                    httpResponse = httpClient.execute(httpGet);
                    document = Jsoup.parse(EntityUtils.toString(httpResponse.getEntity()));
                    if (httpResponse.getStatusLine().getStatusCode() == 200 && document.title().equals("现代教学管理信息系统")) {
                        //获取所有可用的学年列表
                        List<String> yearList = new ArrayList<>();
                        Element yearSelect = document.getElementById("ddlXN");
                        Elements yearOptions = yearSelect.select("option");
                        for (Element element : yearOptions) {
                            if (!element.text().trim().isEmpty()) {
                                yearList.add(element.text());
                            }
                        }
                        Collections.reverse(yearList);
                        //检查需要查询的学年是否可用
                        if (yearList.isEmpty() || yearList.size() <= year || year < -1) {
                            throw new NotAvailableConditionException("当前学年暂不可查询");
                        }
                        if (year == -1) {
                            //若没有指定查询的学年，则进行默认学年查询
                            year = yearList.size() - 1;
                        }
                        gradeQueryResult.setQueryYear(year);
                        String ddlXN = yearList.get(year);
                        //获取ViewState的值
                        String viewState = document.select("input[name=__VIEWSTATE]").val();
                        //封装查询成绩发送的数据
                        BasicNameValuePair basicNameValuePair_1 = new BasicNameValuePair("__VIEWSTATE", viewState);
                        BasicNameValuePair basicNameValuePair_2 = new BasicNameValuePair("Button5", "%B0%B4%D1%A7%C4%EA%B2%E9%D1%AF");
                        BasicNameValuePair basicNameValuePair_3 = new BasicNameValuePair("ddlXN", ddlXN);
                        BasicNameValuePair basicNameValuePair_4 = new BasicNameValuePair("ddlXQ", "");
                        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
                        basicNameValuePairs.add(basicNameValuePair_1);
                        basicNameValuePairs.add(basicNameValuePair_2);
                        basicNameValuePairs.add(basicNameValuePair_3);
                        basicNameValuePairs.add(basicNameValuePair_4);
                        HttpPost httpPost = new HttpPost(url + "xscj_gc.aspx?xh=" + number);
                        httpPost.setEntity(new UrlEncodedFormEntity(basicNameValuePairs, StandardCharsets.UTF_8));
                        httpResponse = httpClient.execute(httpPost);
                        document = Jsoup.parse(EntityUtils.toString(httpResponse.getEntity()));
                        if (httpResponse.getStatusLine().getStatusCode() == 200 && document.title().equals("现代教学管理信息系统")) {
                            //获取存放成绩的表格
                            Element element = document.getElementsByClass("datelist").first();
                            //得到所有的行
                            Elements trs = element.getElementsByTag("tr");
                            List<Grade> firstTermGradeList = new ArrayList<>();
                            List<Grade> secondTermGradeList = new ArrayList<>();
                            double firstTermIGP = 0;
                            double secondTermIGP = 0;
                            double firstCreditSum = 0;
                            double secondCreditSum = 0;
                            double firstTermGPA = 0;
                            double secondTermGPA = 0;
                            for (int i = 1; i < trs.size(); i++) {
                                //首行是列名,从第二行开始
                                Element e = trs.get(i);
                                //得到行中的所有列
                                Elements tds = e.getElementsByTag("td");
                                String grade_year = tds.get(0).text();
                                String grade_term = tds.get(1).text();
                                String grade_id = tds.get(2).text();
                                String grade_name = tds.get(3).text();
                                String grade_type = tds.get(4).text();
                                String grade_credit = tds.get(6).text();
                                String grade_gpa = tds.get(7).text();
                                String grade_score = tds.get(8).text();
                                Grade grade = new Grade();
                                grade.setGrade_year(grade_year);
                                grade.setGrade_term(grade_term);
                                grade.setGrade_id(grade_id);
                                grade.setGrade_name(grade_name);
                                grade.setGrade_type(grade_type);
                                grade.setGrade_credit(grade_credit);
                                grade.setGrade_gpa(grade_gpa);
                                grade.setGrade_score(grade_score);
                                if (grade_term.equals("1")) {
                                    //计算第一学期IGP
                                    firstTermIGP = firstTermIGP + (Double.parseDouble(grade_credit) * Double.parseDouble(grade_gpa));
                                    firstCreditSum = firstCreditSum + Double.parseDouble(grade_credit);
                                    firstTermGradeList.add(grade);
                                } else if (grade_term.equals("2")) {
                                    //计算第二学期IGP
                                    secondTermIGP = secondTermIGP + (Double.parseDouble(grade_credit) * Double.parseDouble(grade_gpa));
                                    secondCreditSum = secondCreditSum + Double.parseDouble(grade_credit);
                                    secondTermGradeList.add(grade);
                                }
                            }
                            //计算第一学期GPA
                            if (firstCreditSum != 0) {
                                firstTermGPA = firstTermIGP / firstCreditSum;
                            }
                            //计算第二学期GPA
                            if (secondCreditSum != 0) {
                                secondTermGPA = secondTermIGP / secondCreditSum;
                            }
                            //保留两位小数
                            DecimalFormat decimalFormat = new DecimalFormat("#.00");
                            firstTermIGP = Double.parseDouble(decimalFormat.format(firstTermIGP));
                            secondTermIGP = Double.parseDouble(decimalFormat.format(secondTermIGP));
                            firstTermGPA = Double.parseDouble(decimalFormat.format(firstTermGPA));
                            secondTermGPA = Double.parseDouble(decimalFormat.format(secondTermGPA));
                            gradeQueryResult.setFirstTermIGP(firstTermIGP);
                            gradeQueryResult.setFirstTermGPA(firstTermGPA);
                            gradeQueryResult.setSecondTermIGP(secondTermIGP);
                            gradeQueryResult.setSecondTermGPA(secondTermGPA);
                            gradeQueryResult.setFirstTermGradeList(firstTermGradeList);
                            gradeQueryResult.setSecondTermGradeList(secondTermGradeList);
                            gradeQueryResult.setGradeServiceResultEnum(ServiceResultEnum.SUCCESS);
                            return gradeQueryResult;
                        }
                        throw new ServerErrorException("教务系统异常");
                    }
                    throw new ServerErrorException("教务系统异常");
                }
                throw new ServerErrorException("教务系统异常");
            } else if (httpResponse.getStatusLine().getStatusCode() == 302) {
                throw new PasswordIncorrectException("账号密码错误");
            }
            throw new ServerErrorException("教务系统异常");
        } catch (PasswordIncorrectException e) {
            log.error("课程成绩查询异常：" + e);
            gradeQueryResult.setGradeServiceResultEnum(ServiceResultEnum.PASSWORD_INCORRECT);
        } catch (NotAvailableConditionException e) {
            log.error("课程成绩查询异常：" + e);
            gradeQueryResult.setGradeServiceResultEnum(ServiceResultEnum.ERROR_CONDITION);
        } catch (ServerErrorException e) {
            log.error("课程成绩查询异常：" + e);
            gradeQueryResult.setGradeServiceResultEnum(ServiceResultEnum.SERVER_ERROR);
        } catch (IOException e) {
            log.error("课程成绩查询异常：" + e);
            gradeQueryResult.setGradeServiceResultEnum(ServiceResultEnum.TIME_OUT);
        } catch (Exception e) {
            log.error("课程成绩查询异常：" + e);
            gradeQueryResult.setGradeServiceResultEnum(ServiceResultEnum.SERVER_ERROR);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return gradeQueryResult;
    }

}