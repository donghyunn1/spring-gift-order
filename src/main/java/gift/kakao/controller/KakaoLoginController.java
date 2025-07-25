package gift.kakao.controller;

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
    public ResponseEntity<String> callback(@RequestParam String code) {
        try {
            String accessToken = kakaoLoginService.getAccessToken(code);
            return ResponseEntity.ok("로그인 성공! Access Token: " + accessToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인 실패: " + e.getMessage());
        }
    }
}
