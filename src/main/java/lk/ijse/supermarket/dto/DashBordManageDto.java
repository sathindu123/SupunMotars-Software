package lk.ijse.supermarket.dto;

public class DashBordManageDto {
    private String productId;
    private int count;
    private String date;
    private double discount;
    private double price;
    private double onePrice;

    private int orderId;
    private String type;
    private String vehicleNb;

    private int nb ;




    public DashBordManageDto(String productId, int count, String date, double onePrice , double discount, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
        this.date = date;
        this.onePrice = onePrice;
        this.discount = discount;
        this.price = price;
    }


    public DashBordManageDto(int orderId, String productId, int count, String date, double onePrice, double discount, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
        this.date = date;
        this.onePrice = onePrice;
        this.discount = discount;
        this.price = price;
    }

    public DashBordManageDto(String productId, int count, String date, double discount, double price) {
        this.productId = productId;
        this.count = count;
        this.date = date;
        this.discount = discount;
        this.price = price;
    }

//    public DashBordManageDto(int nb,String productId, int count, String date, double discount, double price) {
//        this.nb = nb;
//        this.productId = productId;
//        this.count = count;
//        this.date = date;
//        this.discount = discount;
//        this.price = price;
//    }

    public DashBordManageDto(int orderId ,String productId, int count, String date, double discount, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
        this.date = date;
        this.discount = discount;
        this.price = price;
    }


    public DashBordManageDto(String type, int count,double onePrice, double discount, double price) {
        this.type = type;
        this.count = count;
        this.onePrice = onePrice;
        this.discount = discount;
        this.price = price;
    }

    public DashBordManageDto() {

    }

    @Override
    public String toString() {
        return "DashBordManageDto{" +
                ", orderId='" + orderId + '\'' +
                "productId='" + productId + '\'' +
                ", count=" + count +
                ", date='" + date + '\'' +
                ", onePrice=" + onePrice +
                ", discount=" + discount +
                ", price=" + price +
                '}';
    }

    public String getVehicleNb() {
        return vehicleNb;
    }

    public void setVehicleNb(String vehicleNb) {
        this.vehicleNb = vehicleNb;
    }

    public double getOnePrice() {
        return onePrice;
    }

    public void setOnePrice(double onePrice) {
        this.onePrice = onePrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
