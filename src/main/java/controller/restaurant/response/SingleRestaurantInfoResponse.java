package controller.restaurant.response;



import domain.entity.Location;
import domain.entity.Menu;
import domain.entity.Restaurant;

import java.util.ArrayList;

public class SingleRestaurantInfoResponse {
    private String id;
    private String name;
    private Location location;
    private String imageUrl;
    private ArrayList<Menu> menus;
    private String estimateDelivery;


    public SingleRestaurantInfoResponse(Restaurant restaurant , String estimate) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.location = restaurant.getLocation();
        this.imageUrl = restaurant.getImageUrl();
        this.menus = restaurant.getMenus();
        this.estimateDelivery = estimate;
    }

    public String getId(){
        return  id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }
    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void  setMenus(ArrayList<Menu> menus){
        this.menus = menus;
    }

    public ArrayList<Menu> getMenus(){
        return this.menus;
    }

    public String getEstimateDelivery() {
        return estimateDelivery;
    }

    public void setEstimateDelivery(String estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
    }
}

