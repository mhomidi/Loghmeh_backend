package domain.exceptions;





public class  FoodNotExist extends Exception  {
    private String message;
    public  FoodNotExist (){
        message = "غذای مورد نظر در رستوران وجود نداشته است(خطای سیستمی)";
    }
    public String getMessage() {
        return message;
    }
}

