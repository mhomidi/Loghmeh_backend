package domain.exceptions;

public class RestaurantExist extends  Exception {
    private String message;
    public RestaurantExist(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
