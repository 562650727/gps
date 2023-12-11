package com.lhn.gps.enums;

/**
 * 邮件发送状态
 */
public enum EmailLogState{
    SUCCESS("1"),
    FAIL("0");
    private String code;

    EmailLogState(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
