package gift.product.repository;

import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Product Repository 학습 테스트")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("상품을 저장하고 조회할 수 있다")
    void saveAndFindProduct() {
        // given
        Product product = new Product("kakao", 1000, "https://example.com/kakao.jpg");

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("kakao");
        assertThat(savedProduct.getPrice()).isEqualTo(1000);
        assertThat(savedProduct.getImageUrl()).isEqualTo("https://example.com/kakao.jpg");
    }

    @Test
    @DisplayName("ID로 상품을 조회")
    void findById() {
        // given
        Product product = new Product("kakao2", 10000, "https://example.com/kakao2.jpg");
        Product savedProduct = entityManager.persistAndFlush(product);

        // when
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("kakao2");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 빈 Optional을 반환")
    void findByIdNotExists() {
        // when
        Optional<Product> foundProduct = productRepository.findById(999L);

        // then
        assertThat(foundProduct).isEmpty();
    }

    @Test
    @DisplayName("모든 상품 조회")
    void findAll() {
        // given
        Product product1 = new Product("kakao", 1200000, "https://example.com/kakao.jpg");
        Product product2 = new Product("kakao2", 1000000, "https://example.com/kakao2.jpg");

        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.flush();

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(2);
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProduct() {
        // given
        Product product = new Product("kakao3", 2500000, "https://example.com/kakao3.jpg");
        Product savedProduct = entityManager.persistAndFlush(product);

        // when
        productRepository.deleteById(savedProduct.getId());

        // then
        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        assertThat(deletedProduct).isEmpty();
    }
}