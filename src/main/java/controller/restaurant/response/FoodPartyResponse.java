package controller.restaurant.response;



public class FoodPartyResponse {
    private String restaurantId;
    private String restaurantName;
    private String restaurantLogo;
    private String menuName;
    private String menuDescription;
    private Double menuPopularity;
    private Double menuOldPrice;
    private String menuUrlImage;
    private Double menuNewPrice;
    private int menuCount;


    public FoodPartyResponse(String restaurantId , String restaurantName , String restaurantLogo ,
                             String menuName, String menuDescription, Double menuNewPrice , Double menuOldPrice ,
                             String menuUrlImage , Double menuPopularity , int menuCount ) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantLogo = restaurantLogo;
        this.menuName = menuName;
        this.menuDescription = menuDescription;
        this.menuNewPrice = menuNewPrice;
        this.menuOldPrice = menuOldPrice;
        this.menuUrlImage = menuUrlImage;
        this.menuPopularity = menuPopularity;
        this.menuCount = menuCount;

    }

    public Double getMenuNewPrice() {
        return menuNewPrice;
    }

    public Double getMenuOldPrice() {
        return menuOldPrice;
    }

    public Double getMenuPopularity() {
        return menuPopularity;
    }

    public int getMenuCount() {
        return menuCount;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuUrlImage() {
        return menuUrlImage;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantLogo() {
        return restaurantLogo;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setMenuCount(int menuCount) {
        this.menuCount = menuCount;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;

    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setMenuNewPrice(Double menuNewPrice) {
        this.menuNewPrice = menuNewPrice;
    }

    public void setMenuOldPrice(Double menuOldPrice) {
        this.menuOldPrice = menuOldPrice;
    }

    public void setMenuPopularity(Double menuPopularity) {
        this.menuPopularity = menuPopularity;
    }

    public void setMenuUrlImage(String menuUrlImage) {
        this.menuUrlImage = menuUrlImage;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantLogo(String restaurantLogo) {
        this.restaurantLogo = restaurantLogo;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }


}


