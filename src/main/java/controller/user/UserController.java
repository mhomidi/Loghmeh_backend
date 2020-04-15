package controller.user;



import controller.user.requests.*;
import controller.user.responses.BuyBasketResponse;
import controller.user.responses.SingleUserInfoResponse;
import controller.user.responses.TokenResponse;
import controller.user.responses.UserOrdersResponse;
import domain.entity.FoodParty;
import domain.entity.Menu;
import domain.entity.MenuParty;
import domain.entity.Restaurant;
import domain.exceptions.*;

import domain.entity.User;
import services.RestaurantService;
import services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class UserController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try{
            String loginInfo ="Login:" + request.getUsername() + " " + request.getPassword();
            System.out.println(loginInfo);
            String token =  UserService.login(request.getUsername(), request.getPassword());
            loginInfo += " user found token is " + token;
            System.out.println(loginInfo);
            return  ResponseEntity.status(HttpStatus.OK).body(new TokenResponse(token,request.getUsername()));
        }catch (LoginFailure e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }


    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?>  signup(@RequestBody final SignupRequest request)  {
        try{
            String signupInfo ="sign up:" + request.getFirstName() + " " +request.getLastName() +
                    " " + request.getUsername() +" " + request.getPassword() +" " +
                    request.getEmail() +" " + request.getPhone();
            System.out.println(signupInfo);
            UserService.registerUser(
                    new User(request.getFirstName(),
                            request.getLastName(),
                            request.getUsername(),
                            request.getPassword(),
                            request.getPhone(),
                            request.getEmail()));

            String token = UserService.login(request.getUsername(), request.getPassword());
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
        }

    }

    @RequestMapping(value = "/users/{username}/add_credit", method = RequestMethod.POST)
    public ResponseEntity<?> AddCredit(@PathVariable(value = "username") String username ,
            @RequestBody final CreditRequest request){
        System.out.println("user name want to add money: " + username +" " +
                request.getMoney());
        try {
            UserService.addCredit(username,Double.parseDouble(request.getMoney()));
            Message m = new Message("مبلغ با موفقیت به حساب کاربر اضافه شد");
            return  ResponseEntity.status(HttpStatus.OK).body(m);
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<?>  getUser(@PathVariable(value = "username") String username) throws UserNotFound{
       // SingleUserInfoResponse
        System.out.println("server return user info of " + username);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(
                    new SingleUserInfoResponse(UserService.getUserByID(username)));
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }

    }

    @RequestMapping(value = "/users/{username}/add_cart", method = RequestMethod.POST)
    public ResponseEntity<?> AddCredit(@PathVariable(value = "username") String username,
                                       @RequestBody final AddToCartRequest request) {
        try {
            int count = Integer.parseInt(request.getNumFood());
            String foodName = request.getFoodName();
            String restaurantId = request.getRestaurantId();
            String info = "user " + username +" want to add " + Integer.toString(count) + " " +
                    foodName + " from restaurant " + restaurantId;
            System.out.println(info);
            User user = UserService.getUserByID(username);
            Restaurant restaurant = RestaurantService.getInstance().getRestaurantById(restaurantId);
            String restaurantName = restaurant.getName();
            Menu food = RestaurantService.getInstance().findMenuInRestaurantWithFoodName(restaurant , foodName);
            UserService.addFoodToCart(user,restaurant,food,count);
        }catch (BuyFromOtherRestaurant e){
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(m);
        }
        catch (FoodNotExist e){
            Message m = new Message(e.getMessage());
            return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        catch (RestaurantNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        catch (RestaurantNotAvailable e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        Message m = new Message("غذا با موفقیت به سبد خرید اضافه شد");
        return  ResponseEntity.status(HttpStatus.OK).body(m);
    }





    @RequestMapping(value = "/users/{username}/add_foodParty_cart", method = RequestMethod.POST)
    public ResponseEntity<?> AddPartyCredit(@PathVariable(value = "username") String username,
                          @RequestBody final AddToCartFoodPartyRequest request) {
        try {
            int count = Integer.parseInt(request.getCountFood());
            String foodName = request.getFoodName();
            String restaurantId = request.getRestaurantId();
            String info = "user " + username + " want to add " + Integer.toString(count) + " " +
                    foodName + " from restaurant " + restaurantId + " food party!!";
            System.out.println(info);
            User user = UserService.getUserByID(username);
            Restaurant restaurant = RestaurantService.getInstance().getRestaurantWithId(restaurantId);
            FoodParty foodParty = RestaurantService.getInstance().findRestaurantInFoodParty(restaurantId);

            if (foodParty == null) {
                Message m = new Message("غذای مورد نظر در رستوران به عنوان فود پارتی یافت نشد");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
            }
            MenuParty menu = foodParty.findMenuOfFood(foodName);
            if (menu == null) {
                Message m = new Message("غذای مورد نظر در رستوران به عنوان فود پارتی یافت نشد");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
            }
            if (!foodParty.timeAvailable()) {
                Message m = new Message("اتمام زمان لازم جهت سفارش غذای فود پارتی ");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
            }

            if (!menu.canChooseThisMenu(count)) {
                Message m = new Message("اتمام موجودی غذا جهت سفارش غذای فود پارتی ");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
            }

            if (user.startChoosingFood()) {
                user.addPartyFoodToCart(menu, restaurant.getName(), restaurantId , count);
                System.out.println("user add food party " + foodName + " with count " + count +" with new price " + menu.getNewPrice() +
                        " from restaurant " + restaurant.getName() + " successfully done!");
                Message m = new Message("فود پارتی با موفقیت به سبد شما اضافه شد");
                return ResponseEntity.status(HttpStatus.OK).body(m);

            }
            else if (user.isBuyFromOtherRestaurant(restaurant.getName())) {
                Message m = new Message("شما در حال حاضر سفارش ثبت نشده ای از رستوران دیگری دارید");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(m);
            }
            else {
                user.addPartyFoodToCart(menu, restaurant.getName(), restaurantId , count);
                System.out.println("user add food " + foodName + " with count " + count +" with new price " + menu.getNewPrice() +
                        " from restaurant " + restaurant.getName() + " successfully done!");
                Message m = new Message("فود پارتی با موفقیت به سبد شما اضافه شد");
                return ResponseEntity.status(HttpStatus.OK).body(m);
            }
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }



    @RequestMapping(value = "/users/{username}/show_cart", method = RequestMethod.GET)
    public  ResponseEntity<?> ShowCart(@PathVariable(value = "username") String username)
            throws UserNotFound {
        String info ="user " + username + "wants to show his/her curr order";
        System.out.println(info);
        try{
            User user = UserService.getUserByID(username);
            return ResponseEntity.status(HttpStatus.OK).body(new BuyBasketResponse(user));
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }


    @RequestMapping(value = "/users/{username}/increase", method = RequestMethod.POST)
    public ResponseEntity<?> increaseCountFood(@PathVariable(value = "username") String username ,
                                  @RequestBody final ModifyCountFoodRequest request) {
        try{
            String foodName = request.getFoodName();
            String info ="user " + username + "wants to  increase count of food " + foodName;
            System.out.println(info);
            User user = UserService.getUserByID(username);
            user.ModifyCountFoodInCurrOrder(foodName , 1);
            String success = "تعداد غذای"+ " " + foodName + " با موفقیت یکی زیاد شد";
            System.out.println(success);
            Message m = new Message(success);
            return  ResponseEntity.status(HttpStatus.OK).body(m);
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (FoodNotExist e) {
            Message m = new Message(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }


    @RequestMapping(value = "/users/{username}/decrease", method = RequestMethod.POST)
    public ResponseEntity<?> decreaseCountFood(@PathVariable(value = "username") String username ,
                                  @RequestBody final ModifyCountFoodRequest request) {
        try {
            String foodName = request.getFoodName();
            String info ="user " + username + "wants to  decrease count of food " + foodName;
            System.out.println(info);
            User user = UserService.getUserByID(username);
            user.ModifyCountFoodInCurrOrder(foodName,-1);
            String success = "تعداد غذای"+ " " + foodName + " با موفقیت یکی کم شد";
            System.out.println(success);
            Message m = new Message(success);
            return  ResponseEntity.status(HttpStatus.OK).body(m);
        }catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }catch (FoodNotExist e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }

    }



    @RequestMapping(value = "/users/{username}/finalize", method = RequestMethod.GET)
    public ResponseEntity<?> finalizeOrder(@PathVariable(value = "username") String username) {
        try {
            String info = "user " + username + " wants to finalize his/her order";
            System.out.println(info);
            User user = UserService.getUserByID(username);
            UserService.finalizeOrder(user);
        }
        catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);

        }catch (NotEnoughMoneyToBuy e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
        catch (EmptyCartToFinalize e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
        catch (CountValidationErrorFoodParty e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
        catch (TimeValidationErrorFoodParty e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
        catch (RestaurantNotAvailable e) {
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
        catch (RestaurantNotFound e) {
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
        Message m = new Message("سفارش با موفقیت ثبت شد.آماده جهت ارسال پیک...");
        System.out.println(m.getMessage());
        return  ResponseEntity.status(HttpStatus.OK).body(m);
    }




    @RequestMapping(value = "/users/{username}/orders", method = RequestMethod.GET)
    public ResponseEntity<?>  getOrders(@PathVariable(value = "username") String username)
            throws UserNotFound {
        try {
            String info = "user " + username + " wants to get all orders info his/her order";
            System.out.println(info);
            User user = UserService.getUserByID(username);
            System.out.println(user);
            return  ResponseEntity.status(HttpStatus.OK).body(new UserOrdersResponse(user));
        }
        catch (UserNotFound e){
            Message m = new Message(e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
        }
    }









}
