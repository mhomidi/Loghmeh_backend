package domain.exceptions;

public class NoCurrOrder extends Exception {
    private String message;
    public  NoCurrOrder (){
        message = "کاربر سفارش جاری ثبت شده ای در سیستم ندارد";
    }
    public String getMessage() {
        return message;
    }
}
