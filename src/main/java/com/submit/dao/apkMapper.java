package com.submit.dao;

import com.submit.pojo.ApkInfo;
import com.submit.pojo.AuditRet;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface apkMapper {
    //插入job，具体的那次作业
//    @Insert("insert into job (teachclassid,no,title,duedate,type,note,creteTime)" +
//            "values(#{teachclassid},#{no},#{title},#{duedate},#{type},#{note},#{creteTime})")

    @Insert("insert into app_info (user_id, submit_hash, package_name, app_name, app_version, submit_time, size, icon_file_name) " +
            "values(#{userId}, #{submitHash}, #{apkInfo.packageName},#{apkInfo.appName},#{apkInfo.appVersion}, " +
            "#{dateTime}, #{apkInfo.size}, #{iconFileName})")
    void addApkInfo(ApkInfo apkInfo, String userId, String iconFileName, String submitHash, String dateTime);

    List<ApkInfo> getApkFile(String userId);

    List<ApkInfo> getAllApkFile();

    List<AuditRet> getAuditRet(String submitHash);

    List<ApkInfo> getapkfileByPackageName(String packageName);

    @Delete("delete from app_info where package_name=#{packageName}")
    void deleteApk(String packageName);



    @Update("update app_info set status='under_audit' where submit_hash=#{submitHash}")
    boolean change2underAudit(String submitHash);

    @Select("SELECT status, count(status) as num FROM app_info GROUP BY status")
    List<Map<String, Object>> getAppAuditType(String userid);

    @Select("SELECT type_name as type, count(type) as num FROM audit_info as a, UI_type as b " +
            "where submit_hash=#{submitHash} and a.type=b.id GROUP BY type ")
    List<Map<String, Object>> getAppAuditResult(String submitHash);
}
