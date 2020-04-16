package services;

import com.google.common.hash.Hashing;

import domain.entity.Menu;
import domain.entity.Restaurant;
import domain.exceptions.*;

import domain.entity.DeliveryStatus;
import domain.repositories.Loghmeh;
import domain.entity.User;
import models.data.user.mapper.UserMapper;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UserService {

    public static String login(String username, String password) throws LoginFailure {
        System.out.println("Userservice login method validate user in database");
        password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

        //not validating in database
        if (!UserMapper.getInstance().validateUser(username,password)) {
            throw new LoginFailure();
        }
        return Authentication.createToken(username);
    }

    public static void registerUser(User user) throws InvalidUser, UserAlreadyExists {
        if(user.getUsername().equals("") || user.getUserFirstName().equals("") ||
                user.getUserLastName().equals("")
                || user.getEmail().equals("") || user.getUserPassword().equals(""))
            throw new InvalidUser();
        user.setUserPassword(
                Hashing.sha256().hashString(user.getUserPassword(), StandardCharsets.UTF_8)
                        .toString()
        );
        UserMapper.getInstance().registerNewUser(user);
    }

    public static User getUserByID(String userId) throws UserNotFound {
        return UserMapper.getInstance().getUserById(userId);
    }

    public static void addCredit(String username, Double amount)throws UserNotFound{
        User user = UserMapper.getInstance().getUserById(username);
        user.increaseCredit(amount);
    }


    public static void addFoodToCart(User user , Restaurant restaurant , Menu food , int count)throws BuyFromOtherRestaurant{
        if (user.startChoosingFood()) {
            user.addFoodToCart(food, restaurant.getName() ,restaurant.getId(), count);
            System.out.println("user add food " + food.getName() + " with price " + food.getPrice()+
                    " from restaurant " + restaurant.getName() +" successfully done!");
            return;
        }
        else if(user.isBuyFromOtherRestaurant(restaurant.getName())){
            System.out.println("You already have food from other restaurant " +
                    user.giveYourRestaurantYouBuy() + " in your food cart!");
            throw new BuyFromOtherRestaurant();
        }
        else{
            user.addFoodToCart(food,restaurant.getName() , restaurant.getId() , count);
            System.out.println("user add food " + food.getName() + " with price " + food.getPrice()+
                    " from restaurant " + restaurant.getName() +" successfully done!");
            return;
        }
    }

    public static void  finalizeOrder(User user)
            throws EmptyCartToFinalize, NotEnoughMoneyToBuy,
            CountValidationErrorFoodParty, TimeValidationErrorFoodParty, RestaurantNotAvailable, RestaurantNotFound {
        Double userCredit = user.getCredit();
        boolean foodToBuy = user.foodToBuy();
        Double moneyToPay = user.moneyToPayForOrder();

        if (user.isChosenFoodParty() &&
                !RestaurantService.getInstance().foodPartyTimeValidationForFinalizing(user)) {
            throw new TimeValidationErrorFoodParty();
        }

        if (!RestaurantService.getInstance().foodPartyCountValidationForFinalizing(user) ){
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
        DeliveryServices.getInstance().DeliveryUserOrder(user);
        RestaurantService.getInstance().changeCountOfPartyFoodUserBuy(user);
        user.finalizeOrder(moneyToPay);
        System.out.println("return from finalize order in userservice");


    }

    public static ArrayList<User> getAllUsers(){
        return Loghmeh.getInstance().getUsers();
    }
}

