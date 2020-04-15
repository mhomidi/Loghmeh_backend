package services;


import org.json.simple.JSONArray;

public class GetFoodPartyList implements Runnable {

    @Override
    public void run()  {
        JSONArray jsonArray = RestaurantService.getInstance().requestFoodPartApiGetList();
        RestaurantService.getInstance().updateListOfFoodParty(jsonArray);
    }
}


