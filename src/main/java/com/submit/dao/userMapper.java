package com.submit.dao;

import com.submit.pojo.user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface userMapper {
    int deleteByPrimaryKey(String userid);

    int insert(user record);

    int insertSelective(user record);

    user selectByPrimaryKey(String userid);

    @Select("select * from user")
    List<user> getall();

    int updateByPrimaryKeySelective(user record);

    int updateByPrimaryKey(user record);

    //修改密码
    @Update("update user set password=#{password} where user_id=#{userid}")
    boolean updatepassword(@Param("userid") String userid, @Param("password") String password);

    @Select("SELECT b.userid  userid ,a.`name`,a.pinyin,a.password,b.no,b.ID from user a,userclass b " +
            "WHERE a.userid=b.userid " +
            "and b.classID=#{classid} ")
    List<Map> getuserbyclassid(int classid);

    @Select("select * from user where userid like CONCAT('%',#{userid},'%') and name like CONCAT('%',#{name},'%')")
    List<user> getalluser(@Param("userid") String userid, @Param("name") String name);
}/**/