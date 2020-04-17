package controller.restaurant;
import domain.FrontEntity.FoodPartyDTO;
import domain.FrontEntity.RestaurantMenuDTO;
import domain.FrontEntity.RestaurantInfoDTO;
import domain.entity.FoodParty;
import domain.entity.MenuParty;
import domain.exceptions.Message;
import domain.exceptions.RestaurantNotAvailable;
import domain.exceptions.RestaurantNotFound;

import domain.manager.RestaurantManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;


@RestController
public class RestaurantController {

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    public ResponseEntity<?> getAvailableRestaurants() throws SQLException {
        try {
            System.out.println("return Available restaurants");
            ArrayList<RestaurantInfoDTO> result = RestaurantManager.getInstance().getAvailableRestaurants();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام لود کردن رستوران های موجود");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }

    @RequestMapping(value = "/food_parties", method = RequestMethod.GET)
    public ArrayList<FoodPartyDTO> getFoodPartiesAvailable() {
        System.out.println("return all food parties from server");
        ArrayList<FoodPartyDTO> result = new ArrayList<FoodPartyDTO>();
        for(FoodParty foodParty: RestaurantManager.getInstance().getFoodParties()){
            for (MenuParty menu: foodParty.getMenus()){
                result.add(new FoodPartyDTO(foodParty.getRestaurantId() , foodParty.getRestaurantName(),
                        foodParty.getRestaurantLogo(), menu.getName(), menu.getDescription(), menu.getNewPrice(),
                        menu.getOldPrice(), menu.getUrlImage(), menu.getPopularity(), menu.getCount()));
            }
        }
        return result;
    }


    @RequestMapping(value = "/restaurants/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getRestaurant(@PathVariable(value = "id") String id)
            throws RestaurantNotFound , RestaurantNotAvailable {
        RestaurantMenuDTO restaurant = RestaurantManager.getInstance().getRestaurantById(id);
        return ResponseEntity.status(HttpStatus.OK).body(restaurant);
    }


    @RequestMapping(value = "/restaurants/foodParty_start_time", method = RequestMethod.GET)
    public Long getFoodPartyRemainingTime() {
        Long res = RestaurantManager.getInstance().getRemainingTimeFoodParty();
        return res;
    }


}
