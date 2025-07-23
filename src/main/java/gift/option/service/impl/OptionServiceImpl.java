package gift.option.service.impl;

import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.excepiton.DuplicatedOptionNameException;
import gift.option.excepiton.OptionNotFoundException;
import gift.option.excepiton.OptionValidationException;
import gift.option.model.Option;
import gift.option.repository.OptionRespository;
import gift.option.service.OptionService;
import gift.product.exception.ProductNotFoundException;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OptionServiceImpl implements OptionService {

    private final OptionRespository optionRespository;
    private final ProductRepository productRepository;

    public OptionServiceImpl(OptionRespository optionRespository, ProductRepository productRepository) {
        this.optionRespository = optionRespository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Option createOption(Long productId, OptionRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (optionRespository.existsByProductIdAndName(productId, requestDto.name())) {
            throw new DuplicatedOptionNameException("동일한 상품 내에 중복된 옵션명이 존재합니다: " + requestDto.name());
        }

        Option option = requestDto.toEntity(product);
        return optionRespository.save(option);
    }

    @Override
    public List<OptionResponseDto> getOptionsByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }

        List<Option> options = optionRespository.findByProductId(productId);
        return options.stream()
                .map(OptionResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void subtractOptionQuantity(Long optionId, Long quantity) {
        Option option = optionRespository.findById(optionId)
                        .orElseThrow(() -> new OptionNotFoundException("옵션을 찾을 수 없습니다."));
        option.subtractQuantity(quantity);
    }

    @Transactional
    public void deleteOption(Long optionId) {
        Option option = optionRespository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException("옵션을 찾을 수 없습니다. ID: " + optionId));

        Long productId = option.getProduct().getId();
        long optionCount = optionRespository.countByProductId(productId);

        if (optionCount < 1) {
            throw new OptionValidationException("상품에는 최소 하나 이상의 옵션이 있어야 합니다. 마지막 옵션은 삭제할 수 없습니다.");
        }

        optionRespository.deleteById(optionId);
    }
}
