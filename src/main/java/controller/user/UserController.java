package controller.user;



import controller.user.requests.*;
import controller.user.responses.BuyBasketResponse;
import domain.FrontEntity.SingleUserDTO;
import controller.user.responses.TokenResponse;
import controller.user.responses.UserOrdersResponse;
import domain.databaseEntity.UserDAO;
import domain.entity.FoodParty;
import domain.entity.Menu;
import domain.entity.MenuParty;
import domain.entity.Restaurant;
import domain.exceptions.*;

import domain.entity.User;
import domain.manager.RestaurantManager;
import domain.manager.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;


@RestController
public class UserController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try{
            String loginInfo ="Login:" + request.getUsername() + " " + request.getPassword();
            System.out.println(loginInfo);
            String token =  UserManager.login(request.getUsername(), request.getPassword());
            loginInfo += " user found token is " + token;
            System.out.println(loginInfo);
            return  ResponseEntity.status(HttpStatus.OK).body(new TokenResponse(token,request.getUsername()));
        }catch (LoginFailure e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام ورود به صفحه ی کاربری");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody final SignupRequest request)  {
        try{
            System.out.println(request.getPhone());
            UserManager.registerUser(
                    new UserDAO(request.getFirstName(),
                            request.getLastName(),
                            request.getUsername(),
                            request.getPassword(),
                            request.getEmail(),
                            request.getPhone(), 0.0));

            String token = UserManager.login(request.getUsername(), request.getPassword());
            return  ResponseEntity.status(HttpStatus.OK).body(new TokenResponse(token , request.getUsername()));
        }catch (UserAlreadyExists e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (InvalidUser e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (LoginFailure e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام ورود به صفحه ی کاربری");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }

    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<?>  getUser(@PathVariable(value = "username") String username) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(UserManager.getUserByID(username));
        } catch (UserNotFound e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (SQLException e) {
            Message m = new Message("خطای دیتابیس هنگام ورود به صفحه ی کاربری");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }

    @RequestMapping(value = "/users/{username}/add_credit", method = RequestMethod.POST)
    public ResponseEntity<?> AddCredit(@PathVariable(value = "username") String username ,
            @RequestBody final CreditRequest request) {
        try {
            UserManager.addCredit(username, Double.parseDouble(request.getMoney()));
            Message m = new Message("مبلغ با موفقیت به حساب کاربر اضافه شد");
            return ResponseEntity.status(HttpStatus.OK).body(m);
        } catch (UserNotFound e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SQLException e) {
            Message m = new Message("خطای دیتابیس هنگام اضافه کردن مبلغ به حساب کاربری");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }











    @RequestMapping(value = "/users/{username}/add_cart", method = RequestMethod.POST)
    public ResponseEntity<?> AddCredit(@PathVariable(value = "username") String username,
                                       @RequestBody final AddToCartRequest request) {
//        try {
//            int count = Integer.parseInt(request.getNumFood());
//            String foodName = request.getFoodName();
//            String restaurantId = request.getRestaurantId();
//            String info = "user " + username +" want to add " + Integer.toString(count) + " " +
//                    foodName + " from restaurant " + restaurantId;
//            System.out.println(info);
//            User user = UserManager.getUserByID(username);
//            Restaurant restaurant = RestaurantManager.getInstance().getRestaurantById(restaurantId);
//            String restaurantName = restaurant.getName();
//            Menu food = RestaurantManager.getInstance().findMenuInRestaurantWithFoodName(restaurant , foodName);
//            UserManager.addFoodToCart(user,restaurant,food,count);
//        }catch (BuyFromOtherRestaurant e){
//            Message m = new Message(e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(m);
//        }
//        catch (FoodNotExist e){
//            Message m = new Message(e.getMessage());
//            return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//
//        catch (RestaurantNotFound e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//
//        catch (RestaurantNotAvailable e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        catch (UserNotFound e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
        Message m = new Message("غذا با موفقیت به سبد خرید اضافه شد");
        return  ResponseEntity.status(HttpStatus.OK).body(m);
    }

    @RequestMapping(value = "/users/{username}/add_foodParty_cart", method = RequestMethod.POST)
    public ResponseEntity<?> AddPartyCredit(@PathVariable(value = "username") String username,
                          @RequestBody final AddToCartFoodPartyRequest request) {
        Message m = new Message("غذا با موفقیت به سبد خرید اضافه شد");
        return  ResponseEntity.status(HttpStatus.OK).body(m);
//        try {
//            int count = Integer.parseInt(request.getCountFood());
//            String foodName = request.getFoodName();
//            String restaurantId = request.getRestaurantId();
//            String info = "user " + username + " want to add " + Integer.toString(count) + " " +
//                    foodName + " from restaurant " + restaurantId + " food party!!";
//            System.out.println(info);
//            User user = UserManager.getUserByID(username);
//            Restaurant restaurant = RestaurantManager.getInstance().getRestaurantWithId(restaurantId);
//            FoodParty foodParty = RestaurantManager.getInstance().findRestaurantInFoodParty(restaurantId);
//
//            if (foodParty == null) {
//                Message m = new Message("غذای مورد نظر در رستوران به عنوان فود پارتی یافت نشد");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//            }
//            MenuParty menu = foodParty.findMenuOfFood(foodName);
//            if (menu == null) {
//                Message m = new Message("غذای مورد نظر در رستوران به عنوان فود پارتی یافت نشد");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//            }
//            if (!foodParty.timeAvailable()) {
//                Message m = new Message("اتمام زمان لازم جهت سفارش غذای فود پارتی ");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//            }
//
//            if (!menu.canChooseThisMenu(count)) {
//                Message m = new Message("اتمام موجودی غذا جهت سفارش غذای فود پارتی ");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//            }
//
//            if (user.startChoosingFood()) {
//                user.addPartyFoodToCart(menu, restaurant.getName(), restaurantId , count);
//                System.out.println("user add food party " + foodName + " with count " + count +" with new price " + menu.getNewPrice() +
//                        " from restaurant " + restaurant.getName() + " successfully done!");
//                Message m = new Message("فود پارتی با موفقیت به سبد شما اضافه شد");
//                return ResponseEntity.status(HttpStatus.OK).body(m);
//
//            }
//            else if (user.isBuyFromOtherRestaurant(restaurant.getName())) {
//                Message m = new Message("شما در حال حاضر سفارش ثبت نشده ای از رستوران دیگری دارید");
//                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(m);
//            }
//            else {
//                user.addPartyFoodToCart(menu, restaurant.getName(), restaurantId , count);
//                System.out.println("user add food " + foodName + " with count " + count +" with new price " + menu.getNewPrice() +
//                        " from restaurant " + restaurant.getName() + " successfully done!");
//                Message m = new Message("فود پارتی با موفقیت به سبد شما اضافه شد");
//                return ResponseEntity.status(HttpStatus.OK).body(m);
//            }
//        }catch (UserNotFound e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }

    }

    @RequestMapping(value = "/users/{username}/show_cart", method = RequestMethod.GET)
    public  ResponseEntity<?> ShowCart(@PathVariable(value = "username") String username)
            throws UserNotFound {
        Message m = new Message("غذا با موفقیت به سبد خرید اضافه شد");
        return  ResponseEntity.status(HttpStatus.OK).body(m);
//        String info ="user " + username + "wants to show his/her curr order";
//        System.out.println(info);
//        try{
//            User user = UserManager.getUserByID(username);
//            return ResponseEntity.status(HttpStatus.OK).body(new BuyBasketResponse(user));
//        }catch (UserNotFound e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
    }

    @RequestMapping(value = "/users/{username}/increase", method = RequestMethod.POST)
    public ResponseEntity<?> increaseCountFood(@PathVariable(value = "username") String username ,
                                  @RequestBody final ModifyCountFoodRequest request) {
        Message m = new Message("غذا با موفقیت به سبد خرید اضافه شد");
        return  ResponseEntity.status(HttpStatus.OK).body(m);
//        try{
//            String foodName = request.getFoodName();
//            String info ="user " + username + "wants to  increase count of food " + foodName;
//            System.out.println(info);
//            User user = UserManager.getUserByID(username);
//            user.ModifyCountFoodInCurrOrder(foodName , 1);
//            String success = "تعداد غذای"+ " " + foodName + " با موفقیت یکی زیاد شد";
//            System.out.println(success);
//            Message m = new Message(success);
//            return  ResponseEntity.status(HttpStatus.OK).body(m);
//        }catch (UserNotFound e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }catch (FoodNotExist e) {
//            Message m = new Message(e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
    }

    @RequestMapping(value = "/users/{username}/decrease", method = RequestMethod.POST)
    public ResponseEntity<?> decreaseCountFood(@PathVariable(value = "username") String username ,
                                  @RequestBody final ModifyCountFoodRequest request) {
        Message m = new Message("غذا با موفقیت به سبد خرید اضافه شد");
        return  ResponseEntity.status(HttpStatus.OK).body(m);
//        try {
//            String foodName = request.getFoodName();
//            String info ="user " + username + "wants to  decrease count of food " + foodName;
//            System.out.println(info);
//            User user = UserManager.getUserByID(username);
//            user.ModifyCountFoodInCurrOrder(foodName,-1);
//            String success = "تعداد غذای"+ " " + foodName + " با موفقیت یکی کم شد";
//            System.out.println(success);
//            Message m = new Message(success);
//            return  ResponseEntity.status(HttpStatus.OK).body(m);
//        }catch (UserNotFound e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }catch (FoodNotExist e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }

    }

    @RequestMapping(value = "/users/{username}/finalize", method = RequestMethod.GET)
    public ResponseEntity<?> finalizeOrder(@PathVariable(value = "username") String username) {
//        try {
//            String info = "user " + username + " wants to finalize his/her order";
//            System.out.println(info);
//            User user = UserManager.getUserByID(username);
//            UserManager.finalizeOrder(user);
//        }
//        catch (UserNotFound e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//
//        }catch (NotEnoughMoneyToBuy e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
//        catch (EmptyCartToFinalize e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
//        catch (CountValidationErrorFoodParty e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
//        catch (TimeValidationErrorFoodParty e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
//        catch (RestaurantNotAvailable e) {
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
//        catch (RestaurantNotFound e) {
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
        Message m = new Message("سفارش با موفقیت ثبت شد.آماده جهت ارسال پیک...");
        System.out.println(m.getMessage());
        return  ResponseEntity.status(HttpStatus.OK).body(m);
    }


    @RequestMapping(value = "/users/{username}/orders", method = RequestMethod.GET)
    public ResponseEntity<?>  getOrders(@PathVariable(value = "username") String username)
            throws UserNotFound {
        Message m = new Message("غذا با موفقیت به سبد خرید اضافه شد");
        return  ResponseEntity.status(HttpStatus.OK).body(m);
//        try {
//            String info = "user " + username + " wants to get all orders info his/her order";
//            System.out.println(info);
//            User user = UserManager.getUserByID(username);
//            System.out.println(user);
//            return  ResponseEntity.status(HttpStatus.OK).body(new UserOrdersResponse(user));
//        }
//        catch (UserNotFound e){
//            Message m = new Message(e.getMessage());
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
    }









}
