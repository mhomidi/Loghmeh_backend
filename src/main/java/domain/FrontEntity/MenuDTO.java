package domain.FrontEntity;

public class MenuDTO {
    private String foodName;
    private Double popularity;
    private Double price;
    private String urlImage;
    private String description;
    private String restaurantId;


    public MenuDTO(String foodName, Double price , Double popularity , String description ,String urlImage ,String restaurantId){
        this.foodName = foodName;
        this.popularity = popularity;
        this.price = price;
        this.urlImage = urlImage;
        this.description = description;
        this.restaurantId = restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
