

package domain.exceptions;




public class  UserNotFound  extends Exception {
    private String message;
    public   UserNotFound (){
        message = "کاربر در سیستم یافت نشد" ;

    }
    public String getMessage() {
        return message;
    }
}
