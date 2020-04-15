

package domain.exceptions;


public class   RestaurantNotFound  extends Exception {
    private String message;
    public   RestaurantNotFound (){
        message ="رستوران در سرور یافت نشد";

    }
    public String getMessage() {
        return message;
    }
}
