package gift.order.service.impl;

import gift.kakao.dto.KakaoMessageTemplate;
import gift.kakao.message.KakaoMessageClient;
import gift.option.excepiton.OptionNotFoundException;
import gift.option.model.Option;
import gift.option.repository.OptionRespository;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.exception.InsufficientStockException;
import gift.order.model.Order;
import gift.order.repository.OrderRepository;
import gift.order.service.OrderService;
import gift.product.model.Product;
import gift.wishlist.model.Wish;
import gift.wishlist.repository.WishRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OptionRespository optionRepository;
    private final WishRepository wishRepository;
    private final KakaoMessageClient kakaoMessageClient;

    public OrderServiceImpl(OrderRepository orderRepository,
            OptionRespository optionRepository,
            WishRepository wishRepository,
            KakaoMessageClient kakaoMessageClient) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoMessageClient = kakaoMessageClient;
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long memberId, String accessToken, OrderRequestDto requestDto) {
        Option option = optionRepository.findById(requestDto.optionId())
                .orElseThrow(() -> new OptionNotFoundException("존재하지 않는 옵션입니다."));

        if (option.getQuantity() < requestDto.quantity()) {
            throw new InsufficientStockException(
                    String.format("재고가 부족합니다. 현재 재고: %d개, 주문 수량: %d개", option.getQuantity(), requestDto.quantity())
            );
        }

        option.subtractQuantity(requestDto.quantity());

        Product product = option.getProduct();
        Optional<Wish> existingWish = wishRepository.findByMemberIdAndProductId(memberId, product.getId());
        existingWish.ifPresent(wishRepository::delete);

        Order order = requestDto.toEntity(memberId);
        Order savedOrder = orderRepository.save(order);

        try {
            sendKakaoMessage(accessToken, product, option, savedOrder);
        } catch (Exception e) {
            System.err.println("카카오톡 메시지 전송 실패: " + e.getMessage());
        }

        return OrderResponseDto.from(savedOrder);
    }

    private void sendKakaoMessage(String accessToken, Product product, Option option, Order order) {
        String messageTemplate = KakaoMessageTemplate.createOrderMessage(
                product.getName(),
                option.getName(),
                order.getQuantity(),
                order.getMessage(),
                order.getOrderDateTime()
        );

        kakaoMessageClient.sendMeMessage(accessToken, messageTemplate);
    }
}