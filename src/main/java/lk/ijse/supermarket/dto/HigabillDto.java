package lk.ijse.supermarket.dto;

public class HigabillDto {
    private int orderId;
    private String vehicleId;
    private String customerTel;
    private String date;
    private double totalPrice;
    private double higa;

    public HigabillDto(int orderId, String vehicleId, String customerTel, String date, double totalPrice, double higa) {
        this.orderId = orderId;
        this.vehicleId = vehicleId;
        this.customerTel = customerTel;
        this.date = date;
        this.totalPrice = totalPrice;
        this.higa = higa;
    }

    @Override
    public String toString() {
        return "HigabillDto{" +
                "orderId=" + orderId +
                ", vehicleId='" + vehicleId + '\'' +
                ", customerTel='" + customerTel + '\'' +
                ", date='" + date + '\'' +
                ", totalPrice=" + totalPrice +
                ", higa=" + higa +
                '}';
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getHiga() {
        return higa;
    }

    public void setHiga(double higa) {
        this.higa = higa;
    }
}
