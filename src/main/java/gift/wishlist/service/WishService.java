package gift.wishlist.service;

import gift.wishlist.dto.WishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.model.Wish;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishService {
    Wish addWish(Long memberId, WishRequestDto requestDto);
    Page<WishResponseDto> getWishesByMemberId(Long memberId, Pageable pageable);
    void deleteWish(Long wishId, Long memberId);
}
