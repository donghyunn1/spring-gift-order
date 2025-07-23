package gift.option.repository;

import gift.option.model.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRespository extends JpaRepository<Option, Long> {

    List<Option> findByProductId(Long productId);
    boolean existsByProductIdAndName(Long productId, String name);
    long countByProductId(Long productId);
}
