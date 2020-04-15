package tools;


import domain.entity.Delivery;
import domain.exceptions.RestaurantExist;
import domain.repositories.Repository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Utility {

    public static void addRestaurantFromJSONArray(Repository system, JSONArray jsonArray) {
        for(int i=0;i<jsonArray.size();i++){
            try{
                system.addRestaurantCommand((JSONObject)jsonArray.get(i));
            }catch (RestaurantExist e){
                System.out.println(e.getMessage());
            }
        }
    }





    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm){
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list =
                new LinkedList<Map.Entry<String,Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<Delivery, Integer> sortByValueDeliveryTime(HashMap<Delivery, Integer> hm){
        // Create a list from elements of HashMap
        List<Map.Entry<Delivery, Integer> > list =
                new LinkedList<Map.Entry<Delivery,Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Delivery, Integer> >() {
            public int compare(Map.Entry<Delivery, Integer> o1,
                               Map.Entry<Delivery, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Delivery, Integer> temp = new LinkedHashMap<Delivery, Integer>();
        for (Map.Entry<Delivery, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}

