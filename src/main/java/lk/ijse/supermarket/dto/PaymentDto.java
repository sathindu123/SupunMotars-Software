package lk.ijse.supermarket.dto;

public class PaymentDto {
    private int orderId;
    private String productId;
    private int count;
    private String date;
    private double discount;
    private double price;
    private double onePrice;

    private String type;

    public PaymentDto(String productId, String type, int count, double price) {
        this.productId = productId;
        this.type = type;
        this.count = count;
        this.price = price;
    }

    public PaymentDto(int orderId, String productId, int count, String date, double discount, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
        this.date = date;
        this.discount = discount;
        this.price = price;
    }

    public PaymentDto(int orderId, String productId, int count, String date, double onePrice, double discount, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.count = count;
        this.date = date;
        this.onePrice = onePrice;
        this.discount = discount;
        this.price = price;
    }

    public double getOnePrice() {
        return onePrice;
    }

    public void setOnePrice(double onePrice) {
        this.onePrice = onePrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    @Override
    public String toString() {
        return "PaymentDto{" +
                "orderId=" + orderId +
                ", productId='" + productId + '\'' +
                ", count=" + count +
                ", date='" + date + '\'' +
                ", discount=" + discount +
                ", price=" + price +
                '}';
    }
}
