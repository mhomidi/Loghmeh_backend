package domain.exceptions;


public class BuyFromOtherRestaurant extends Exception {
    private String message;
    public BuyFromOtherRestaurant(){
        //message = "You have already food from other restaurant!";
        message = "شما در حال حاضر سفارش ثبت نشده ای از رستوران دیگری دارید.";
    }
    public String getMessage() {
        return message;
    }
}


