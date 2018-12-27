package cn.no7player.response;

public class ResponseBean {
    int code;
    String message;
    Object data;

    public ResponseBean(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseBean(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseBean(Object data) {
        this.code = 0;
        this.message = "";
        this.data = data;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
