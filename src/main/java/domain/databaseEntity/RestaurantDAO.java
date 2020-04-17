package domain.databaseEntity;

public class RestaurantDAO {
    private String restaurantName;
    private String restaurantId;
    private String logoUrl;
    private Double location_X;
    private Double location_Y;

    public RestaurantDAO(String restaurantId, String restaurantName,
                  String logoUrl, Double location_X, Double location_Y){
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.logoUrl = logoUrl;
        this.location_X = location_X;
        this.location_Y = location_Y;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public Double getLocation_X() {
        return location_X;
    }

    public Double getLocation_Y() {
        return location_Y;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLocation_X(Double location_X) {
        this.location_X = location_X;
    }

    public void setLocation_Y(Double location_Y) {
        this.location_Y = location_Y;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        return "RestaurantDAO{" +
                "restaurantName='" + restaurantName + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", location_X=" + location_X +
                ", location_Y=" + location_Y +
                '}';
    }
}
