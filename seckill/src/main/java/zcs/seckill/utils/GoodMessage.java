package zcs.seckill.utils;

public class GoodMessage {
    private String phone;

    private int id;

    public GoodMessage(String phone, int id) {
        this.phone = phone;
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
