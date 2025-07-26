package gift.kakao.config;

import gift.kakao.dto.KakaoLoginResponse;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoConfig {

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoLoginResponse getAccessToken(String code, String clientId, String redirectUri) {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        ResponseEntity<KakaoLoginResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, request, KakaoLoginResponse.class
        );

        return response.getBody();
    }
}
