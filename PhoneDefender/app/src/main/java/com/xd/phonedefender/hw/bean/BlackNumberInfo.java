package com.xd.phonedefender.hw.bean;

/**
 * Created by hhhhwei on 16/1/28.
 */
public class BlackNumberInfo {

    private String number;
    private String mode;

    public BlackNumberInfo() {

    }

    public BlackNumberInfo(String number, String mode) {
        this.number = number;
        this.mode = mode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
