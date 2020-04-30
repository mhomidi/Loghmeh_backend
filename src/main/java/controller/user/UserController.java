package controller.user;



import controller.user.requests.*;
import domain.FrontEntity.*;
import controller.user.responses.TokenResponse;
import domain.databaseEntity.UserDAO;
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
            String token =  UserManager.getInstance().login(request.getUsername(), request.getPassword());
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
            UserManager.getInstance().registerUser(
                    new UserDAO(request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPassword(),
                            request.getEmail(),
                            request.getPhone(), 0.0));

            String token = UserManager.getInstance().login(request.getEmail(), request.getPassword());
            return  ResponseEntity.status(HttpStatus.OK).body(new TokenResponse(token , request.getEmail()));
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
            return ResponseEntity.status(HttpStatus.OK).body(UserManager.getInstance().getUserByID(username));
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
            UserManager.getInstance().addCredit(username, Double.parseDouble(request.getMoney()));
            Message m = new Message("مبلغ با موفقیت به حساب کاربر اضافه شد");
            return ResponseEntity.status(HttpStatus.OK).body(m);
        } catch (UserNotFound e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (SQLException e) {
            Message m = new Message("خطای دیتابیس هنگام اضافه کردن مبلغ به حساب کاربری");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }

    @RequestMapping(value = "/users/{username}/add_cart", method = RequestMethod.POST)
    public ResponseEntity<?> AddToCart(@PathVariable(value = "username") String username,
                                       @RequestBody final AddToCartRequest request) {
        try {
            String restaurantId = request.getRestaurantId();
            int menuId = Integer.parseInt(request.getMenuId());
            String foodName = request.getFoodName();
            int foodCount = Integer.parseInt(request.getFoodCount());
            Double price = Double.parseDouble(request.getPrice());
            System.out.println("price is" + Double.toString(price) + " and food name is " + foodName);
            SingleUserDTO user = UserManager.getInstance().getUserByID(username);
            RestaurantInfoDTO restaurant = RestaurantManager.getInstance().getRestaurantById(restaurantId);
            MenuDTO food = RestaurantManager.getInstance().findMenuInRestaurantWithFoodNameAndMenuId(restaurantId,foodName,menuId);
            System.out.println(user  + "\n\n" + restaurant + "\n\n" + food);
            UserManager.getInstance().addFoodToCart(username, restaurantId, menuId, foodName, price, foodCount);
            Message m = new Message("غذا با موفقیت به سبد خرید اضافه شد");
            return ResponseEntity.status(HttpStatus.OK).body(m);
        } catch (UserNotFound e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (RestaurantNotFound e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (RestaurantNotAvailable e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (FoodNotExist e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (NoCurrOrder e){
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (BuyFromOtherRestaurant e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(m);
        }
        catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام اضافه کردن غذا به سبد خرید");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }


    @RequestMapping(value = "/users/{username}/add_foodParty_cart", method = RequestMethod.POST)
    public ResponseEntity<?> AddPartyCart(@PathVariable(value = "username") String username,
                          @RequestBody final AddToCartFoodPartyRequest request) {
        try {
            String restaurantId = request.getRestaurantId();
            int menuId = Integer.parseInt(request.getMenuId());
            String foodName = request.getFoodName();
            Double price = Double.parseDouble(request.getPrice());
            int foodCount = Integer.parseInt(request.getFoodCount());
            SingleUserDTO user = UserManager.getInstance().getUserByID(username);
            RestaurantInfoDTO restaurant = RestaurantManager.getInstance().getRestaurantById(restaurantId);
            UserManager.getInstance().addFoodPartyToCart(username, restaurantId, menuId, foodName, price, foodCount);
            Message m = new Message("فود پارتی با موفقیت به سبد خرید اضافه شد");
            return ResponseEntity.status(HttpStatus.OK).body(m);
        } catch (UserNotFound e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (SQLException e) {
            Message m = new Message("خطای دیتابیس هنگام افزودن فود پارتی به سبد خرید");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (RestaurantNotFound e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (RestaurantNotAvailable e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (FoodNotInFoodParty e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (TimeValidationErrorFoodParty e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (CountValidationErrorFoodParty e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (NoCurrOrder e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (BuyFromOtherRestaurant e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }


    @RequestMapping(value = "/users/{username}/show_cart", method = RequestMethod.GET)
    public  ResponseEntity<?> ShowCart(@PathVariable(value = "username") String username) {
        try{
            SingleUserDTO user = UserManager.getInstance().getUserByID(username);
            BuyBasketDTO buyBasketDTO = UserManager.getInstance().getUserCurrBuyBasket(username);
            return ResponseEntity.status(HttpStatus.OK).body(buyBasketDTO);
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام نمایش سبد خرید");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }



    @RequestMapping(value = "/users/{username}/increase", method = RequestMethod.POST)
    public ResponseEntity<?> increaseCountFood(@PathVariable(value = "username") String username ,
                                               @RequestBody final ModifyCountFoodRequest request) {
        try{
            String foodName = request.getFoodName();
            int menuId = Integer.parseInt(request.getMenuId());
            int currCount = Integer.parseInt(request.getCurrCount());
            SingleUserDTO user = UserManager.getInstance().getUserByID(username);
            UserManager.getInstance().increaseCountFoodInUserCurrBuyBasket(username, foodName, menuId , currCount);
            String success = "تعداد غذای"+ " " + foodName + " با موفقیت یکی زیاد شد";
            Message m = new Message(success);
            return  ResponseEntity.status(HttpStatus.OK).body(m);
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (NoCurrOrder e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }  catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام اضافه کردن تعداد غذا");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }

    @RequestMapping(value = "/users/{username}/decrease", method = RequestMethod.POST)
    public ResponseEntity<?> decreaseCountFood(@PathVariable(value = "username") String username ,
                                  @RequestBody final ModifyCountFoodRequest request) {
        try {
            String foodName = request.getFoodName();
            int menuId = Integer.parseInt(request.getMenuId());
            int currCount = Integer.parseInt(request.getCurrCount());
            SingleUserDTO user = UserManager.getInstance().getUserByID(username);
            UserManager.getInstance().decreaseCountFoodInUserCurrBuyBasket(username, foodName, menuId , currCount);
            String success = "تعداد غذای"+ " " + foodName + " با موفقیت یکی کم شد";
            Message m = new Message(success);
            return  ResponseEntity.status(HttpStatus.OK).body(m);
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (NoCurrOrder e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }  catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام کم کردن تعداد غذا");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }










    @RequestMapping(value = "/users/{username}/orders", method = RequestMethod.GET)
    public ResponseEntity<?>  getOrders(@PathVariable(value = "username") String username) {
        try {
            SingleUserDTO user = UserManager.getInstance().getUserByID(username);
            System.out.println("return all orders");
            AllUserOrdersDTO allUserOrdersDTO = UserManager.getInstance().getUserAllOrders(username);
            return  ResponseEntity.status(HttpStatus.OK).body(allUserOrdersDTO);
        }
        catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام نمایش لیست اردرها");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }


    @RequestMapping(value = "/users/{username}/finalize", method = RequestMethod.GET)
    public ResponseEntity<?> finalizeOrder(@PathVariable(value = "username") String username) {
        try {
            SingleUserDTO user = UserManager.getInstance().getUserByID(username);
            UserManager.getInstance().finalizeOrder(username);
            Message m = new Message("سفارش با موفقیت ثبت شد.آماده جهت ارسال پیک...");
            System.out.println(m.getMessage());
            return  ResponseEntity.status(HttpStatus.OK).body(m);

        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام نهایی کردن سفارش");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (NoCurrOrder e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (NotEnoughMoneyToBuy e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (TimeValidationErrorFoodParty e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (CountValidationErrorFoodParty e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }

    }










}
