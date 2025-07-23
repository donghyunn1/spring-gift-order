package gift.option.dto;

import gift.option.model.Option;

public record OptionResponseDto(
        Long id,
        String name,
        Long quantity
) {
    public static OptionResponseDto from(Option option) {
        return new OptionResponseDto(
                option.getId(),
                option.getName(),
                option.getQuantity()
        );
    }
}
