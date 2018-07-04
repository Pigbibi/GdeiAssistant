package com.gdeiassistant.gdeiassistant.Repository.Mysql.GdeiAssistant.Privacy;

import com.gdeiassistant.gdeiassistant.Pojo.Entity.Privacy;
import org.apache.ibatis.annotations.Param;

public interface PrivacyMapper {

    public Privacy selectPrivacy(String username) throws Exception;

    public void initPrivacy(String username) throws Exception;

    public void updateGender(@Param("gender") boolean gender, @Param("username") String username) throws Exception;

    public void updateGenderOrientation(@Param("genderOrientation") boolean genderOrientation
            , @Param("username") String username) throws Exception;

    public void updateRegion(@Param("region") boolean region
            , @Param("username") String username) throws Exception;

    public void updateIntroduction(@Param("introduction") boolean introduction
            , @Param("username") String username) throws Exception;
}