package domain.databaseEntity;


public class DeliveryDAO {
    private String id;
    private Double velocity;
    private Double location_X;
    private Double location_Y;

    public DeliveryDAO(String id, Double velocity, Double location_X, Double location_Y) {
        this.id = id;
        this.velocity = velocity;
        this.location_X = location_X;
        this.location_Y = location_Y;
    }

    public void setLocation_Y(Double location_Y) {
        this.location_Y = location_Y;
    }

    public void setLocation_X(Double location_X) {
        this.location_X = location_X;
    }

    public Double getLocation_Y() {
        return location_Y;
    }

    public Double getLocation_X() {
        return location_X;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DeliveryDAO{" +
                "id='" + id + '\'' +
                ", velocity=" + velocity +
                ", location_X=" + location_X +
                ", location_Y=" + location_Y +
                '}';
    }
}
