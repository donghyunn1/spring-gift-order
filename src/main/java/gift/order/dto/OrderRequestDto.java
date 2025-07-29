package gift.order.dto;

import gift.order.model.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record OrderRequestDto(
        @NotNull
        Long optionId,

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        Long quantity,

        @Size(max = 500, message = "메시지는 최대 500자까지 입력 가능합니다.")
        String message
) {
    public Order toEntity(Long memberId) {
        return new Order(memberId, this.optionId, this.quantity, LocalDateTime.now(), this.message);
    }
}
