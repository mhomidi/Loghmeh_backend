package controller.restaurant;
import controller.restaurant.response.FoodPartyResponse;
import controller.restaurant.response.SingleRestaurantInfoResponse;
import domain.entity.FoodParty;
import domain.entity.MenuParty;
import domain.entity.Restaurant;
import domain.exceptions.RestaurantNotAvailable;
import domain.exceptions.RestaurantNotFound;
import domain.exceptions.UserNotFound;

import domain.manager.RestaurantManager;
import domain.manager.UserManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
public class RestaurantController {
    @RequestMapping(value = "/all_restaurants", method = RequestMethod.GET)
    public ArrayList<SingleRestaurantInfoResponse> getAllRestaurants( ) {
        System.out.println("return all restaurants from server");
        ArrayList<SingleRestaurantInfoResponse> result = new ArrayList<SingleRestaurantInfoResponse>();
        for(Restaurant restaurant: RestaurantManager.getInstance().getAllRestaurants()){
            result.add(new SingleRestaurantInfoResponse(restaurant ,
                    RestaurantManager.getInstance().estimateTime(restaurant.getId())));
        }
        return result;
    }

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    public  ArrayList<SingleRestaurantInfoResponse> getAvailableRestaurants(
            @RequestParam("username") String username)throws UserNotFound{
        ArrayList<SingleRestaurantInfoResponse> result = new ArrayList<SingleRestaurantInfoResponse>();
        System.out.println("return Available restaurants");
        for(Restaurant restaurant: RestaurantManager.getInstance().getAvailableRestaurants(UserManager.getUserByID(username))){
            result.add(new SingleRestaurantInfoResponse(restaurant,
                    RestaurantManager.getInstance().estimateTime(restaurant.getId())));
        }
        return result;
    }

    @RequestMapping(value = "/food_parties", method = RequestMethod.GET)
    public ArrayList<FoodPartyResponse> getAllFoodParties( ) {
        System.out.println("return all food parties from server");
        ArrayList<FoodPartyResponse> result = new ArrayList<FoodPartyResponse>();
        for(FoodParty foodParty: RestaurantManager.getInstance().getFoodParties()){
            for (MenuParty menu: foodParty.getMenus()){
                result.add(new FoodPartyResponse(foodParty.getRestaurantId() , foodParty.getRestaurantName(),
                        foodParty.getRestaurantLogo(), menu.getName(), menu.getDescription(), menu.getNewPrice(),
                        menu.getOldPrice(), menu.getUrlImage(), menu.getPopularity(), menu.getCount()));
            }
        }
        return result;
    }


    @RequestMapping(value = "/restaurants/{id}", method = RequestMethod.GET)
    public SingleRestaurantInfoResponse getRestaurant(@PathVariable(value = "id") String id)
            throws RestaurantNotFound , RestaurantNotAvailable {
        return new SingleRestaurantInfoResponse(RestaurantManager.getInstance().getRestaurantById(id) ,
                RestaurantManager.getInstance().estimateTime(id));
    }


    @RequestMapping(value = "/restaurants/foodParty_start_time", method = RequestMethod.GET)
    public Long getFoodPartyRemainingTime() {
        System.out.println("hereeeee in getting time");
        Long res = RestaurantManager.getInstance().getRemainingTimeFoodParty();
        System.out.println(res);
        return res;
    }


}
