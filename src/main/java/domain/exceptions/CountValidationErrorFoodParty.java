package domain.exceptions;



public class  CountValidationErrorFoodParty extends Exception  {
    private String message;
    public  CountValidationErrorFoodParty (){
        message = "اتمام موجودی غذا جهت سفارش غذای فود پارتی ";
    }
    public String getMessage() {
        return message;
    }
}
