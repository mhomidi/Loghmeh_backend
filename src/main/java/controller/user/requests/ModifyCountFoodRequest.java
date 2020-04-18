package controller.user.requests;

public class ModifyCountFoodRequest {
    private String menuId;
    private String foodName;
    private String currCount;



    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getCurrCount() {
        return currCount;
    }

    public void setCurrCount(String currCount) {
        this.currCount = currCount;
    }

    public String getMenuId() {
        return menuId;
    }
}
