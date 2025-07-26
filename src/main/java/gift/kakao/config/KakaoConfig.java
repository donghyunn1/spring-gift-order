package gift.kakao.config;

import gift.kakao.dto.KakaoLoginResponse;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoConfig {

    private final RestTemplate restTemplate;

    public KakaoConfig(RestTemplateBuilder restTemplateBuilder, @Value("${kakao.api.base-url}") String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(10));

        this.restTemplate = restTemplateBuilder
                .rootUri(baseUrl)
                .requestFactory(() -> factory)  // RequestFactory 사용
                .build();
    }

    public KakaoLoginResponse getAccessToken(String code, String clientId, String redirectUri) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject("/oauth/token", request, KakaoLoginResponse.class);
    }
}
