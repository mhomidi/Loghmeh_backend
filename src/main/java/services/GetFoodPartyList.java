package services;


import domain.manager.RestaurantManager;
import org.json.simple.JSONArray;

public class GetFoodPartyList implements Runnable {

    @Override
    public void run()  {
        System.out.println("get food party triggered by scheduler");
        RestaurantManager.getInstance().getFoodPartiesFromUrl();
        JSONArray jsonArray = RestaurantManager.getInstance().requestFoodPartApiGetList();
        RestaurantManager.getInstance().updateListOfFoodParty(jsonArray);
    }
}


