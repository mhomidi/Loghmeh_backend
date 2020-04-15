package controller.user.responses;


import domain.entity.Menu;
import domain.entity.MenuParty;
import domain.entity.Order;
import domain.entity.User;

import java.util.ArrayList;
import java.util.Map;

public class BuyBasketResponse {
    private String username ;
    private int id;
    private String restaurantName;
    private String restaurantId;
    private int totalFood;
    ArrayList<FoodInBasketResponse> foods;
    private Double totalMoney;


    public BuyBasketResponse(User user) {
        Order order = user.getCurrentOrder();
        this.username = user.getUsername();
        this.totalMoney = 0.0;
        this.totalFood = 0;
        this.id = order.getId();
        this.restaurantName = order.getRestaurantName();
        this.restaurantId = order.getRestaurantId();
        this.foods = new ArrayList<FoodInBasketResponse>();
        for (Map.Entry<Menu, Integer> entry : order.getMenus().entrySet()) {
            foods.add(new FoodInBasketResponse(entry.getKey(), entry.getValue()));
            this.totalFood += entry.getValue();
            this.totalMoney += entry.getKey().getPrice() * entry.getValue();
        }
        for (Map.Entry<MenuParty, Integer> entry : order.getPartyMenus().entrySet()) {
            foods.add(new FoodInBasketResponse(entry.getKey(), entry.getValue()));
            this.totalFood += entry.getValue();
            this.totalMoney += entry.getKey().getNewPrice() * entry.getValue();
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }




    public String getRestaurantName(){
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantId(){
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public ArrayList<FoodInBasketResponse> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<FoodInBasketResponse> foods) {
        this.foods = foods;
    }

    public int getTotalFood() {
        return totalFood;
    }

    public void setTotalFood(int totalFood) {
        this.totalFood = totalFood;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
