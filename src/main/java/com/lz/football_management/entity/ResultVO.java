package com.lz.football_management.entity;

/**
 * 响应数据实体类
 */
public class ResultVO {
    private int code;       //代码，成功200， 400,500失败
    private String msg;     //消息
    private Object data;    //数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
