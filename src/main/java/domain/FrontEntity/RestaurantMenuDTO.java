package domain.FrontEntity;

import java.util.ArrayList;

public class RestaurantMenuDTO {
    private String restaurantId;
    private String restaurantName;
    private String logoUrl;
    private String estimateDelivery;
    private ArrayList<MenuDTO> menus;


    public RestaurantMenuDTO(String id, String name , String imageUrl) {
        this.restaurantId = id;
        this.restaurantName = name;
        this.logoUrl = imageUrl;
        this.menus = new ArrayList<MenuDTO>();
    }


    public String getId(){
        return  restaurantId;
    }
    public void setId(String id){
        this.restaurantId = id;
    }

    public String getName(){
        return restaurantName;
    }

    public void setName(String name){
        this.restaurantName = name;
    }


    public String getLogoUrl(){
        return logoUrl;
    }

    public void setLogoUrl(String imageUrl){
        this.logoUrl = imageUrl;
    }

    public String getEstimateDelivery() {
        return estimateDelivery;
    }

    public void setEstimateDelivery(String estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
    }

    public ArrayList<MenuDTO> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<MenuDTO> menus) {
        this.menus = menus;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
