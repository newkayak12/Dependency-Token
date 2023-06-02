package exception;

public class TokenException extends Exception{
    public TokenException(Reason reason) {
        super(reason.getMessage());
    }
}
