package lk.ijse.supermarket.dto;

public class QtyDto {
    private int qtId;
    private String type;
    private int count;
    private double price;

    public QtyDto(int qtId, String type, int count, double price) {
        this.qtId = qtId;
        this.type = type;
        this.count = count;
        this.price = price;
    }
    public QtyDto( String type, int count, double price) {
        this.type = type;
        this.count = count;
        this.price = price;
    }


    @Override
    public String toString() {
        return "QtyDto{" +
                "qtId=" + qtId +
                ", type='" + type + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }

    public int getQtId() {
        return qtId;
    }

    public void setQtId(int qtId) {
        this.qtId = qtId;
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
}
