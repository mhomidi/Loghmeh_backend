package domain.exceptions;



public class  CountValidationErrorFoodParty extends Exception  {
    private String message;
    public  CountValidationErrorFoodParty (){
        message = "اتمام موجودی غذا جهت سفارش یا انتخاب غذای فود پارتی ";
    }
    public String getMessage() {
        return message;
    }
}
