package gift.kakao.message;

import gift.kakao.exception.KakaoMessageException;
import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoMessageClient {

    private final RestTemplate restTemplate;
    private static final String KAKAO_API_BASE_URL = "https://kapi.kakao.com";
    private static final String SEND_ME_MESSAGE_URL = "/v2/api/talk/memo/default/send";

    public KakaoMessageClient(RestTemplateBuilder restTemplateBuilder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(2));
        factory.setReadTimeout(Duration.ofSeconds(2));

        this.restTemplate = restTemplateBuilder
                .rootUri(KAKAO_API_BASE_URL)
                .requestFactory(() -> factory)
                .build();
    }

    public void sendMeMessage(String accessToken, String templateObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(SEND_ME_MESSAGE_URL, request, String.class);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                throw new KakaoMessageException(
                        String.format("카카오톡 메시지 전송 실패 - 상태코드: %s, 응답: %s",
                                response.getStatusCode(), response.getBody())
                );
            }
        } catch (HttpClientErrorException e) {
            handleClientError(e);
        } catch (HttpServerErrorException e) {
            handleServerError(e);
        } catch (RuntimeException e) {
            throw new KakaoMessageException("카카오톡 메시지 전송 중 예상치 못한 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void handleClientError(HttpClientErrorException e) {
        switch (e.getStatusCode().value()) {
            case 400:
                throw new KakaoMessageException("잘못된 메시지 템플릿 형식입니다.");
            case 401:
                throw new KakaoMessageException("카카오톡 메시지 전송 권한이 없습니다. 액세스 토큰을 확인해주세요.");
            case 403:
                throw new KakaoMessageException("카카오톡 메시지 API 사용 권한이 없습니다.");
            default:
                throw new KakaoMessageException("카카오톡 메시지 전송 요청이 실패했습니다.");
        }
    }

    private void handleServerError(HttpServerErrorException e) {
        throw new KakaoMessageException("카카오 서버에 오류입니다. 잠시 후 다시 시도해주세요.");
    }
}
