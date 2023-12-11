package com.lhn.gps.enums;

/**
 * 业务类型
 */
public enum Bussiness {
    NORMAL("normal", "主动发送", ""),
    FILE_UPLOAD("file_upload", "上传邮件", "上传了文件:%s");
    String code;
    String name;
    String event;

    Bussiness(String code, String name, String event) {
        this.code = code;
        this.name = name;
        this.event = event;
    }

    /**
     * 根据凑得获取Business
     * @param code
     * @return
     */
    public static Bussiness getBussiness(String code){
        for (Bussiness bussiness: Bussiness.values()){
            if(bussiness.getCode().equals(code)){
                return bussiness;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
