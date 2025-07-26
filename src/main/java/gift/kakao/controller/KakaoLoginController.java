package gift.kakao.controller;

import gift.kakao.dto.KakaoCallbackResponse;
import gift.kakao.service.KakaoLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/login/callback")
    public ResponseEntity<KakaoCallbackResponse> callback(@RequestParam String code) {
        try {
            String accessToken = kakaoLoginService.getAccessToken(code);
            return ResponseEntity.ok(KakaoCallbackResponse.success(accessToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(KakaoCallbackResponse.failure("로그인 실패: " + e.getMessage()));
        }
    }
}
