package gift.option.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.option.dto.OptionRequestDto;
import gift.option.excepiton.DuplicatedOptionNameException;
import gift.option.excepiton.OptionValidationException;
import gift.option.model.Option;
import gift.option.repository.OptionRespository;
import gift.product.exception.ProductNotFoundException;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("OptionService 단위 테스트")
class OptionServiceImplTest {

    @Mock
    private OptionRespository optionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OptionServiceImpl optionService;

    private Product product;
    private Option option;
    private OptionRequestDto optionRequestDto;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "테스트 상품", 10000, "http://test.com/image.jpg");
        option = new Option("기본 옵션", 100L, product);
        optionRequestDto = new OptionRequestDto("새로운 옵션", 50L);
    }

    @Test
    @DisplayName("옵션 생성 성공")
    void createOption_Success() {
        // given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(optionRepository.existsByProductIdAndName(1L, "새로운 옵션")).thenReturn(false);
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        // when
        Option result = optionService.createOption(1L, optionRequestDto);

        // then
        assertThat(result).isNotNull();
        verify(productRepository).findById(1L);
        verify(optionRepository).existsByProductIdAndName(1L, "새로운 옵션");
        verify(optionRepository).save(any(Option.class));
    }

    @Test
    @DisplayName("존재하지 않는 상품에 옵션 생성 시 예외 발생")
    void createOption_ProductNotFound() {
        // given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> optionService.createOption(1L, optionRequestDto))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("중복된 옵션명으로 생성 시 예외 발생")
    void createOption_DuplicateOptionName() {
        // given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(optionRepository.existsByProductIdAndName(1L, "새로운 옵션")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> optionService.createOption(1L, optionRequestDto))
                .isInstanceOf(DuplicatedOptionNameException.class)
                .hasMessageContaining("중복된 옵션명이 존재합니다");
    }

    @Test
    @DisplayName("유효하지 않은 옵션명으로 생성 시 예외 발생")
    void createOption_InvalidOptionName() {
        // given
        OptionRequestDto invalidRequest = new OptionRequestDto("옵션@#$", 50L);

        // when & then
        assertThatThrownBy(() -> optionService.createOption(1L, invalidRequest))
                .isInstanceOf(OptionValidationException.class)
                .hasMessageContaining("허용되지 않은 특수문자");
    }

    @Test
    @DisplayName("옵션 수량 차감 성공")
    void subtractOptionQuantity_Success() {
        // given
        Option optionWithQuantity = new Option("테스트 옵션", 100L, product);
        when(optionRepository.findById(1L)).thenReturn(Optional.of(optionWithQuantity));

        // when
        optionService.subtractOptionQuantity(1L, 30L);

        // then
        assertThat(optionWithQuantity.getQuantity()).isEqualTo(70L);
    }
}