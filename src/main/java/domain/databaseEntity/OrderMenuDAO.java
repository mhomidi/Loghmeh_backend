package domain.databaseEntity;

public class OrderMenuDAO {
    private int orderId;
    private int menuId;
    private String foodName;
    private Double price;
    private int count;
    private boolean isFoodParty;


    public OrderMenuDAO(int orderId , int menuId , String foodName , Double price, int count , boolean isFoodParty){
        this.orderId = orderId;
        this.foodName = foodName;
        this.price = price;
        this.menuId = menuId;
        this.count = count;
        this.isFoodParty = isFoodParty;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMenuId() {
        return menuId;
    }

    public int getCount() {
        return count;
    }

    public boolean isFoodParty() {
        return isFoodParty;
    }

    public void setFoodParty(boolean foodParty) {
        isFoodParty = foodParty;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    @Override
    public String toString() {
        return "OrderMenuDAO{" +
                "orderId=" + orderId +
                ", menuId=" + menuId +
                ", count=" + count +
                ", isFoodParty=" + isFoodParty +
                '}';
    }
}
