package domain.exceptions;




public class  EmptyCartToFinalize extends Exception  {
    private String message;
    public  EmptyCartToFinalize (){
        message = "سبد خرید جهت نهایی شدن خرید خالی است";
    }
    public String getMessage() {
        return message;
    }
}

