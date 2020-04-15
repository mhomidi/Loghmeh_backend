package controller.user.requests;

public class AddToCartRequest {
    private String foodName;
    private String numFood;
    private String restaurantId;


    public String getFoodName() {
        return foodName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getNumFood() {
        return numFood;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setNumFood(String numFood) {
        this.numFood = numFood;
    }
}
