package controller.user.responses;

import domain.entity.DeliveryStatus;
import domain.entity.Menu;
import domain.entity.MenuParty;
import domain.entity.Order;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

public class SingleOrderResponese {
    private int id;
    private String restaurantName;
    private String restaurantId;
    private DeliveryStatus status;
    int totalDeliveryTime;
    private String state;
    private LocalTime timeGiveToDelivery;
    private String deliverPersonId;
    private ArrayList<FoodInBasketResponse> foods;
    private Double totalMoney;
    private int totalFood;


    public SingleOrderResponese(Order order){
        this.id = order.getId();
        this.restaurantName = order.getRestaurantName();
        this.restaurantId = order.getRestaurantId();
        this.status = order.getStatus();
        this.totalDeliveryTime = order.getTotalDeliveryTime();
        this.timeGiveToDelivery = order.getTimeGiveToDelivery();
        this.deliverPersonId = order.getDeliverPersonId();
        this.totalMoney = 0.0;
        this.totalFood = 0;
        this.foods = new ArrayList<FoodInBasketResponse>();
        for (Map.Entry<Menu,Integer> entry : order.getMenus().entrySet()){
            foods.add(new FoodInBasketResponse(entry.getKey() , entry.getValue()));
            this.totalFood += entry.getValue();
            this.totalMoney += entry.getKey().getPrice() * entry.getValue();
        }
        for (Map.Entry<MenuParty,Integer> entry : order.getPartyMenus().entrySet()){
            foods.add(new FoodInBasketResponse(entry.getKey() , entry.getValue()));
            this.totalFood += entry.getValue();
            this.totalMoney += entry.getKey().getNewPrice() * entry.getValue();
        }
        this.state = status.toString();

    }


    public void setFoods(ArrayList<FoodInBasketResponse> foods) {
        this.foods = foods;
    }

    public ArrayList<FoodInBasketResponse> getFoods() {
        return foods;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public int getTotalDeliveryTime() {
        return totalDeliveryTime;
    }

    public LocalTime getTimeGiveToDelivery() {
        return timeGiveToDelivery;
    }

    public String getDeliverPersonId() {
        return deliverPersonId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setDeliverPersonId(String deliverPersonId) {
        this.deliverPersonId = deliverPersonId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void setTimeGiveToDelivery(LocalTime timeGiveToDelivery) {
        this.timeGiveToDelivery = timeGiveToDelivery;
    }

    public void setTotalDeliveryTime(int totalDeliveryTime) {
        this.totalDeliveryTime = totalDeliveryTime;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public int getTotalFood() {
        return totalFood;
    }

    public void setTotalFood(int totalFood) {
        this.totalFood = totalFood;
    }

    public String getState() {
        return state;
    }
}
