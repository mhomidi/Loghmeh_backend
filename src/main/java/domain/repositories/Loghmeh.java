package domain.repositories;

import dataAccess.dataMapper.delivery.DeliveryMapper;
import dataAccess.dataMapper.menu.MenuMapper;
import dataAccess.dataMapper.restaurant.RestaurantMapper;
import dataAccess.dataMapper.user.UserMapper;
import domain.entity.Delivery;
import domain.entity.FoodParty;
import domain.entity.Restaurant;
import domain.entity.User;
import domain.exceptions.RestaurantExist;
import tools.Request;
import tools.Utility;
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

    }


    public void addDelivery(Delivery new_delivery){
        this.deliveries.add(new_delivery);
    }

    public void setStartGetFoodParty(LocalTime startGetFoodParty) {
        this.startGetFoodParty = startGetFoodParty;
    }

    public LocalTime getStartGetFoodParty() {
        return this.startGetFoodParty;
    }

    public void setFoodParties(ArrayList<FoodParty> foodParties){
        this.foodParties = new ArrayList<FoodParty>();
        this.foodParties = foodParties;
    }
    public void setDeliveries(ArrayList<Delivery> deliveries){
        this.deliveries = new ArrayList<Delivery>();
        this.deliveries = deliveries;
    }

    public static Loghmeh getInstance() {
        if (instance == null) {
            instance = new Loghmeh();
            String restaurantUrl = "http://138.197.181.131:8080/restaurants";
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            System.out.println("start adding restaurant");
            try {
                String jsonRestaurants = Request.get(restaurantUrl);
                JSONParser parser = new JSONParser();
                JSONArray jsonArray = new JSONArray();
                jsonArray = (JSONArray) parser.parse(jsonRestaurants);
                Utility.addRestaurantFromJSONArray(instance, jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("finish adding restaurant");
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n\n\n\n");
        }
        return instance;
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

    public void addUser(User new_user){
        users.add(new_user);
        System.out.println("new user added");
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

    public Restaurant getRestaurantWithId(String id){
        for (Restaurant restaurant:restaurants){
            if (restaurant.getId().equals(id)){
                return restaurant;
            }
        }
        return null;
    }


    public ArrayList<Restaurant> getRestaurantsAvailableForUser(){
        ArrayList<Restaurant> result = new ArrayList<Restaurant>();
        for (Restaurant restaurant:restaurants){
            if(restaurant.distanceFromUser()<=170){
                result.add(restaurant);
            }
        }
        return result;
    }

    public FoodParty findRestaurantInFoodParty(String restaurantId){
        for(int i=0;i<foodParties.size();i++){
            if(foodParties.get(i).getRestaurantId().equals(restaurantId)){
                return foodParties.get(i);
            }
        }
        return null;
    }

    public boolean restaurantAvailable(Restaurant restaurant){
        return (restaurant.distanceFromUser()<=170);
    }


    public boolean isMenuFoodPartyOfRestaurant(Restaurant restaurant , String foodName){
        FoodParty foodParty = findRestaurantInFoodParty(restaurant.getId());
        if(foodParty!=null && foodParty.hasFood(foodName)){
            return true;
        }
        else{
            return false;
        }
    }

    public void addRestaurantCommand(JSONObject jsonObject) throws RestaurantExist {
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