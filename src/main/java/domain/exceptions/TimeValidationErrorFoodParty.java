package domain.exceptions;




public class  TimeValidationErrorFoodParty extends Exception  {
    private String message;
    public  TimeValidationErrorFoodParty (){
        message = "اتمام زمان لازم جهت سفارش غذای فود پارتی ";
    }
    public String getMessage() {
        return message;
    }
}
