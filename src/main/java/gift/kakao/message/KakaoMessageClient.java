package gift.kakao.message;

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
                throw new RuntimeException(
                        String.format("카카오톡 메시지 전송 실패 - 상태코드: %s, 응답: %s", response.getStatusCode(), response.getBody())
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("카카오톡 메시지 전송 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}
