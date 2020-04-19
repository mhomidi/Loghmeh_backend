package domain.exceptions;

public class NotFindOrder extends Exception {
    private String message;
    public  NotFindOrder (){
        message = "اطلاعات سفارش کاربر یافت نشد";
    }
    public String getMessage() {
        return message;
    }
}
