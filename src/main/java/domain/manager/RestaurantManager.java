package domain.manager;

import dataAccess.dataMapper.menu.MenuMapper;
import dataAccess.dataMapper.restaurant.RestaurantMapper;
import domain.databaseEntity.MenuDAO;
import domain.databaseEntity.RestaurantDAO;
import domain.entity.*;
import domain.exceptions.FoodNotExist;
import domain.exceptions.RestaurantExist;
import domain.exceptions.RestaurantNotAvailable;
import domain.exceptions.RestaurantNotFound;
import domain.repositories.Loghmeh;

import domain.entity.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tools.Request;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import static java.time.temporal.ChronoUnit.SECONDS;


public class RestaurantManager {
    private static RestaurantManager instance;
    private static int PERIOD_GET_FOOD_PARTY = 60*30;

    private RestaurantManager() {
    }
    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    public void getRestaurantsFromUrl(){
        String restaurantUrl = "http://138.197.181.131:8080/restaurants";
        System.out.println("\n\n\n#################################");
        System.out.println("start adding new restaurant to database from url");
        try {
            String jsonRestaurants = Request.get(restaurantUrl);
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = new JSONArray();
            jsonArray = (JSONArray) parser.parse(jsonRestaurants);
            this.addRestaurantFromJSONArray(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("finish adding restaurant");
        System.out.println("#####################################\n\n\n");
    }

    public void addRestaurantFromJSONArray(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject restaurantInfo = (JSONObject) jsonArray.get(i);
            String new_rest_name = restaurantInfo.get("name").toString();
            String new_rest_id = restaurantInfo.get("id").toString();
            String new_rest_logo = restaurantInfo.get("logo").toString();
            Double new_rest_loc_x = Double.parseDouble(((JSONObject) restaurantInfo.get("location")).get("x").toString());
            Double new_rest_loc_y = Double.parseDouble(((JSONObject) restaurantInfo.get("location")).get("y").toString());
            RestaurantDAO new_rest = new RestaurantDAO(new_rest_id, new_rest_name, new_rest_logo, new_rest_loc_x, new_rest_loc_y);
            ArrayList<MenuDAO> menusToAddDatabase = new ArrayList<MenuDAO>();
            JSONArray array_menus = (JSONArray) restaurantInfo.get("menu");
            for (int j = 0; j < array_menus.size(); j++) {
                JSONObject menuInfo = (JSONObject) array_menus.get(j);
                String name = menuInfo.get("name").toString();
                String description = menuInfo.get("description").toString();
                Double popularity = Double.parseDouble(menuInfo.get("popularity").toString());
                Double price = Double.parseDouble(menuInfo.get("price").toString());
                String urlImage = menuInfo.get("image").toString();
                menusToAddDatabase.add(new MenuDAO(new_rest_id, name, description, popularity, price, urlImage));
            }
            try {
                RestaurantMapper.getInstance().insert(new_rest);
            }catch (SQLException ignored){
            }
            for(MenuDAO menuDAO:menusToAddDatabase){
                try {
                    MenuMapper.getInstance().insert(menuDAO);
                }catch (SQLException e){
                    continue;
                }
            }
//            System.out.println("new restaurant:");
//            System.out.println(new_rest);
//            for (MenuDAO menuDAO : menusToAddDatabase) {
//                System.out.println(menuDAO);
//            }
        }
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
        ArrayList<Restaurant> restaurants = Loghmeh.getInstance().getRestaurants();
        for(int i=0;i<restaurants.size();i++){
            if(restaurants.get(i).getId().equals(id)){
                return restaurants.get(i);
            }
        }
        return null;
    }

    private  void insert(Restaurant new_restaurant){
        Loghmeh.getInstance().addRestaurant(new_restaurant);
    }


    public  ArrayList<Restaurant> getAllRestaurants(){
        return Loghmeh.getInstance().getRestaurants();
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
        ArrayList<Restaurant> restaurants = Loghmeh.getInstance().getRestaurants();
        for (Restaurant restaurant:restaurants){
            if (restaurant.getId().equals(id)){
                return restaurant;
            }
        }
        return null;
    }

    public FoodParty findRestaurantInFoodParty(String restaurantId){
        ArrayList<FoodParty> foodParties = Loghmeh.getInstance().getFoodParties();
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
        Loghmeh.getInstance().setStartGetFoodParty(LocalTime.now());
    }

    public LocalTime getTimerForFoodParty(){
        return Loghmeh.getInstance().getStartGetFoodParty();
    }


    public ArrayList<FoodParty> getFoodParties(){
        return Loghmeh.getInstance().getFoodParties();
    }


    public void setFoodParties(ArrayList<FoodParty> foodParties){
        Loghmeh.getInstance().setFoodParties(foodParties);
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
        ArrayList<Restaurant> restaurants = Loghmeh.getInstance().getRestaurants();
        for (int i=0;i<restaurants.size();i++){
            if(restaurants.get(i).getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public void addRestaurant(Restaurant restaurant){
        Loghmeh.getInstance().addRestaurant(restaurant);
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
