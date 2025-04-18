package lk.ijse.supermarket.dto;

public class orderDetails {
    private String vehicleNb;
    private String milagage;
    private String nextmilagage;
    private String tel;
    private int orderId;

    public orderDetails(String vehicleNb, String milagage, String nextmilagage, String tel, int orderId) {
        this.vehicleNb = vehicleNb;
        this.milagage = milagage;
        this.nextmilagage = nextmilagage;
        this.tel = tel;
        this.orderId = orderId;
    }

    public String getVehicleNb() {
        return vehicleNb;
    }

    public void setVehicleNb(String vehicleNb) {
        this.vehicleNb = vehicleNb;
    }

    public String getMilagage() {
        return milagage;
    }

    public void setMilagage(String milagage) {
        this.milagage = milagage;
    }

    public String getNextmilagage() {
        return nextmilagage;
    }

    public void setNextmilagage(String nextmilagage) {
        this.nextmilagage = nextmilagage;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "orderDetails{" +
                "vehicleNb='" + vehicleNb + '\'' +
                ", milagage='" + milagage + '\'' +
                ", nextmilagage='" + nextmilagage + '\'' +
                ", tel='" + tel + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
