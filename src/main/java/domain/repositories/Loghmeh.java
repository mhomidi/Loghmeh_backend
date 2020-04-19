package domain.repositories;


import dataAccess.dataMapper.delivery.DeliveryMapper;
import dataAccess.dataMapper.foodPartyMenus.MenuPartyMapper;
import dataAccess.dataMapper.menu.MenuMapper;
import dataAccess.dataMapper.orderMenu.OrderMenuMapper;
import dataAccess.dataMapper.orders.OrdersMapper;
import dataAccess.dataMapper.restaurant.RestaurantMapper;
import dataAccess.dataMapper.user.UserMapper;
import domain.entity.Delivery;
import domain.entity.FoodParty;
import domain.entity.Restaurant;
import domain.entity.User;
import domain.exceptions.RestaurantExist;
import domain.manager.RestaurantManager;
import tools.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.time.LocalTime;
import java.util.ArrayList;


public class Loghmeh {
    private ArrayList<Restaurant> restaurants;
    private ArrayList<User> users;
    private static Loghmeh instance;
    private ArrayList<Delivery> deliveries;
    private ArrayList<FoodParty> foodParties;
    private LocalTime startGetFoodParty;

    private UserMapper userMapper;
    private RestaurantMapper restaurantMapper;
    private MenuMapper menuMapper;
    private DeliveryMapper deliveryMapper;
    private MenuPartyMapper menuPartyMapper;
    private OrdersMapper allOrdersMapper;
    private OrderMenuMapper orderMenuMapper;


    private Loghmeh() {
        restaurants = new ArrayList<Restaurant>();
        deliveries = new ArrayList<Delivery>();
        foodParties = new ArrayList<FoodParty>();
        users = new ArrayList<User>();


        this.startGetFoodParty = LocalTime.now();
        this.userMapper = UserMapper.getInstance();
        this.restaurantMapper = RestaurantMapper.getInstance();
        this.menuMapper = MenuMapper.getInstance();
        this.deliveryMapper = DeliveryMapper.getInstance();
        this.menuPartyMapper = MenuPartyMapper.getInstance();
        this.allOrdersMapper = OrdersMapper.getInstance();
        this.orderMenuMapper = OrderMenuMapper.getInstance();
    }

    public static Loghmeh getInstance() {
        if (instance == null) {
            instance = new Loghmeh();
            initialize();
        }
        return instance;
    }

    public static void initialize(){
        RestaurantManager.getInstance().getRestaurantsFromUrl();
    }

    public void setStartGetFoodParty(LocalTime startGetFoodParty) {
        this.startGetFoodParty = startGetFoodParty;
    }
    public LocalTime getStartGetFoodParty() {
        return this.startGetFoodParty;
    }










    public void addDelivery(Delivery new_delivery){
        this.deliveries.add(new_delivery);
    }


    public void setFoodParties(ArrayList<FoodParty> foodParties){
        this.foodParties = new ArrayList<FoodParty>();
        this.foodParties = foodParties;
    }
    public void setDeliveries(ArrayList<Delivery> deliveries){
        this.deliveries = new ArrayList<Delivery>();
        this.deliveries = deliveries;
    }













    public ArrayList<User> getUsers(){
        return users;
    }

    public ArrayList<Delivery> getDeliveries(){
        return this.deliveries;
    }

    public ArrayList<Restaurant> getRestaurants(){
        return this.restaurants;
    }

    public ArrayList<FoodParty> getFoodParties(){
        return this.foodParties;
    }



    public boolean isRestaurantExist(String id){
        ArrayList<Restaurant> restaurants = Loghmeh.getInstance().getRestaurants();
        for (int i=0;i<restaurants.size();i++){
            if(restaurants.get(i).getId().equals(id)){
                return true;
            }
        }
        return false;
    }



    public void addRestaurant(Restaurant new_restaurant){
        for (Restaurant restaurant:this.restaurants){
            if(restaurant.getId().equals(new_restaurant.getId())){
                return;
            }
        }
        restaurants.add(new_restaurant);
    }





    public FoodParty findRestaurantInFoodParty(String restaurantId){
        for(int i=0;i<foodParties.size();i++){
            if(foodParties.get(i).getRestaurantId().equals(restaurantId)){
                return foodParties.get(i);
            }
        }
        return null;
    }




    public void addRestaurant(JSONObject jsonObject) throws RestaurantExist {
        String new_restaurant_name = jsonObject.get("name").toString();
        String new_restaurant_id = jsonObject.get("id").toString();
        if(isRestaurantExist(new_restaurant_id)){
            throw new RestaurantExist("You want to add new Restaurant with id " + new_restaurant_id + " which already exist!");
        }
        else{
            Restaurant new_rest = new Restaurant(jsonObject);
            restaurants.add(new_rest);
            System.out.println("adding restaurant " + new_restaurant_name +" with id " + new_restaurant_id+" successfully finished!");
        }
    }

}