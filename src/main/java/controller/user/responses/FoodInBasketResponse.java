package controller.user.responses;


import domain.entity.Menu;
import domain.entity.MenuParty;

public class FoodInBasketResponse {
    private String foodName;
    private int numFood;
    private Double price;

    public FoodInBasketResponse(Menu menu , Integer count){
        this.foodName = menu.getName();
        this.numFood = count;
        this.price = menu.getPrice();
    }

    public  FoodInBasketResponse(MenuParty menu, Integer count){
        this.foodName = menu.getName();
        this.numFood = count;
        this.price = menu.getNewPrice();
    }
    
    public void setNumFood(int numFood) {
        this.numFood = numFood;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public Double getPrice() {
        return price;
    }

    public int getNumFood() {
        return numFood;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}

