package lk.ijse.supermarket.dto;

public class StockManageDto {
    private String id;
    private String type;
    private int count;
    private double sellPrice;
    private double price;
    private String placeHolder;

    public StockManageDto(String id, int count, String placeHolder, double price) {
        this.id = id;
        this.count = count;
        this.placeHolder = placeHolder;
        this.price = price;
    }



    public StockManageDto(String id, String type, int count, double price, String placeHolder) {
        this.id = id;
        this.type = type;
        this.count = count;
        this.price = price;
        this.placeHolder = placeHolder;
    }

    public StockManageDto(String id, String type, int count, double sellPrice, double price, String placeHolder) {
        this.id = id;
        this.type = type;
        this.count = count;
        this.sellPrice = sellPrice;
        this.price = price;
        this.placeHolder = placeHolder;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public StockManageDto(String type, Double price){
        this.type = type;
        this.price = price;

    }

    public StockManageDto(String id, String type, int count, double price) {
        this.id = id;
        this.type = type;
        this.count = count;
        this.price = price;
    }


    public StockManageDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    @Override
    public String toString() {
        return "stockManageDto{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", placeHolder='" + placeHolder + '\'' +
                '}';
    }
}
