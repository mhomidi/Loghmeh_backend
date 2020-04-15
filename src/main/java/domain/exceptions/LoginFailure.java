package domain.exceptions;




public class  LoginFailure extends Exception  {
    private String message;
    public  LoginFailure (){
        message = "خطای سیستمی در ورود به صفحه ی کاربری";
    }
    public String getMessage() {
        return message;
    }
}
