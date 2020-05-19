package zcs.commons.utils;


public enum ResponseState {
    OK(200,"success"),
    FAIL(404,"fail"),
    END(701,"结束"),
    EXIST(702,"用户已存在"),
    NOEXIST(703,"不存在"),
    DUPLICATE(704,"用户名重复");

    private int code;
    private String msg;

    ResponseState(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
