package gift.order.controller;

import gift.jwt.auth.LoginMember;
import gift.member.model.Member;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto requestDto,
            @LoginMember Member member,
            @RequestHeader(value = "Kakao-Access-Token", required = false) String kakaoAccessToken) {

        OrderResponseDto orderResponse = orderService.createOrder(
                member.getId(),
                kakaoAccessToken,
                requestDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
