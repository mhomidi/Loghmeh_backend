package domain.exceptions;




public class  InvalidUser extends Exception  {
    private String message;
    public  InvalidUser (){
        message = "خطای سیستمی در اضافه کردن کاربر فیلد های وارده معتبر نیست";
    }
    public String getMessage() {
        return message;
    }
}
