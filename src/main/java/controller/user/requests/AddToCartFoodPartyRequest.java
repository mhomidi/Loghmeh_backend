package controller.user.requests;

public class AddToCartFoodPartyRequest {
    private String restaurantName;
    private String RestaurantId;
    private String foodName;
    private Double newPrice;
    private String countFood;


    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public String getCountFood() {
        return countFood;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setCountFood(String countFood) {
        this.countFood = countFood;
    }

    public void setRestaurantId(String getRestaurantId) {
        this.RestaurantId = getRestaurantId;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }
}
