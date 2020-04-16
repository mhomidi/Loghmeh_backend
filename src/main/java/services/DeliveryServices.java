package services;

import domain.entity.*;
import domain.exceptions.RestaurantNotAvailable;
import domain.exceptions.RestaurantNotFound;
import domain.repositories.Loghmeh;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tools.Request;
import tools.Utility;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeliveryServices {

    private static DeliveryServices instance;


    private DeliveryServices() {
    }

    public static DeliveryServices getInstance() {
        if (instance == null) {
            instance = new DeliveryServices();
        }
        return instance;
    }


    public void DeliveryUserOrder(User user) throws RestaurantNotAvailable, RestaurantNotFound {
        JSONArray jsonArray = this.requestDeliveryApiGetList();
        this.updateListOfDelivery(jsonArray);
        if (this.NoDelivery()) {
            System.out.println("No delivery available now.Handle later.put this order for user in not found array list!");
            user.addCurrentOrderToNotFoundDeliveryList(user.getCurrentOrder());
        } else {
            System.out.println("try to find delivery with min time for user");
            Delivery findDelivery = this.findDeliveryForOrder(user.getCurrentOrder());
            if(findDelivery == null){
                System.out.println("ERROR No delivery available now.Handle later.put this order for user in not found array list!");
                user.addCurrentOrderToNotFoundDeliveryList(user.getCurrentOrder());
            }
            else {
                System.out.println("delivery find is:==> " + findDelivery);
                user.getCurrentOrder().orderGiveToDeliverySetTime(LocalTime.now(), findDelivery.getId());
                user.getCurrentOrder().setStatus(DeliveryStatus.DELIVERING);
            }
        }
    }

    public  void updateListOfDelivery(JSONArray jsonArray){
        this.cleanDeliveries();
        for(int i=0;i<jsonArray.size();i++){
            if(!this.deliveryExist(((JSONObject)jsonArray.get(i)).get("id").toString())){
                Delivery new_delivery = new  Delivery((JSONObject)jsonArray.get(i));
                System.out.println(new_delivery);
                this.addDelivery(new_delivery);
            }
            else{
                System.out.println("Delivery with id " +
                        ((JSONObject)jsonArray.get(i)).get("id").toString() +" already added!");
            }
        }
        System.out.println("adding new deliveries successfully done.");
        System.out.println("#########################################");
    }


    public  JSONArray requestDeliveryApiGetList() {
        System.out.println("#########################################");
        System.out.println("request to get delivery...");
        String deliveryUrl = "http://138.197.181.131:8080/deliveries";
        JSONArray jsonArray = new JSONArray();
        try {
            String jsonDeliveries = Request.get(deliveryUrl);
            JSONParser parser = new JSONParser();
            jsonArray = (JSONArray) parser.parse(jsonDeliveries);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(jsonArray);
        return  jsonArray;
    }


    public boolean deliveryExist(String id) {
        ArrayList<Delivery> deliveries = Loghmeh.getInstance().getDeliveries();
        for (Delivery delivery : deliveries) {
            if (delivery.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


    public void addDelivery(Delivery new_delivery){
        Loghmeh.getInstance().addDelivery(new_delivery);
    }

    public void cleanDeliveries(){
        ArrayList<Delivery> deliveries = new ArrayList<Delivery>();
        Loghmeh.getInstance().setDeliveries(deliveries);
    }

    public boolean NoDelivery(){
        return Loghmeh.getInstance().getDeliveries().size() == 0 ;
    }


    public Delivery findDeliveryForOrder(Order order){
        System.out.println("in this function we find nearest delivery:");
        System.out.println("#####Start#######");
        HashMap<Delivery, Integer> timeToDelivery = new HashMap<Delivery,Integer>();
        for(Delivery delivery : this.getAllDeliveries()){
            String restaurantId = order.getRestaurantId();
            String restaurantName = order.getRestaurantName();
            System.out.println(restaurantId);
            System.out.println(restaurantName);
            Restaurant restaurant = RestaurantService.getInstance().getRestaurantWithId(restaurantId);
            if(restaurant == null){
                System.out.println("not finding restauarnt so return null");
                return null;
            }
            Double distance_delivery_restaurant = delivery.distanceFromRestaurant(restaurant);
            System.out.println("distance delivery to restaurant:" + distance_delivery_restaurant);
            Double distance_restaurant_user = restaurant.distanceFromUser();
            System.out.println("distance user to restaurant:" + distance_restaurant_user);
            int seconds = (int)((distance_delivery_restaurant + distance_restaurant_user)/delivery.getVelocity());
            timeToDelivery.put(delivery,seconds);



        }

        for (Map.Entry<Delivery,Integer> entry : timeToDelivery.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
        HashMap<Delivery,Integer> sort_time_to_delivery = Utility.sortByValueDeliveryTime(timeToDelivery);
        Map.Entry<Delivery,Integer> entry = sort_time_to_delivery.entrySet().iterator().next();
        Delivery find_delivery = entry.getKey();
        Integer calc_time = entry.getValue();
        order.setTotalDeliveryTime(calc_time);
        return find_delivery;
    }

    public ArrayList<Delivery> getAllDeliveries(){
        return Loghmeh.getInstance().getDeliveries();
    }

}
