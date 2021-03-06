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

    @RequestMapping(value = "/googleLogin", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody GoogleLoginRequest request) {
        System.out.println(request.getIdToken());
        try {
            TokenResponse tokenResponse = UserManager.getInstance().verifyGoogleIdToken(request.getIdToken());
            if(tokenResponse==null){
                Message m = new Message("خطای احراز هویت گوگل");
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
            }
            else {
                return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
            }
        }catch (SQLException e){
            Message m = new Message("خطای دیتابیس هنگام احراز هویت گوگل");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (GoogleVerifierException e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }





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
            System.out.println("login failure");
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
            System.out.println(request.getEmail());
            System.out.println(request.getPassword());
            System.out.println(request.getFirstName());
            System.out.println(request.getLastName());
            UserManager.getInstance().registerUser(
                    new UserDAO(request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPassword(),
                            request.getEmail(),
                            request.getPhone(),
                            0.0));

            String token = UserManager.getInstance().login(request.getEmail(), request.getPassword());
            System.out.println(token);
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

    @RequestMapping(value = "/users/user_info", method = RequestMethod.GET)
    public ResponseEntity<?>  getUser(@RequestAttribute("id") String username ) {
        try {
            System.out.println("login user is " + username );
            System.out.println(username);
            return ResponseEntity.status(HttpStatus.OK).body(UserManager.getInstance().getUserByID(username));
        } catch (UserNotFound e) {
            Message m = new Message(e.getMessage());
            System.out.println(m.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        } catch (SQLException e) {
            Message m = new Message("خطای دیتابیس هنگام ورود به صفحه ی کاربری");
            System.out.println(m.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }

    @RequestMapping(value = "/users/add_credit", method = RequestMethod.POST)
    public ResponseEntity<?> AddCredit(@RequestAttribute("id") String username,
            @RequestBody final CreditRequest request) {
        try {
            System.out.println("login user is " + username );
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

    @RequestMapping(value = "/users/add_cart", method = RequestMethod.POST)
    public ResponseEntity<?> AddToCart(@RequestAttribute("id") String username,
                                       @RequestBody final AddToCartRequest request) {
        try {
            System.out.println("login user is " + username );
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


    @RequestMapping(value = "/users/add_foodParty_cart", method = RequestMethod.POST)
    public ResponseEntity<?> AddPartyCart(@RequestAttribute("id") String username,
                          @RequestBody final AddToCartFoodPartyRequest request) {
        try {
            System.out.println("login user is " + username );
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


    @RequestMapping(value = "/users/show_cart", method = RequestMethod.GET)
    public  ResponseEntity<?> ShowCart(@RequestAttribute("id") String username) {
        try{
            System.out.println("login user is " + username );
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



    @RequestMapping(value = "/users/increase", method = RequestMethod.POST)
    public ResponseEntity<?> increaseCountFood(@RequestAttribute("id") String username,
                                               @RequestBody final ModifyCountFoodRequest request) {
        try{
            System.out.println("login user is " + username );
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

    @RequestMapping(value = "/users/decrease", method = RequestMethod.POST)
    public ResponseEntity<?> decreaseCountFood(@RequestAttribute("id") String username,
                                  @RequestBody final ModifyCountFoodRequest request) {
        try {
            System.out.println("login user is " + username );
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




    @RequestMapping(value = "/users/orders", method = RequestMethod.GET)
    public ResponseEntity<?>  getOrders(@RequestAttribute("id") String username) {
        try {
            System.out.println("login user is " + username );
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


    @RequestMapping(value = "/users/finalize", method = RequestMethod.GET)
    public ResponseEntity<?> finalizeOrder(@RequestAttribute("id") String username) {
        try {
            System.out.println("login user is " + username );
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
