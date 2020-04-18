package domain.FrontEntity;


import domain.entity.DeliveryStatus;



import java.time.LocalTime;
import java.util.ArrayList;

public class SingleUserOrderDTO {
    private int orderId;
    private String restaurantName;
    private String restaurantId;
    private DeliveryStatus status;
    private String state;
    int totalDeliveryTime;
    private LocalTime timeGiveToDelivery;
    private String deliverPersonId;
    private ArrayList<FoodInBasketDTO> foods;
    private Double totalMoney;
    private int totalFood;


    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public void setTotalFood(int totalFood) {
        this.totalFood = totalFood;
    }

    public void setFoods(ArrayList<FoodInBasketDTO> foods) {
        this.foods = foods;
    }

    public int getTotalFood() {
        return totalFood;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public ArrayList<FoodInBasketDTO> getFoods() {
        return foods;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public DeliveryStatus getStatus() {
        return status;
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

    public void setDeliverPersonId(String deliverPersonId) {
        this.deliverPersonId = deliverPersonId;
    }

    public void setTimeGiveToDelivery(LocalTime timeGiveToDelivery) {
        this.timeGiveToDelivery = timeGiveToDelivery;
    }

    public void setTotalDeliveryTime(int totalDeliveryTime) {
        this.totalDeliveryTime = totalDeliveryTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
