package gift.kakao.service;

import gift.kakao.config.KakaoConfig;
import gift.kakao.dto.KakaoLoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoLoginService {

    private final KakaoConfig kakaoConfig;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    public KakaoLoginService(KakaoConfig kakaoConfig) {
        this.kakaoConfig = kakaoConfig;
    }

    public String getAccessToken(String code) {
        KakaoLoginResponse response = kakaoConfig.getAccessToken(code, clientId, redirectUri);

        return response.accessToken();
    }
}
