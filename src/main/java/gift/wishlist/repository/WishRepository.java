package gift.wishlist.repository;

import gift.wishlist.model.Wish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByMemberId(Long memberId, Pageable pageable);
    Optional<Wish> findByProductId(Long productId);
    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId); // 추가
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
