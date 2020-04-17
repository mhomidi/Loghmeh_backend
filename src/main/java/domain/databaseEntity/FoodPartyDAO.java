package domain.databaseEntity;

public class FoodPartyDAO {
    private int menuId;
    private int count;
    private Double newPrice;

    public FoodPartyDAO(int menuId, int count, Double newPrice){
        this.menuId = menuId;
        this.count = count ;
        this.newPrice = newPrice;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

    public Double getNewPrice() {
        return newPrice;
    }


    public int getCount() {
        return count;
    }

    public int getMenuId() {
        return menuId;
    }


    public void setCount(int count) {
        this.count = count;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
