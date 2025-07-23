package gift.option.dto;

import gift.option.model.Option;
import gift.product.model.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionRequestDto(
        @NotBlank(message = "옵션 이름은 필수입니다.")
        @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력 가능합니다.")
        @Pattern(
                regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$",
                message = "옵션명에 허용되지 않은 특수문자가 포함되어 있습니다. 사용 가능한 특수문자: ( ) [ ] + - & / _"
        )
        String name,

        @Min(value = 1, message = "옵션 수량은 최소 1개 이상입니다.")
        @Max(value = 99_999_999, message = "옵션 수량은 최대 1억개입니다.")
        Long quantity
) {
    public Option toEntity(Product product) {
        return new Option(this.name, this.quantity, product);
    }
}
