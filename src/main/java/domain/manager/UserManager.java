package domain.manager;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.hash.Hashing;

import controller.user.responses.TokenResponse;
import dataAccess.dataMapper.orderMenu.OrderMenuMapper;
import dataAccess.dataMapper.orders.OrdersMapper;
import dataAccess.dataMapper.user.UserMapper;
import domain.FrontEntity.*;
import domain.databaseEntity.OrderMenuDAO;
import domain.databaseEntity.OrdersDAO;
import domain.databaseEntity.UserDAO;
import domain.entity.Menu;
import domain.entity.Restaurant;
import domain.exceptions.*;

import domain.entity.DeliveryStatus;
import domain.repositories.Loghmeh;
import domain.entity.User;
import services.Authentication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;




public class UserManager {
    private static UserManager instance;

    private UserManager() {
    }
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public  String login(String username, String password) throws LoginFailure , SQLException {
        password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        if (!dataAccess.dataMapper.user.UserMapper.getInstance().validateUser(username,password)) {
            throw new LoginFailure();
        }
        return Authentication.createToken(username);
    }

    public  void registerUser(UserDAO user) throws InvalidUser, UserAlreadyExists , SQLException {
        if(user.getUsername().equals("") || user.getFirstName().equals("") ||
                user.getLastName().equals("")
                || user.getEmail().equals("") || user.getPassword().equals(""))
            throw new InvalidUser();
        user.setPassword(
                Hashing.sha256().hashString(user.getPassword(), StandardCharsets.UTF_8)
                        .toString()
        );
        UserMapper.getInstance().registerUser(user);
    }

    public  SingleUserDTO getUserByID(String username) throws UserNotFound , SQLException {
        UserDAO userDAO = UserMapper.getInstance().getUserById(username);
        return new SingleUserDTO(userDAO.getUsername(), userDAO.getFirstName(), userDAO.getLastName(),
                userDAO.getEmail() , userDAO.getPhone() , userDAO.getCredit());
    }

    public  void addCredit(String username, Double amount)throws UserNotFound , SQLException{
        UserDAO user = UserMapper.getInstance().getUserById(username);
        UserMapper.getInstance().updateUserCredit(user.getUsername() , user.getCredit() + amount);
    }

    public void addFoodToCart(String username, String restaurantId,
            int menuId, String foodName, Double price, int foodCount)throws BuyFromOtherRestaurant ,SQLException , NoCurrOrder{
        if (this.checkUserStartChoosingFoodInCurrOrder(username)) {
            System.out.println("user " + username + " start new order");
            //add new tuple to orders table it means user start new order from restaurantId
            // the default state should be 1(not finalizing) and delivery is null
            OrdersMapper.getInstance().insert(new OrdersDAO(username,restaurantId));
            //after adding new tuple to orders table just need to add new tuple to order menus table
            int orderId = OrdersMapper.getInstance().findOrderIdOfUserCurrOrder(username);
            System.out.println("curr order id is " + Integer.toString(orderId));
            OrderMenuMapper.getInstance().insert(new OrderMenuDAO(orderId, menuId, foodName, price,foodCount, false));
            return;
        }
        else if(this.checkUserIsBuyFromOtherRestaurant(username, restaurantId)){
            System.out.println("user " + username + " has already food from other restaurant");
            throw new BuyFromOtherRestaurant();
        }
        else{
            // no need to add new tuple to orders table
            // just update order menu table if menuId
            int orderId = OrdersMapper.getInstance().findOrderIdOfUserCurrOrder(username);
            System.out.println("user not start order and restaurant is same" + Integer.toString(orderId));
            OrderMenuMapper.getInstance().InsertOrUpdateCountFood(orderId, menuId, foodName, price, foodCount, false);
            return;
        }
    }


    public boolean checkUserStartChoosingFoodInCurrOrder(String username) throws SQLException{
        return OrdersMapper.getInstance().checkUserStartChoosingFoodInCurrOrder(username);
    }

    public boolean checkUserIsBuyFromOtherRestaurant(String username , String restaurantId)
            throws NoCurrOrder , SQLException{
       String  RestaurantIdInCurrOrder = OrdersMapper.getInstance().getRestaurantIdForCurrOrderOfUser(username);
       return !RestaurantIdInCurrOrder.equals(restaurantId);
    }

    public void addFoodPartyToCart(String username, String restaurantId, int menuId,
                                   String foodName , Double price, int foodCount)
    throws SQLException, FoodNotInFoodParty, TimeValidationErrorFoodParty, BuyFromOtherRestaurant ,
            CountValidationErrorFoodParty , NoCurrOrder{
        boolean canChoose = RestaurantManager.getInstance().canChooseFoodParty(
                restaurantId, menuId, foodName, foodCount);
        if (canChoose){
            if (this.checkUserStartChoosingFoodInCurrOrder(username)) {
                System.out.println("user " + username + " start new order from food party");
                //add new tuple to orders table it means user start new order from restaurantId
                // the default state should be 1(not finalizing) and delivery is null
                OrdersMapper.getInstance().insert(new OrdersDAO(username,restaurantId));
                //after adding new tuple to orders table just need to add new tuple to order menus table
                int orderId = OrdersMapper.getInstance().findOrderIdOfUserCurrOrder(username);
                OrderMenuMapper.getInstance().insert(new OrderMenuDAO(orderId, menuId, foodName, price, foodCount, true));
                return;
            }
            else if(this.checkUserIsBuyFromOtherRestaurant(username, restaurantId)){
                System.out.println("user " + username + " has already food from other restaurant");
                throw new BuyFromOtherRestaurant();
            }
            else{
                // no need to add new tuple to orders table
                // just update order menu table if menuId
                int orderId = OrdersMapper.getInstance().findOrderIdOfUserCurrOrder(username);
                System.out.println("user not start order and restaurant is same" + Integer.toString(orderId));
                OrderMenuMapper.getInstance().InsertOrUpdateCountFood(orderId, menuId, foodName, price, foodCount, true);
                return;
            }
        }

    }


    public BuyBasketDTO getUserCurrBuyBasket(String username) throws SQLException{
        return UserMapper.getInstance().getUserCurrBuyBasket(username);
    }

    public AllUserOrdersDTO getUserAllOrders(String username) throws SQLException{
        ArrayList<SingleUserOrderDTO> orders = OrdersMapper.getInstance().getAllUserOrders(username);
        ArrayList<SingleUserOrderDTO> userOrders = new ArrayList<SingleUserOrderDTO>();
        for (SingleUserOrderDTO order: orders){
            ArrayList<FoodInBasketDTO> foods = OrderMenuMapper.getInstance().getFoodsOfOrder(order.getOrderId());
            int totalFood= 0;
            Double totalMoney = 0.0;
            for (FoodInBasketDTO f:foods){
                totalFood += f.getCountFood();
                totalMoney += f.getCountFood() * f.getFoodPrice();
            }
            userOrders.add(new SingleUserOrderDTO(order.getOrderId(),
                    order.getRestaurantId(), order.getRestaurantName(),
                    order.getStatus(), order.getTotalDeliveryTime(), order.getDeliverPersonId(), foods,totalFood, totalMoney ));
        }

        return new AllUserOrdersDTO(username,userOrders);
    }


    public void increaseCountFoodInUserCurrBuyBasket(String username, String foodName , int menuId , int currCount)
        throws SQLException , NoCurrOrder{
        int orderId = OrdersMapper.getInstance().findOrderIdOfUserCurrOrder(username);
        OrderMenuMapper.getInstance().increaseCountFood(orderId , menuId , foodName, currCount);
    }

    public void decreaseCountFoodInUserCurrBuyBasket(String username, String foodName , int menuId , int currCount)
            throws SQLException , NoCurrOrder{
        int orderId = OrdersMapper.getInstance().findOrderIdOfUserCurrOrder(username);
        if (currCount == 1){
            //here we should remove this menuId from list user
            //also check user has a order remove orderId row in Orders table
            OrderMenuMapper.getInstance().deleteFoodFromOrder(orderId, menuId, foodName);
            if (OrderMenuMapper.getInstance().orderIsEmpty(orderId)){
                //order is empty clean order id
                OrdersMapper.getInstance().deleteOrderForUser(orderId);
            }
        }
        else{
            OrderMenuMapper.getInstance().decreaseCountFood(orderId , menuId , foodName, currCount);
        }

    }


    public void finalizeOrder(String username) throws SQLException , NoCurrOrder ,
            UserNotFound , NotEnoughMoneyToBuy, TimeValidationErrorFoodParty, CountValidationErrorFoodParty{
        int orderId = OrdersMapper.getInstance().findOrderIdOfUserCurrOrder(username);
        Double userCredit = this.getUserCredit(username);
        Double moneyToPay = this.moneyUserShouldPayForCurrOrder(username);
        if (userCredit < moneyToPay) {
            throw new NotEnoughMoneyToBuy();
        }
        boolean foodPartyCheck = RestaurantManager.getInstance().CountAndTimeValidationForFoodPartyOfUserOrder(orderId);
        System.out.println("food party check:");
        System.out.println(foodPartyCheck);
        if (foodPartyCheck) {
            OrdersMapper.getInstance().changeStatusOfUserOrderToFindingDelivery(orderId);
            RestaurantManager.getInstance().updateCountFoodPartyWhenUserChoose(orderId);
            UserMapper.getInstance().updateUserCredit(username, userCredit - moneyToPay);
            // request and try to find delivery
            DeliveryManager.getInstance().deliveryUserOrder(orderId);
        }
    }



    public Double getUserCredit(String username)throws SQLException , UserNotFound{
        return UserMapper.getInstance().getUserCredit(username);
    }

    public Double moneyUserShouldPayForCurrOrder(String username) throws SQLException{
        BuyBasketDTO buyBasketDTO = UserMapper.getInstance().getUserCurrBuyBasket(username);
        System.out.println("total money user should pay is:");
        System.out.println(buyBasketDTO.getTotalFood());
        return buyBasketDTO.getTotalMoney();
    }


    public TokenResponse verifyGoogleIdToken(String idToken) throws  SQLException , UserNotFound , GoogleVerifierException{
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("917688244498-jte5pkllkdt43dc356ofu9umdsple4df.apps.googleusercontent.com"))
                .build();

        try {
            GoogleIdToken id = verifier.verify(idToken);
            if (id != null) {
                Payload payload = id.getPayload();
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);
                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
                System.out.println(email);
                System.out.println(name);
                System.out.println(familyName);
                SingleUserDTO userDTO = this.getUserByID(email);
                return new TokenResponse(Authentication.createToken(email),email);
            } else {
                System.out.println("Invalid ID token.");
                return null;
            }
        }catch (SQLException e){
            throw  new SQLException();
        }catch (UserNotFound e){
            throw new UserNotFound();
        } catch (Exception e){
            throw new GoogleVerifierException();
        }


    }



    


//    public static void  finalizeOrder(User user)
//            throws EmptyCartToFinalize, NotEnoughMoneyToBuy,
//            CountValidationErrorFoodParty, TimeValidationErrorFoodParty, RestaurantNotAvailable, RestaurantNotFound {
//        Double userCredit = user.getCredit();
//        boolean foodToBuy = user.foodToBuy();
//        Double moneyToPay = user.moneyToPayForOrder();
//
//        if (user.isChosenFoodParty() &&
//                !RestaurantManager.getInstance().foodPartyTimeValidationForFinalizing(user)) {
//            throw new TimeValidationErrorFoodParty();
//        }
//
//        if (!RestaurantManager.getInstance().foodPartyCountValidationForFinalizing(user) ){
//            throw  new CountValidationErrorFoodParty();
//        }
//        if (!foodToBuy) {
//            throw new EmptyCartToFinalize();
//        }
//        if (userCredit < moneyToPay) {
//            throw new NotEnoughMoneyToBuy();
//        }
//        System.out.println("User finalize order!");
//        user.getCurrentOrder().setStatus(DeliveryStatus.FINDING_DELIVERY); //change status of order to find delivery
//        System.out.println("here before " + user.getCurrentOrder().getRestaurantId());
//        DeliveryManager.getInstance().DeliveryUserOrder(user);
//        RestaurantManager.getInstance().changeCountOfPartyFoodUserBuy(user);
//        user.finalizeOrder(moneyToPay);
//        System.out.println("return from finalize order in userservice");
//
//
//    }

    public static ArrayList<User> getAllUsers(){
        return Loghmeh.getInstance().getUsers();
    }
}

