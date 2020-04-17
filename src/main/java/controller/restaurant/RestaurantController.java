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
import java.util.List;


@RestController
public class RestaurantController {

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    public ResponseEntity<?> getAvailableRestaurants(
            @RequestParam(value = "page", defaultValue = "-1") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchFood", defaultValue = "") String searchFoodKey,
            @RequestParam(value = "searchRestaurant", defaultValue = "") String searchRestaurantKey
    ){
        try {
            System.out.println("return Available restaurants");
            System.out.println(pageNumber);
            System.out.println(size);
            System.out.println(searchFoodKey);
            System.out.println(searchRestaurantKey);
            ArrayList<RestaurantInfoDTO> result = RestaurantManager.getInstance().getAvailableRestaurants(
                    pageNumber, size, searchFoodKey, searchRestaurantKey
            );
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام لود کردن رستوران های موجود");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }




    @RequestMapping(value = "/food_parties", method = RequestMethod.GET)
    public ResponseEntity<?> getFoodPartiesAvailable() {
        try {
            System.out.println("return available food parties");
            ArrayList<FoodPartyDTO> result = RestaurantManager.getInstance().getAvailableFoodParties();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام لود کردن فود پارتی های موجود");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }

    }

    @RequestMapping(value = "/restaurants/foodParty_start_time", method = RequestMethod.GET)
    public Long getFoodPartyRemainingTime() {
        Long res = RestaurantManager.getInstance().getRemainingTimeFoodParty();
        return res;
    }



    @RequestMapping(value = "/restaurants/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getRestaurant(@PathVariable(value = "id") String id) {
        try {
            RestaurantMenuDTO restaurant = RestaurantManager.getInstance().getRestaurantWithMenusById(id);
            return ResponseEntity.status(HttpStatus.OK).body(restaurant);
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام لود کردن فود صفحه ی رستوران");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (RestaurantNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (RestaurantNotAvailable e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }

}
