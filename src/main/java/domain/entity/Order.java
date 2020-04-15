package domain.entity;


import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Order {
    private int id;
    private Map<Menu, Integer> menus;
    private Map<MenuParty,Integer> party_menus;
    private String restaurantName;
    private String restaurantId;
    DeliveryStatus status;
    int totalDeliveryTime;
    LocalTime timeGiveToDelivery;
    String deliverPersonId;





    public int getTotalDeliveryTime(){
        return this.totalDeliveryTime;
    }


    public String getDeliverPersonId(){
        return this.deliverPersonId;
    }

    public LocalTime getTimeGiveToDelivery(){
        return timeGiveToDelivery;
    }


    public Order() {
        menus = new HashMap<Menu, Integer>();
        party_menus = new HashMap<MenuParty,Integer>();
        restaurantName = null;
        restaurantId = null;
        status = DeliveryStatus.NOT_FINALIZING;
    }

    public String remainingTime(){
        long dif = timeGiveToDelivery.until(LocalTime.now(),SECONDS);
        if(dif < totalDeliveryTime){
            System.out.println(dif);
            return LocalTime.MIN.plusSeconds(totalDeliveryTime- dif).toString();
        }
        this.setStatus(DeliveryStatus.DONE);
        return "00:00:00";

    }

    public void chooseStatus(){
        long dif = timeGiveToDelivery.until(LocalTime.now(),SECONDS);
        if(dif < totalDeliveryTime){
            return;
        }
        this.setStatus(DeliveryStatus.DONE);
    }

    public void orderGiveToDeliverySetTime(LocalTime now , String deliverPersonId){
        this.timeGiveToDelivery = now;
        this.deliverPersonId = deliverPersonId;
    }


    public void setTotalDeliveryTime(int seconds){
        this.totalDeliveryTime = seconds;
    }


    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void addFood(Menu menu , int count) {
        if (menus.containsKey(menu)) {
            menus.replace(menu, menus.get(menu) + count);
        }
        else {
            menus.put(menu, count);
        }
    }

    public void addPartyFood(MenuParty menu , int count){
        if(party_menus.containsKey(menu)){
            party_menus.replace(menu,party_menus.get(menu)+count);
        }
        else{
            party_menus.put(menu,count);
        }
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantId(String restaurantId){
        this.restaurantId = restaurantId;
    }

    public String getRestaurantId(){
        return this.restaurantId;
    }



    public Map<Menu, Integer> getMenus() {
        return menus;
    }


    public Map<MenuParty,Integer> getPartyMenus(){
        return party_menus;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }
    public String getRestaurantName() {
        if (restaurantName != null)
            return restaurantName;
        return "";
    }

    public int getFoodCount(Menu menu) {
        return menus.get(menu);
    }

    public int getFoodCount(MenuParty menu){
        return party_menus.get(menu);
    }

    public void deleteMenuFromOrder(int foodParty , String foodName){
        if(foodParty==0){
            Iterator<Map.Entry<Menu, Integer> >
                    iterator = menus.entrySet().iterator();
            // Iterate over the HashMap
            while (iterator.hasNext()) {
                Map.Entry<Menu, Integer> entry = iterator.next();
                // Check if this key is the required key
                if (foodName.equals(entry.getKey().getName())) {
                    // Remove this entry from HashMap
                    System.out.println("here we remove food from order!");
                    iterator.remove();
                }
            }
            System.out.println("user delete " + foodName + " from his/her order");
        }
        else{
            Iterator<Map.Entry<MenuParty, Integer> >
                    iterator = party_menus.entrySet().iterator();
            // Iterate over the HashMap
            while (iterator.hasNext()) {
                Map.Entry<MenuParty, Integer> entry = iterator.next();
                // Check if this key is the required key
                if ( foodName.equals(entry.getKey().getName())) {
                    // Remove this entry from HashMap
                    iterator.remove();
                }
            }
            System.out.println("user delete " + foodName + " from his/her order");
        }
    }





}