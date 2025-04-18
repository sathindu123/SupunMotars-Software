package lk.ijse.supermarket.dto;

public class CustomerVehicelDto {
    private String vehicelId;
    private int ordId;



    public CustomerVehicelDto(int ordId, String vehicelId) {
        this.ordId = ordId;
        this.vehicelId = vehicelId;

    }

    public CustomerVehicelDto(String vehicelId, int ordId) {
        this.vehicelId = vehicelId;
        this.ordId = ordId;
    }

    @Override
    public String toString() {
        return "CustomerVehicelDto{" +
                "ordId=" + ordId +
                ", vehicelId='" + vehicelId + '\'' +
                '}';
    }

    public int getOrdId() {
        return ordId;
    }

    public void setOrdId(int ordId) {
        this.ordId = ordId;
    }


    public String getVehicelId() {
        return vehicelId;
    }

    public void setVehicelId(String vehicelId) {
        this.vehicelId = vehicelId;
    }
}
