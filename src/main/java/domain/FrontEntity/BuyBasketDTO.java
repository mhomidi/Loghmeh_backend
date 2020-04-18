package domain.FrontEntity;

import java.util.ArrayList;


public class BuyBasketDTO {
    private String username;
    private int orderId;
    ArrayList<FoodInBasketDTO> foods;
    private int totalFood;
    private Double totalMoney;


    public BuyBasketDTO(String username){
        this.username = username;
        this.totalFood = 0;
        this.totalMoney = 0.0;
    }



    private void calcTotalMoneyAndTotalFood(){
        for (FoodInBasketDTO foodInBasketDTO:foods){
            this.totalFood += foodInBasketDTO.getCountFood();
            this.totalMoney += foodInBasketDTO.getCountFood() * foodInBasketDTO.getFoodPrice();
        }
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
        this.calcTotalMoneyAndTotalFood();
    }

    public void setTotalFood(int totalFood) {
        this.totalFood = totalFood;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
