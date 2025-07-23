package gift.option.service;

import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.model.Option;
import java.util.List;

public interface OptionService {
    Option createOption(Long productId, OptionRequestDto requestDto);
    List<OptionResponseDto> getOptionsByProductId(Long productId);
    void deleteOption(Long optionId);
    void subtractOptionQuantity(Long optionId, Long quantity);
}
