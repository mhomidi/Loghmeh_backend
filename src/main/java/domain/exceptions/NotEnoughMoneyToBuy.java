package domain.exceptions;





public class NotEnoughMoneyToBuy extends Exception  {
    private String message;
    public  NotEnoughMoneyToBuy (){
        message = "موجودی برای سفارش کافی نیست";
    }
    public String getMessage() {
        return message;
    }
}