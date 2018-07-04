package com.gdeiassistant.gdeiassistant.Pojo.GradeQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.Grade;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
@JsonIgnoreProperties
public class GradeQueryJsonResult {

    private Integer queryYear;

    private Integer currentTerm;

    private Double firstTermGPA;

    private Double firstTermIGP;

    private Double secondTermGPA;

    private Double secondTermIGP;

    private List<Grade> firstTermGradeList;

    private List<Grade> secondTermGradeList;

    private boolean success;

    private String errorMessage;

    public Integer getQueryYear() {
        return queryYear;
    }

    public void setQueryYear(Integer queryYear) {
        this.queryYear = queryYear;
    }

    public Integer getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(Integer currentTerm) {
        this.currentTerm = currentTerm;
    }

    public Double getFirstTermGPA() {
        return firstTermGPA;
    }

    public void setFirstTermGPA(Double firstTermGPA) {
        this.firstTermGPA = firstTermGPA;
    }

    public Double getFirstTermIGP() {
        return firstTermIGP;
    }

    public void setFirstTermIGP(Double firstTermIGP) {
        this.firstTermIGP = firstTermIGP;
    }

    public Double getSecondTermGPA() {
        return secondTermGPA;
    }

    public void setSecondTermGPA(Double secondTermGPA) {
        this.secondTermGPA = secondTermGPA;
    }

    public Double getSecondTermIGP() {
        return secondTermIGP;
    }

    public void setSecondTermIGP(Double secondTermIGP) {
        this.secondTermIGP = secondTermIGP;
    }

    public List<Grade> getFirstTermGradeList() {
        return firstTermGradeList;
    }

    public void setFirstTermGradeList(List<Grade> firstTermGradeList) {
        this.firstTermGradeList = firstTermGradeList;
    }

    public List<Grade> getSecondTermGradeList() {
        return secondTermGradeList;
    }

    public void setSecondTermGradeList(List<Grade> secondTermGradeList) {
        this.secondTermGradeList = secondTermGradeList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}