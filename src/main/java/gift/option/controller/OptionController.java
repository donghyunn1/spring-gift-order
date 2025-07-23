package gift.option.controller;

import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.model.Option;
import gift.option.service.OptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<Option> createOption
            (@PathVariable("productId") Long productId,
             @Valid @RequestBody OptionRequestDto requestDto) {
        Option option = optionService.createOption(productId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(option);
    }

    @GetMapping
    public ResponseEntity<List<OptionResponseDto>> getOptionsByProductId(@PathVariable Long productId) {
        List<OptionResponseDto> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
