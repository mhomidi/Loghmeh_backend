package domain.entity;


import domain.exceptions.FoodNotExist;


import java.util.ArrayList;
import java.util.Map;

public class User {
    private String userFirstName;
    private String userLastName;
    private String username;
    private String userPassword;
    private String email;
    private String phoneNumber;
    private Double credit;
    private Order currentOrder;
    private String chosenRestaurantNameToBuy;
    private ArrayList<Order> allOrders;
    private int currentOrderId;
    private ArrayList<Order> notFoundDelivery;


    public User(String name , String family , String username , String password,String phone , String email){
        this.userFirstName = name;
        this.userLastName = family;
        this.username = username;
        this.userPassword = password;
        this.email = email;
        this.phoneNumber = phone;
        this.credit = 0.0;
        this.currentOrder = new Order();
        this.allOrders = new ArrayList<Order>();
        this.currentOrderId = 0 ;
        this.chosenRestaurantNameToBuy = null;
        this.notFoundDelivery = new ArrayList<Order>();
    }



    public void ModifyCountFoodInCurrOrder(String foodName , int op)throws FoodNotExist {
        Order curr = this.getCurrentOrder();
        boolean find = false;
        for (Map.Entry<Menu,Integer> entry : curr.getMenus().entrySet()){
            if(entry.getKey().getName().equals(foodName)){
                if(op==1) {
                    curr.getMenus().replace(entry.getKey(), entry.getValue() + 1);
                }
                else if(op==-1){
                    if(entry.getValue()==1){
                        curr.getMenus().remove(entry.getKey());

                    }
                    else {
                        curr.getMenus().replace(entry.getKey(), entry.getValue() - 1);
                    }
                }
                find = true;
                if(curr.getMenus().size() + curr.getPartyMenus().size() == 0){
                    this.getAllOrders().remove(curr);
                    this.removingOrder();
                }
                return;
            }
        }
        for (Map.Entry<MenuParty,Integer> entry : curr.getPartyMenus().entrySet()){
            if(entry.getKey().getName().equals(foodName)){
                if(op==1) {
                    curr.getPartyMenus().replace(entry.getKey(), entry.getValue() + 1);
                }
                else if(op==-1){
                    if(entry.getValue()==1) {
                        curr.getPartyMenus().remove(entry.getKey());
                    }
                    else{
                        curr.getPartyMenus().replace(entry.getKey(), entry.getValue() - 1);
                    }
                }
                find = true;
                if(curr.getMenus().size() + curr.getPartyMenus().size() == 0){
                    this.getAllOrders().remove(curr);
                    this.removingOrder();
                }
                return;
            }
        }
        if(!find){
            throw new FoodNotExist();
        }


    }





    public void setUserPassword(String password){
        this.userPassword = password;
    }




    public String getUserFirstName(){
        return this.userFirstName;
    }

    public String getUserLastName(){
        return this.userLastName;
    }

    public String getFullName() {
        return this.getUserFirstName() + " " + this.getUserLastName();
    }

    public  String getUsername(){ return this.username; }

    public String getUserPassword(){
        return  this.userPassword;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public String getEmail(){
        return this.email;
    }

    public Double getCredit(){
        return this.credit;
    }

    public void increaseCredit(Double increaseAmount){
        System.out.println("User add " + Double.toString(increaseAmount) + "to his/her credit!");
        this.credit+= increaseAmount;
    }


    public boolean isChosenFoodParty(){
        return !this.currentOrder.getPartyMenus().isEmpty();
    }


    public void removeOrderFromNotFoundDelivery(Order order){
        this.notFoundDelivery.remove(order);
    }

    public ArrayList<Order> getNotFoundDelivery(){
        return this.notFoundDelivery;
    }


    public void addCurrentOrderToNotFoundDeliveryList(Order currentOrder){
        System.out.println("add user order to not found delivery list");
        System.out.println(currentOrder.getRestaurantId());
        notFoundDelivery.add(currentOrder);
    }

    public boolean foodToBuy(){
        return (currentOrder.getMenus().size() + currentOrder.getPartyMenus().size()) != 0;
    }

    public Double moneyToPayForOrder(){
        Double pay=0.0;
        for(Menu menu: currentOrder.getMenus().keySet()){
            pay += menu.getPrice() * currentOrder.getMenus().get(menu);
        }
        for(MenuParty menu:currentOrder.getPartyMenus().keySet()){
            pay += menu.getNewPrice() * currentOrder.getPartyMenus().get(menu);
        }
        System.out.println("total money  should pay is " + Double.toString(pay));
        return pay;
    }


    public void finalizeOrder(Double moneyToPay){
        this.setCurrentOrderId();
        currentOrder = new Order();
        this.chosenRestaurantNameToBuy = null;
        this.credit-=moneyToPay;
    }


    public void removingOrder(){
        this.chosenRestaurantNameToBuy = null;
        currentOrder = new Order();
    }

    public void addFoodToCart(Menu menu, String restaurantName , String restaurantId , int count){
        if(this.startChoosingFood()){
            System.out.println("user start new order from " + restaurantName);
            currentOrder.setId(this.getCurrentOrderId());
            this.allOrders.add(currentOrder);
        }
        this.currentOrder.addFood(menu , count);
        this.currentOrder.setRestaurantName(restaurantName);
        this.currentOrder.setRestaurantId(restaurantId);
        chosenRestaurantNameToBuy = restaurantName;
    }

    public void addPartyFoodToCart(MenuParty menu , String restaurantName , String restaurantId ,int count){
        if(this.startChoosingFood()){
            System.out.println("user start new order from " + restaurantName);
            currentOrder.setId(this.getCurrentOrderId());
            this.allOrders.add(currentOrder);
        }
        this.currentOrder.addPartyFood(menu , count);
        this.currentOrder.setRestaurantName(restaurantName);
        this.currentOrder.setRestaurantId(restaurantId);
        chosenRestaurantNameToBuy = restaurantName;
    }

    public boolean startChoosingFood(){
        return chosenRestaurantNameToBuy==null;

    }

    public String giveYourRestaurantYouBuy(){
        return chosenRestaurantNameToBuy;
    }

    public boolean isBuyFromOtherRestaurant(String name){
        return !chosenRestaurantNameToBuy.equals(name);
    }

    public void setRestaurantToBuyFrom(String name){
        this.chosenRestaurantNameToBuy= name;
    }

    public Order getCurrentOrder(){
        return currentOrder;
    }

    public ArrayList<Order> getAllOrders() {
        return this.allOrders; }




    public int getCurrentOrderId() { return currentOrderId; }

    public void setCurrentOrderId() { currentOrderId += 1; }

    public Order getOrder(int id){
        for(int i=0;i<allOrders.size();i++){
            if(allOrders.get(i).getId()==id){
                return allOrders.get(i);
            }
        }
        return null;
    }

}
