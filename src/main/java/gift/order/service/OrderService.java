package gift.order.service;

import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createOrder(Long memberId, String accessToken, OrderRequestDto requestDto);
}
