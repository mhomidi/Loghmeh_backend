package domain.manager;

import com.google.common.hash.Hashing;

import dataAccess.dataMapper.orderMenu.OrderMenuMapper;
import dataAccess.dataMapper.orders.OrdersMapper;
import dataAccess.dataMapper.user.UserMapper;
import domain.FrontEntity.AllUserOrdersDTO;
import domain.FrontEntity.BuyBasketDTO;
import domain.FrontEntity.SingleUserDTO;
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


import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;

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
        dataAccess.dataMapper.user.UserMapper.getInstance().registerUser(user);
    }

    public  SingleUserDTO getUserByID(String username) throws UserNotFound , SQLException {
        UserDAO userDAO = dataAccess.dataMapper.user.UserMapper.getInstance().getUserById(username);
        return new SingleUserDTO(userDAO.getUsername(), userDAO.getFirstName(), userDAO.getLastName(),
                userDAO.getEmail() , userDAO.getPhone() , userDAO.getCredit());
    }

    public  void addCredit(String username, Double amount)throws UserNotFound , SQLException{
        UserDAO user = UserMapper.getInstance().getUserById(username);
        UserMapper.getInstance().updateUserCredit(user.getUsername() , user.getCredit() + amount);
    }

    public void addFoodToCart(String username, String restaurantId,
            int menuId, String foodName, int foodCount)throws BuyFromOtherRestaurant ,SQLException , NoCurrOrder{
        if (this.checkUserStartChoosingFoodInCurrOrder(username)) {
            System.out.println("user " + username + " start new order");
            //add new tuple to orders table it means user start new order from restaurantId
            // the default state should be 1(not finalizing) and delivery is null
            OrdersMapper.getInstance().insert(new OrdersDAO(username,restaurantId));
            //after adding new tuple to orders table just need to add new tuple to order menus table
            int orderId = OrdersMapper.getInstance().findOrderIdOfUserCurrOrder(username);
            OrderMenuMapper.getInstance().insert(new OrderMenuDAO(orderId, menuId, foodCount, false));
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
            OrderMenuMapper.getInstance().InsertOrUpdateCountFood(orderId, menuId, foodCount, false);
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

    public void addFoodPartyToCart(String username, String restaurantId, int menuId, String foodName , int foodCount)
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
                OrderMenuMapper.getInstance().insert(new OrderMenuDAO(orderId, menuId, foodCount, true));
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
                OrderMenuMapper.getInstance().InsertOrUpdateCountFood(orderId, menuId, foodCount, true);
                return;
            }
        }

    }



    public BuyBasketDTO getUserCurrBuyBasket(String username) throws SQLException{
        return null;
    }


    public AllUserOrdersDTO getUserAllOrders(String username) throws SQLException{
        return null;
    }





    


    public static void  finalizeOrder(User user)
            throws EmptyCartToFinalize, NotEnoughMoneyToBuy,
            CountValidationErrorFoodParty, TimeValidationErrorFoodParty, RestaurantNotAvailable, RestaurantNotFound {
        Double userCredit = user.getCredit();
        boolean foodToBuy = user.foodToBuy();
        Double moneyToPay = user.moneyToPayForOrder();

        if (user.isChosenFoodParty() &&
                !RestaurantManager.getInstance().foodPartyTimeValidationForFinalizing(user)) {
            throw new TimeValidationErrorFoodParty();
        }

        if (!RestaurantManager.getInstance().foodPartyCountValidationForFinalizing(user) ){
            throw  new CountValidationErrorFoodParty();
        }
        if (!foodToBuy) {
            throw new EmptyCartToFinalize();
        }
        if (userCredit < moneyToPay) {
            throw new NotEnoughMoneyToBuy();
        }
        System.out.println("User finalize order!");
        user.getCurrentOrder().setStatus(DeliveryStatus.FINDING_DELIVERY); //change status of order to find delivery
        System.out.println("here before " + user.getCurrentOrder().getRestaurantId());
        DeliveryManager.getInstance().DeliveryUserOrder(user);
        RestaurantManager.getInstance().changeCountOfPartyFoodUserBuy(user);
        user.finalizeOrder(moneyToPay);
        System.out.println("return from finalize order in userservice");


    }

    public static ArrayList<User> getAllUsers(){
        return Loghmeh.getInstance().getUsers();
    }
}

