package services;

import domain.entity.*;
import domain.exceptions.FoodNotExist;
import domain.exceptions.RestaurantNotAvailable;
import domain.exceptions.RestaurantNotFound;
import domain.repositories.Repository;

import domain.entity.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tools.Request;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import static java.time.temporal.ChronoUnit.SECONDS;


public class RestaurantService {
    private static RestaurantService instance;
    private static int PERIOD_GET_FOOD_PARTY = 60*30;


    private RestaurantService() {
    }

    public static RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }





    public Restaurant getRestaurantById(String id) throws RestaurantNotFound , RestaurantNotAvailable {
        Restaurant restaurant;
        restaurant = find(id);
        if(restaurant != null) {
            if (this.restaurantAvailable(restaurant)) {
                return restaurant;
            } else {
                throw new RestaurantNotAvailable();
            }
        }
        else {
            throw new RestaurantNotFound();
        }
    }

    private  Restaurant find(String id){
        ArrayList<Restaurant> restaurants = Repository.getInstance().getRestaurants();
        for(int i=0;i<restaurants.size();i++){
            if(restaurants.get(i).getId().equals(id)){
                return restaurants.get(i);
            }
        }
        return null;
    }

    private  void insert(Restaurant new_restaurant){
        Repository.getInstance().addRestaurant(new_restaurant);
    }


    public  ArrayList<Restaurant> getAllRestaurants(){
        return Repository.getInstance().getRestaurants();
    }

    public boolean restaurantAvailable(Restaurant restaurant){
        return (restaurant.distanceFromUser()<=170);
    }

    public  ArrayList<Restaurant> getAvailableRestaurants(User user){
        ArrayList<Restaurant> restaurants = this.getAllRestaurants();
        ArrayList<Restaurant> results = new ArrayList<Restaurant>();
        for(Restaurant restaurant:restaurants){
            if(this.restaurantAvailable(restaurant)){
                results.add(restaurant);
            }
        }
        return results;
    }


    public Menu findMenuInRestaurantWithFoodName(Restaurant restaurant , String foodName)throws FoodNotExist{
        Menu food = restaurant.findFoodMenu(foodName);
        if(food!=null){
            return food;
        }
        else {
            throw new FoodNotExist();
        }
    }



    public boolean foodPartyTimeValidationForFinalizing(User user){
        System.out.println(LocalTime.now());
        Restaurant restaurantToBuy = this.getRestaurantWithId(user.getCurrentOrder().getRestaurantId());
        FoodParty foodParty = this.findRestaurantInFoodParty(restaurantToBuy.getId());
        if(foodParty==null){
            return false;
        }
        if(!foodParty.timeAvailable()){
            return  false;
        }
        return true;
    }


    public Restaurant getRestaurantWithId(String id){
        ArrayList<Restaurant> restaurants = Repository.getInstance().getRestaurants();
        for (Restaurant restaurant:restaurants){
            if (restaurant.getId().equals(id)){
                return restaurant;
            }
        }
        return null;
    }

    public FoodParty findRestaurantInFoodParty(String restaurantId){
        ArrayList<FoodParty> foodParties = Repository.getInstance().getFoodParties();
        for(int i=0;i<foodParties.size();i++){
            if(foodParties.get(i).getRestaurantId().equals(restaurantId)){
                return foodParties.get(i);
            }
        }
        return null;
    }

    public boolean foodPartyCountValidationForFinalizing(User user){
        Restaurant restaurantToBuy = this.getRestaurantWithId(user.getCurrentOrder().getRestaurantId());
        FoodParty foodParty = this.findRestaurantInFoodParty(restaurantToBuy.getId());
        for (Map.Entry<MenuParty,Integer> entry : user.getCurrentOrder().getPartyMenus().entrySet()){
            MenuParty menu = foodParty.findMenuOfFood(entry.getKey().getName());
            if(menu==null){
                return false;
            }
            if(entry.getValue()>menu.getCount()){
                return false;
            }
        }
        return true;
    }


    public void changeCountOfPartyFoodUserBuy(User user){
        System.out.println("decrease count food user buy!");
        FoodParty foodParty = this.findRestaurantInFoodParty(user.getCurrentOrder().getRestaurantId());
        for (Map.Entry<MenuParty,Integer> entry : user.getCurrentOrder().getPartyMenus().entrySet()){
            foodParty.decreaseCountOfPartyFood(entry.getKey().getName() , entry.getValue());
        }
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


    public  JSONArray requestFoodPartApiGetList(){
        System.out.println("#####################################################");
        System.out.println("request for food party:...");
        String foodPartyUrl = "http://138.197.181.131:8080/foodparty";
        JSONArray jsonArray = new JSONArray();
        try {
            String jsonParties = Request.get(foodPartyUrl);
            JSONParser parser = new JSONParser();
            jsonArray = (JSONArray) parser.parse(jsonParties);
            System.out.println("get food party triggered by scheduler");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonArray;
    }


    public  void updateListOfFoodParty(JSONArray jsonArray){
        for(FoodParty foodParty: this.getFoodParties()){
            String restaurantId = foodParty.getRestaurantId();
            Restaurant restaurant = getRestaurantWithId(restaurantId);
            ArrayList<MenuParty> partyMenus = foodParty.getMenus();
            for(MenuParty menu:partyMenus){
                Menu menu_to_add = new Menu(menu.getName(),menu.getDescription(),menu.getPopularity(),
                        menu.getOldPrice(),menu.getUrlImage());
                restaurant.addMenu(menu_to_add);
            }
        }
        ArrayList<FoodParty> foodParties = new ArrayList<FoodParty>();
        this.setFoodParties(foodParties);
        for (int i = 0; i < jsonArray.size(); i++) {
            String restaurantId = ((JSONObject) jsonArray.get(i)).get("id").toString();
            String restaurantName = ((JSONObject) jsonArray.get(i)).get("name").toString();
            Location restaurantLocation = new Location((JSONObject) ((JSONObject) (jsonArray.get(i))).get("location"));
            String restaurantUrl = ((JSONObject) jsonArray.get(i)).get("logo").toString();
            if (!isRestaurantExist(restaurantId)) {
                System.out.println("new restaurant " + restaurantName +" with id " + restaurantId
                        + " added to database from foodParty info getting from url!");
                Restaurant new_restaurant = new Restaurant(restaurantId, restaurantName, restaurantUrl, restaurantLocation);
                this.addRestaurant(new_restaurant);
            }
            if(!this.isRestaurantExistInFoodParty(restaurantId)) {
                FoodParty foodParty = new FoodParty(restaurantId, restaurantName, restaurantUrl, restaurantLocation ,PERIOD_GET_FOOD_PARTY);
                JSONArray array_menus_party = (JSONArray) ((JSONObject) jsonArray.get(i)).get("menu");
                for (int k = 0; k < array_menus_party.size(); k++) {
                    foodParty.addMenu(new MenuParty((JSONObject) array_menus_party.get(k)));
                }
                foodParties.add(foodParty);
            }
            else{
                System.out.println("repeated id of restaurant in jsonArray get from url." +
                        "you have already add restaurant " + restaurantName + " with id " +
                        restaurantId);
            }
        }
        this.setFoodParties(foodParties);
        this.setTimerForFoodParty();
        System.out.println("finish updating food party");
        System.out.println("###########################\n\n\n");

    }


    public void setTimerForFoodParty(){
        Repository.getInstance().setStartGetFoodParty(LocalTime.now());
    }

    public LocalTime getTimerForFoodParty(){
        return Repository.getInstance().getStartGetFoodParty();
    }


    public ArrayList<FoodParty> getFoodParties(){
        return Repository.getInstance().getFoodParties();
    }


    public void setFoodParties(ArrayList<FoodParty> foodParties){
        Repository.getInstance().setFoodParties(foodParties);
    }


    public boolean isRestaurantExistInFoodParty(String restaurantId){
        ArrayList<FoodParty> foodParties = this.getFoodParties();
        for(int i=0;i<foodParties.size();i++){
            if(foodParties.get(i).getRestaurantId().equals(restaurantId)){
                return true;
            }
        }
        return  false;
    }

    public boolean isRestaurantExist(String id){
        ArrayList<Restaurant> restaurants = Repository.getInstance().getRestaurants();
        for (int i=0;i<restaurants.size();i++){
            if(restaurants.get(i).getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public void addRestaurant(Restaurant restaurant){
        Repository.getInstance().addRestaurant(restaurant);
    }


    public String estimateTime(String restaurantId){
        Restaurant restaurant = getRestaurantWithId(restaurantId);
        int time_find_delivery = 60;
        int average_velocity = 5;
        double distance_from_user =  restaurant.distanceFromUser();
        int total_time = time_find_delivery + (int)((1.5)*(distance_from_user/average_velocity));
        return LocalTime.MIN.plusSeconds(total_time).toString();
    }


    public Long getRemainingTimeFoodParty(){
        ArrayList<FoodParty> foodParties = this.getFoodParties();
        Long dif = foodParties.get(0).getStartTime().until(LocalTime.now(),SECONDS);
        return dif;
    }


}
