package com.submit.pojo;

import java.util.Date;

public class ApkInfo {
    private String userId;
    private String submitHash;
    private String packageName;
    private String appName;
    private String appVersion;
    private String submitTime;
    private String iconPathName;
    private String iconWebPath;
    private double size;
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIconWebPath() {
        return iconWebPath;
    }

    public void setIconWebPath(String iconWebPath) {
        this.iconWebPath = iconWebPath;
    }

    public String getSubmitHash() {
        return submitHash;
    }

    public void setSubmitHash(String submitHash) {
        this.submitHash = submitHash;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getIconPathName() {
        return iconPathName;
    }

    public void setIconPathName(String iconPathName) {
        this.iconPathName = iconPathName;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ApkInfo{" +
                "userId='" + userId + '\'' +
                ", submitHash='" + submitHash + '\'' +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", submitTime=" + submitTime +
                ", iconPathName='" + iconPathName + '\'' +
                ", iconWebPath='" + iconWebPath + '\'' +
                ", size=" + size +
                ", status='" + status + '\'' +
                '}';
    }
}