package gift.kakao.dto;

public abstract class BaseResponse {

    private final ResponseStatus status;
    private final String message;

    protected BaseResponse(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
