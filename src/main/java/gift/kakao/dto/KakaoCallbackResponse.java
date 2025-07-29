package gift.kakao.dto;

public class KakaoCallbackResponse extends BaseResponse {
    private final String accessToken;

    private KakaoCallbackResponse(ResponseStatus status, String message, String accessToken) {
        super(status, message);
        this.accessToken = accessToken;
    }

    public static KakaoCallbackResponse success(String accessToken) {
        return new KakaoCallbackResponse(
                ResponseStatus.SUCCESS,
                "로그인에 성공했습니다.",
                accessToken
        );
    }

    public static KakaoCallbackResponse failure(String message) {
        return new KakaoCallbackResponse(
                ResponseStatus.ERROR,
                message,
                null
        );
    }

    public String getAccessToken() {
        return accessToken;
    }
}
