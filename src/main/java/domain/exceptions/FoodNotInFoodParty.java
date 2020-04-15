package domain.exceptions;



public class FoodNotInFoodParty  extends Exception  {
    private String message;
    public  FoodNotInFoodParty  (){
        message = "غذای مورد نظر در رستوران به عنوان فود پارتی یافت نشد";
    }
    public String getMessage() {
        return message;
    }
}
