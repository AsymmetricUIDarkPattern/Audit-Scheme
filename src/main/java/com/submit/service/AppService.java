package com.submit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.submit.dao.*;
import com.submit.pojo.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AppService {
    @Autowired(required = false)
    apkMapper apkMapper;
    @Value("${file.upload.apk_info_url}")
    private String APK_PATH;

    public void addApkInfo(ApkInfo apkInfo, String userId, String iconFileName, String submitHash) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String dateTime = df.format(new Date());
        apkMapper.addApkInfo(apkInfo, userId, iconFileName, submitHash, dateTime);
    }

    public List<ApkInfo> getApkFile(String userId) {
        return apkMapper.getApkFile(userId);
    }
    public List<ApkInfo> getAllApkFile() {
        return apkMapper.getAllApkFile();
    }

    public List<AuditRet> getAuditRet(String submitHash) {
        return apkMapper.getAuditRet(submitHash);
    }

    public List<ApkInfo> deleteIfExist(String packageName) {
        List<ApkInfo> apkRet = apkMapper.getapkfileByPackageName(packageName);
        // 这里只会有一个结果  或者结果为空
        if (apkRet == null || apkRet.size()==0) {
            return apkRet;
        }
        apkMapper.deleteApk(packageName);
        ApkInfo apkInfo = apkRet.get(0);
        // 删除文件
        try {
            File apkFile = new File(APK_PATH + File.separator + "app" + File.separator + apkInfo.getSubmitHash() + ".apk");
            File iconFile = new File(APK_PATH + File.separator + "icon" + File.separator + apkInfo.getSubmitHash() + ".png");
            if (!apkFile.delete()) {
                System.out.println("文件删除失败！");
            }
            if (!iconFile.delete()) {
                System.out.println("文件删除失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apkRet;
    }

    public boolean change2underAudit(String submitHash) {
        System.out.println("change2underAudit submitHash: " + submitHash);
        return apkMapper.change2underAudit(submitHash);
    }

    public List<Map<String, Object>> getAppAuditType(String userid) {
        return apkMapper.getAppAuditType(userid);
    }

    public List<Map<String, Object>> getAppAuditResult(String submitHash) {
        return apkMapper.getAppAuditResult(submitHash);
    }
}
