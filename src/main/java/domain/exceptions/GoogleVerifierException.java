package domain.exceptions;

public class GoogleVerifierException extends Exception {
    private String message;
    public  GoogleVerifierException(){
        message = "خطای اعبارسنجی گوگل";
    }
    public String getMessage() {
        return message;
    }
}
