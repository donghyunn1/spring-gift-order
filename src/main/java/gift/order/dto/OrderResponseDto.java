package gift.order.dto;

import gift.order.model.Order;
import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        Long optionId,
        Long quantity,
        LocalDateTime orderDateTime,
        String message
) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }
}
