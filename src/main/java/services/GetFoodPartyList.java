package services;


import domain.manager.RestaurantManager;
import org.json.simple.JSONArray;

public class GetFoodPartyList implements Runnable {

    @Override
    public void run()  {
        JSONArray jsonArray = RestaurantManager.getInstance().requestFoodPartApiGetList();
        RestaurantManager.getInstance().updateListOfFoodParty(jsonArray);
    }
}


