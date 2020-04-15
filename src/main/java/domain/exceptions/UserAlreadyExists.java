package domain.exceptions;





public class  UserAlreadyExists extends Exception  {
    private String message;
    public   UserAlreadyExists (){
        message = "کاربری با نام کاربری وارد شده قبلا در سیستم ثبت نام کرده است";
    }
    public String getMessage() {
        return message;
    }
}
