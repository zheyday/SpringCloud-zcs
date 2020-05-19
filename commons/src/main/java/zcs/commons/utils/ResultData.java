package zcs.commons.utils;


public class ResultData {
    private ResponseState state;
    //状态码
    private int code;
    //信息
    private String msg;
    //数据
    private Object data;

    public ResultData(ResponseState state) {
        this.code =state.getCode() ;
        this.msg = state.getMsg();
    }

    public ResultData(ResponseState state, Object data) {
        this.code =state.getCode() ;
        this.msg = state.getMsg();
        this.data = data;
    }

    public void setState(ResponseState state) {
        this.state = state;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
