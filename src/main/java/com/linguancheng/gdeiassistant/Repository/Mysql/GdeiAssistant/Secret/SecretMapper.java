package com.gdeiassistant.gdeiassistant.Repository.Mysql.GdeiAssistant.Secret;

import com.gdeiassistant.gdeiassistant.Pojo.Entity.Secret;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.SecretComment;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.SecretContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SecretMapper {

    public Secret selectSecretByID(int id) throws Exception;

    public List<Secret> selectSecretByUsername(String username) throws Exception;

    public List<Secret> selectSecret(@Param("start") int start, @Param("size") int size) throws Exception;

    public void insertSecret(SecretContent secretContent) throws Exception;

    public int selectSecretCommentCount(int id) throws Exception;

    public List<SecretComment> selectSecretComment(int id) throws Exception;

    public void insertSecretComment(SecretComment secretComment) throws Exception;

    public int selectSecretLikeCount(int id) throws Exception;

    public int selectSecretLike(@Param("id") int id, @Param("username") String username) throws Exception;

    public void insertSecretLike(@Param("id") int id, @Param("username") String username) throws Exception;

    public void deleteSecretLike(@Param("id") int id, @Param("username") String username) throws Exception;

}
