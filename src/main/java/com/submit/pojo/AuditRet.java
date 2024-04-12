package com.submit.pojo;

public class AuditRet {
    private String userId;
    private String submitHash;
    private String type;
    private String activityName;
    private String screenshotPath;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubmitHash() {
        return submitHash;
    }

    public void setSubmitHash(String submitHash) {
        this.submitHash = submitHash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

    @Override
    public String toString() {
        return "AuditRet{" +
                "userId='" + userId + '\'' +
                ", submitHash='" + submitHash + '\'' +
                ", type=" + type +
                ", activityName='" + activityName + '\'' +
                ", screenshotPath='" + screenshotPath + '\'' +
                '}';
    }
}
