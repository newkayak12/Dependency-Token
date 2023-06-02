package exception;

public enum Reason {
    INVALID_SECRET_KEY_PATH("잘못된 경로입니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다.");

    private String message;
    Reason(String message){this.message = message;}
    public String getMessage(){
        return this.message;
    }
}
