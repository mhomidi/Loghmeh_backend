

package domain.exceptions;



public class  RestaurantNotAvailable  extends Exception {
    private String message;
    public   RestaurantNotAvailable (){
        message = "رستوران مورد نظر در دسترس شما نیست" ;
    }
    public String getMessage() {
        return message;
    }
}
