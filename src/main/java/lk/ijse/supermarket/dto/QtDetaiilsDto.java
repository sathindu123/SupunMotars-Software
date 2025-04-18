package lk.ijse.supermarket.dto;

public class QtDetaiilsDto {
    private int qtId;
    private String VehicleId;
    private String name;
    private String date;
    private String tel;

    public QtDetaiilsDto(int qtId, String vehicleId, String name, String date, String tel) {
        this.qtId = qtId;
        VehicleId = vehicleId;
        this.name = name;
        this.date = date;
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "QtDetaiilsDto{" +
                "qtId=" + qtId +
                ", VehicleId='" + VehicleId + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }

    public int getQtId() {
        return qtId;
    }

    public void setQtId(int qtId) {
        this.qtId = qtId;
    }

    public String getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(String vehicleId) {
        VehicleId = vehicleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
