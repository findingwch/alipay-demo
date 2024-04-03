package com.example.alipaydemo.base.enums;


public enum ImageDirEnum implements BaseEnum {
    GOODS("0", "goods"),
    INDEX("1", "index");

    ImageDirEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    private String code;

    private String value;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }
}
