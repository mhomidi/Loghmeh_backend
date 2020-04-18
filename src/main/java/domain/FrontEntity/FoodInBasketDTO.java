package domain.FrontEntity;


public class FoodInBasketDTO {
    private int menuId;
    private String foodName;
    private int countFood;
    private Double foodPrice;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public void setCountFood(int countFood) {
        this.countFood = countFood;
    }

    public int getCountFood() {
        return countFood;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getMenuId() {
        return menuId;
    }
}

