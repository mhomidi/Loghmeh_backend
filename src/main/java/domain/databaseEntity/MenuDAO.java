package domain.databaseEntity;

public class MenuDAO {
    private String restaurantId;
    private String foodName;
    private String description;
    private Double popularity;
    private Double price;
    private String urlImage;

    public MenuDAO(String restaurantId, String foodName, String description,
                   Double popularity, Double price, String urlImage){
        this.restaurantId = restaurantId;
        this.foodName = foodName;
        this.description = description;
        this.popularity = popularity;
        this.price = price;
        this.urlImage = urlImage;
    }


    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public String toString() {
        return "MenuDAO{" +
                "restaurantId='" + restaurantId + '\'' +
                ", foodName='" + foodName + '\'' +
                ", description='" + description + '\'' +
                ", popularity=" + popularity +
                ", price=" + price +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
