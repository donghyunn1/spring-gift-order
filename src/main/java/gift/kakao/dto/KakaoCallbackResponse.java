package gift.kakao.dto;

public record KakaoCallbackResponse(
        String status,
        String message,
        String accessToken
) {
    public static KakaoCallbackResponse success(String accessToken) {
        return new KakaoCallbackResponse("success", "로그인에 성공했습니다.", accessToken);
    }

    public static KakaoCallbackResponse failure(String message) {
        return new KakaoCallbackResponse("error", message, null);
    }
}
