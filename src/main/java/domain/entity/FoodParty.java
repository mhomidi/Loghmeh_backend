package domain.entity;

import java.time.LocalTime;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.SECONDS;

public class FoodParty {
    private String restaurantId;
    private String restaurantName;
    private Location location;
    private String restaurantLogo;
    private ArrayList<MenuParty> menus;
    private LocalTime startTime;
    private int period;


    public FoodParty(String restaurantId , String restaurantName , String restaurantLogo,
                     Location location , int period){
        this.restaurantId = restaurantId;
        this.restaurantLogo = restaurantLogo;
        this.restaurantName = restaurantName;
        this.location = location;
        this.startTime = LocalTime.now();
        menus = new ArrayList<MenuParty>();
        this.period = period;
    }

    public void addMenu(MenuParty menu){
        for(int i=0;i<menus.size();i++){
            if(menus.get(i).getName().equals(menu.getName())){
                System.out.println("Already food with name " + menu.getName() +" is in menu of restaurant "+
                        restaurantName + " as a food party!");
                return;
            }
        }
        menus.add(menu);
    }

    public String getRestaurantId(){
        return restaurantId;
    }

    public String getRestaurantName(){
        return  restaurantName;
    }

    public String getRestaurantLogo(){
        return restaurantLogo;
    }

    public ArrayList<MenuParty> getMenus(){
        return this.menus;
    }

    public LocalTime getStartTime(){
        return this.startTime;
    }

    public boolean hasFood(String foodName){
        for(int i=0;i<menus.size();i++){
            if(menus.get(i).getName().equals(foodName)){
                return  true;
            }
        }
        return false;
    }

    public MenuParty findMenuOfFood(String foodName){
        for(int i=0;i<menus.size();i++){
            if(menus.get(i).getName().equals(foodName)){
                return menus.get(i);
            }
        }
        return null;
    }

    public boolean timeAvailable(){
        long dif = startTime.until(LocalTime.now(),SECONDS);
        if(dif < period){
            return true;
        }
        return false;
    }

    public void decreaseCountOfPartyFood(String foodName,int count){
        for(int i=0;i<menus.size();i++){
            if(menus.get(i).getName().equals(foodName)){
                System.out.println("decrese count food " + foodName);
                menus.get(i).decreaseCount(count);
                return;
            }
        }
    }




}
