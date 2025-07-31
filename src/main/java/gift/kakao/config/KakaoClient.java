package gift.kakao.config;

import gift.kakao.dto.KakaoLoginResponse;
import gift.kakao.exception.KakaoAuthException;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoClient {

    private final RestTemplate restTemplate;

    public KakaoClient(RestTemplateBuilder restTemplateBuilder, @Value("${kakao.api.base-url}") String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(10));

        this.restTemplate = restTemplateBuilder
                .rootUri(baseUrl)
                .requestFactory(() -> factory)
                .build();
    }

    public KakaoLoginResponse getAccessToken(String code, String clientId, String redirectUri) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            var body = new LinkedMultiValueMap<String, String>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", clientId);
            body.add("redirect_uri", redirectUri);
            body.add("code", code);

            HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            KakaoLoginResponse response = restTemplate.postForObject("/oauth/token", request, KakaoLoginResponse.class);

            return response;
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        } catch (RuntimeException e) {
            throw new KakaoAuthException("카카오 토큰 발급 중 예상치 못한 오류가 발생했습니다: " + e.getMessage());
        }
        return null;
    }

    private void handleClientError(HttpClientErrorException e) {
        switch (e.getStatusCode().value()) {
            case 400:
                throw new KakaoAuthException("잘못된 요청입니다. 인증 코드나 파라미터를 확인해주세요.");
            case 401:
                throw new KakaoAuthException("카카오 인증에 실패했습니다. 클라이언트 ID를 확인해주세요.");
            case 403:
                throw new KakaoAuthException("카카오 API 접근 권한이 없습니다.");
            default:
                throw new KakaoAuthException("카카오 인증 요청이 실패했습니다.");
        }
    }

    private void handleServerError(HttpServerErrorException e) {
        throw new KakaoAuthException("카카오 서버 오류입니다. 잠시 후 다시 시도해주세요.");
    }
}