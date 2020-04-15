package domain.exceptions;




public class  InvalidUser extends Exception  {
    private String message;
    public  InvalidUser (){
        message = "خطای سیستمی در اضافه کردن کاربر";
    }
    public String getMessage() {
        return message;
    }
}
