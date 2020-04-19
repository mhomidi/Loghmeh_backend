package domain.manager;

import dataAccess.dataMapper.delivery.DeliveryMapper;
import dataAccess.dataMapper.orders.OrdersMapper;
import dataAccess.dataMapper.restaurant.RestaurantMapper;
import domain.databaseEntity.DeliveryDAO;
import domain.databaseEntity.RestaurantDAO;
import domain.entity.*;
import domain.exceptions.NotFindOrder;
import domain.exceptions.RestaurantNotAvailable;
import domain.exceptions.RestaurantNotFound;
import domain.repositories.Loghmeh;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tools.Request;
import tools.Utility;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

public class DeliveryManager {

    private static DeliveryManager instance;
    private static String deliveryUrl = "http://138.197.181.131:8080/deliveries";


    private DeliveryManager() {
    }

    public static DeliveryManager getInstance() {
        if (instance == null) {
            instance = new DeliveryManager();
        }
        return instance;
    }


    public void getDeliveryFromUrl(){
        System.out.println("\n\n\n#########################################");
        System.out.println("request to get delivery adding to database");
        JSONArray jsonArray = new JSONArray();
        try {
            String jsonDeliveries = Request.get(deliveryUrl);
            JSONParser parser = new JSONParser();
            jsonArray = (JSONArray) parser.parse(jsonDeliveries);
            this.addDeliveryFromJSONArray(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDeliveryFromJSONArray(JSONArray jsonArray){
        DeliveryMapper.getInstance().makeAllDeliveryInActive();
        for(int i=0;i<jsonArray.size();i++){
            JSONObject deliveryInfo = (JSONObject) jsonArray.get(i);
            String id = deliveryInfo.get("id").toString();
            Double velocity = Double.parseDouble(deliveryInfo.get("velocity").toString());
            Double delivery_loc_x = Double.parseDouble(((JSONObject) deliveryInfo.get("location")).get("x").toString());
            Double delivery_loc_y = Double.parseDouble(((JSONObject) deliveryInfo.get("location")).get("y").toString());
            DeliveryDAO new_delivery = new DeliveryDAO(id, velocity, delivery_loc_x, delivery_loc_y);
            try {
                DeliveryMapper.getInstance().insert(new_delivery);
            }catch (SQLException ignored){
            }
            System.out.println(jsonArray.size());
            System.out.println(new_delivery);
        }
        System.out.println("adding new deliveries successfully done.");
        System.out.println("#########################################\n\n\n");
    }


    public void deliveryUserOrder(int orderId){
        this.getDeliveryFromUrl();
        if(this.getAllDeliveries().size() == 0){
            System.out.println("No delivery available now.Handle later.");
        }
        else {
            System.out.println("try to find delivery with min time for user");
            DeliveryDAO findDelivery = this.findDeliveryForOrder(orderId);
            if(findDelivery == null){
                System.out.println("ERROR No delivery available now.Handle later!");
            }
            else {
                System.out.println("delivery find is:==> " + findDelivery.getId());
                //here we should update field in order
//                user.getCurrentOrder().orderGiveToDeliverySetTime(LocalTime.now(), findDelivery.getId());
//                user.getCurrentOrder().setStatus(DeliveryStatus.DELIVERING);
                OrdersMapper.getInstance().updateOrderAddDeliveryIdChangeStatus(orderId, findDelivery.getId());
            }
        }
    }

    public ArrayList<DeliveryDAO> getAllDeliveries() {
        return DeliveryMapper.getInstance().getAllDeliveries();
    }


    public DeliveryDAO findDeliveryForOrder(int orderId) {
        System.out.println("in this function we find nearest delivery:");
        System.out.println("#####Start#######");
        HashMap<DeliveryDAO, Integer> timeToDelivery = new HashMap<DeliveryDAO,Integer>();
        String restaurantId;
        RestaurantDAO restaurant;
        try {
            restaurantId = OrdersMapper.getInstance().getRestaurantIdForOrderOfUser(orderId);
        }catch (SQLException | NotFindOrder e){
            return null;
        }
        try {
            restaurant = RestaurantMapper.getInstance().find(restaurantId);
            if (restaurant == null) {
                System.out.println("not finding restaurant id in user order so return null");
                return null;
            }
            if (restaurant.getLocation_X() * restaurant.getLocation_X() +
                    restaurant.getLocation_Y() * restaurant.getLocation_Y() > 28900) {
                System.out.println("restaurant id in user order not available so return null");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("restaurant id in user order not find due to sql exception");
            return null;
        }
        ArrayList<DeliveryDAO> deliveryDAOS = this.getAllDeliveries();
        for(DeliveryDAO delivery : deliveryDAOS) {
            Double distance_delivery_restaurant = this.getDistanceDeliveryFromRestaurant(delivery,restaurant);
            System.out.println("distance delivery to restaurant:" + distance_delivery_restaurant);
            Double distance_restaurant_user = RestaurantManager.getInstance().getDistanceRestaurantFromUser(restaurant);
            System.out.println("distance user to restaurant:" + distance_restaurant_user);
            int seconds = (int)((distance_delivery_restaurant + distance_restaurant_user)/delivery.getVelocity());
            timeToDelivery.put(delivery,seconds);
        }

        for (Map.Entry<DeliveryDAO,Integer> entry : timeToDelivery.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
        HashMap<DeliveryDAO,Integer> sort_time_to_delivery = this.sortByValueDeliveryTime(timeToDelivery);
        Map.Entry<DeliveryDAO,Integer> entry = sort_time_to_delivery.entrySet().iterator().next();
        DeliveryDAO find_delivery = entry.getKey();
        Integer calc_time = entry.getValue();
        OrdersMapper.getInstance().addCalcDeliveryTimeToOrder(orderId , calc_time);
        return find_delivery;
    }


    public Double getDistanceDeliveryFromRestaurant(DeliveryDAO delivery , RestaurantDAO restaurant){
        double x_restaurant = restaurant.getLocation_X();
        double y_restaurant = restaurant.getLocation_Y();
        double x_delivery = delivery.getLocation_X();
        double y_delivery = delivery.getLocation_X();
        return Math.sqrt(((x_restaurant-x_delivery)* (x_restaurant-x_delivery))
                + ((y_restaurant - y_delivery) * (y_restaurant - y_delivery)));
    }



    public HashMap<DeliveryDAO, Integer> sortByValueDeliveryTime(HashMap<DeliveryDAO, Integer> hm){
        // Create a list from elements of HashMap
        List<Map.Entry<DeliveryDAO, Integer> > list =
                new LinkedList<Map.Entry<DeliveryDAO,Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<DeliveryDAO, Integer> >() {
            public int compare(Map.Entry<DeliveryDAO, Integer> o1,
                               Map.Entry<DeliveryDAO, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<DeliveryDAO, Integer> temp = new LinkedHashMap<DeliveryDAO, Integer>();
        for (Map.Entry<DeliveryDAO, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }



    public ArrayList<Integer> getAllUndeliveredOrders(){
        return OrdersMapper.getInstance().getAllUndeliveredOrders();
    }


}
