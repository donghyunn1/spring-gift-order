package gift.kakao.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class KakaoMessageTemplate {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String createOrderMessage(String productName, String optionName, Long quantity, String message, LocalDateTime orderDateTime) {
        String formattedDateTime = orderDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, Object> template = Map.of(
                "object_type", "text",
                "text", String.format(
                        "주문이 완료되었습니다!\n\n" +
                                "상품: %s (%s)\n" +
                                "수량: %d개\n" +
                                "주문시간: %s\n" +
                                "메시지: %s\n\n",
                        productName, optionName, quantity, formattedDateTime,
                        message != null ? message : "없음"
                ),
                "link", Map.of(
                        "web_url", "http://localhost:8080/admin/products"
                )
        );

        try {
            return objectMapper.writeValueAsString(template);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메시지 템플릿 생성 중 오류가 발생했습니다.", e);
        }
    }
}
