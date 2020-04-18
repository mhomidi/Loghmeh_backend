package domain.FrontEntity;

import java.util.ArrayList;


public class BuyBasketDTO {
    private String username;
    private int orderId;
    private String restaurantName;
    private String restaurantId;
    private int totalFood;
    private Double totalMoney;
    ArrayList<FoodInBasketDTO> foods;


    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<FoodInBasketDTO> getFoods() {
        return foods;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public int getTotalFood() {
        return totalFood;
    }

    public void setFoods(ArrayList<FoodInBasketDTO> foods) {
        this.foods = foods;
    }

    public void setTotalFood(int totalFood) {
        this.totalFood = totalFood;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
