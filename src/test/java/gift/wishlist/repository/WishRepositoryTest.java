package gift.wishlist.repository;

import gift.member.model.Member;
import gift.product.model.Product;
import gift.wishlist.model.Wish;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Wish Repository 학습 테스트")
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("ID로 위시리스트를 저장하고 조회")
    void saveAndFindWish_IdBased() {
        // given
        Member member = new Member("test@example.com", "1234");
        Product product = new Product("kakao", 1000, "https://example.com/kakao.jpg");

        Member savedMember = entityManager.persistAndFlush(member);
        Product savedProduct = entityManager.persistAndFlush(product);

        Wish wish = new Wish(savedMember.getId(), savedProduct.getId(), 2l);

        // when
        Wish savedWish = wishRepository.save(wish);

        // then
        assertThat(savedWish.getId()).isNotNull();
        assertThat(savedWish.getMemberId()).isEqualTo(savedMember.getId());
        assertThat(savedWish.getProductId()).isEqualTo(savedProduct.getId());
        assertThat(savedWish.getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("회원 ID로 위시리스트를 조회")
    void findByMemberId() {
        // given
        Member member = new Member("user@example.com", "1234");
        Product product1 = new Product("kakao", 1200000, "https://example.com/kakao.jpg");
        Product product2 = new Product("kakao1", 1000000, "https://example.com/kakao1.jpg");

        Member savedMember = entityManager.persistAndFlush(member);
        Product savedProduct1 = entityManager.persistAndFlush(product1);
        Product savedProduct2 = entityManager.persistAndFlush(product2);

        Wish wish1 = new Wish(savedMember.getId(), savedProduct1.getId(), 1l);
        Wish wish2 = new Wish(savedMember.getId(), savedProduct2.getId(), 2l);

        entityManager.persist(wish1);
        entityManager.persist(wish2);
        entityManager.flush();

        // when
        List<Wish> wishes = wishRepository.findAll();

        // then
        assertThat(wishes).hasSize(2);
    }

    @Test
    @DisplayName("상품 ID로 위시리스트를 조회")
    void findByProductId() {
        // given
        Member member = new Member("user@example.com", "1234");
        Product product = new Product("kakao", 1000, "https://example.com/kakao.jpg");

        Member savedMember = entityManager.persistAndFlush(member);
        Product savedProduct = entityManager.persistAndFlush(product);

        Wish wish = new Wish(savedMember.getId(), savedProduct.getId(), 1l);
        entityManager.persistAndFlush(wish);

        // when
        Optional<Wish> foundWish = wishRepository.findByProductId(savedProduct.getId());

        // then
        assertThat(foundWish.get().getProductId()).isEqualTo(savedProduct.getId());
    }

    @Test
    @DisplayName("위시리스트를 삭제")
    void deleteWish() {
        // given
        Member member = new Member("user@example.com", "1234");
        Product product = new Product("kakao3", 1500000, "https://example.com/kakao3.jpg");

        Member savedMember = entityManager.persistAndFlush(member);
        Product savedProduct = entityManager.persistAndFlush(product);

        Wish wish = new Wish(savedMember.getId(), savedProduct.getId(), 1l);
        Wish savedWish = entityManager.persistAndFlush(wish);

        // when
        wishRepository.deleteById(savedWish.getId());

        // then
        Optional<Wish> deletedWish = wishRepository.findById(savedWish.getId());
        assertThat(deletedWish).isEmpty();
    }
}