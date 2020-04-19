package services;


import domain.manager.RestaurantManager;
import domain.repositories.Loghmeh;
import org.json.simple.JSONArray;

public class GetFoodPartyList implements Runnable {

    @Override
    public void run()  {
        Loghmeh instance = Loghmeh.getInstance();
        System.out.println("get food party triggered by scheduler");
        RestaurantManager.getInstance().getFoodPartiesFromUrl();
    }
}


