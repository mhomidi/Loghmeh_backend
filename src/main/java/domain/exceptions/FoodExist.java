package domain.exceptions;

public class FoodExist extends Exception {
    private String message;
    public FoodExist(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
