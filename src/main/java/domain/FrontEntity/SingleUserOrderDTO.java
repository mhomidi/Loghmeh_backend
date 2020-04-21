package domain.FrontEntity;


import domain.entity.DeliveryStatus;



import java.time.LocalTime;
import java.util.ArrayList;

public class SingleUserOrderDTO {
    private int orderId;
    private String restaurantName;
    private String restaurantId;
    private int status;
    private String state;
    int totalDeliveryTime;
    private String deliverPersonId;
    private Double totalMoney;
    private int totalFood;
    private ArrayList<FoodInBasketDTO> foods;


//    private LocalTime timeGiveToDelivery;




    public SingleUserOrderDTO(int orderId,String restaurantId, String restaurantName,
                              int status, int totalDeliveryTime, String deliverPersonId){
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.status = status;
        this.totalDeliveryTime = totalDeliveryTime;
        this.deliverPersonId = deliverPersonId;
        this.foods = new ArrayList<>();
        if(this.status==1){
            this.state = DeliveryStatus.NOT_FINALIZING.toString();
        }
        else if(this.status==2){
            this.state = DeliveryStatus.FINDING_DELIVERY.toString();
        }
        else if(this.status==3){
            this.state = DeliveryStatus.DELIVERING.toString();
        }
        else if(this.status==4){
            this.state = DeliveryStatus.DONE.toString();
        }

    }

    public SingleUserOrderDTO(int orderId,String restaurantId, String restaurantName,
                              int status, int totalDeliveryTime, String deliverPersonId,
                              ArrayList<FoodInBasketDTO> foods, int num_foods, Double totalMoney){
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.status = status;
        this.totalDeliveryTime = totalDeliveryTime;
        this.deliverPersonId = deliverPersonId;
        this.foods = new ArrayList<FoodInBasketDTO>();
        this.foods = foods;
        this.totalFood = num_foods;
        this.totalMoney = totalMoney;
        if(this.status==1){
            this.state = DeliveryStatus.NOT_FINALIZING.toString();
        }
        else if(this.status==2){
            this.state = DeliveryStatus.FINDING_DELIVERY.toString();
        }
        else if(this.status==3){
            this.state = DeliveryStatus.DELIVERING.toString();
        }
        else if(this.status==4){
            this.state = DeliveryStatus.DONE.toString();
        }


    }

    public void setTotalDeliveryTime(int totalDeliveryTime) {
        this.totalDeliveryTime = totalDeliveryTime;
    }

    public void setDeliverPersonId(String deliverPersonId) {
        this.deliverPersonId = deliverPersonId;
    }

    public String getDeliverPersonId() {
        return deliverPersonId;
    }

    public int getTotalDeliveryTime() {
        return totalDeliveryTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

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

    public int getStatus() {
        return status;
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
        this.calcTotalMoneyAndTotalFood();
    }

    public void setTotalFood(int totalFood) {
        this.totalFood = totalFood;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void calcTotalMoneyAndTotalFood(){
        for (FoodInBasketDTO foodInBasketDTO:foods){
            this.totalFood += foodInBasketDTO.getCountFood();
            this.totalMoney += foodInBasketDTO.getCountFood() * foodInBasketDTO.getFoodPrice();
        }
    }


    @Override
    public String toString() {
        return "SingleUserOrderDTO{" +
                "orderId=" + orderId +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", status=" + status +
                ", totalDeliveryTime=" + totalDeliveryTime +
                ", deliverPersonId='" + deliverPersonId + '\'' +
                ", totalMoney=" + totalMoney +
                ", totalFood=" + totalFood +
                ", foods=" + foods +
                '}';
    }
}
