package gift.member.repository;

import gift.member.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Member Repository 학습 테스트")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("회원을 저장하고 조회")
    void saveAndFindMember() {
        // given
        Member member = new Member("test@example.com", "1234");

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo("test@example.com");
        assertThat(savedMember.getPassword()).isEqualTo("1234");
        assertThat(savedMember.getRole()).isEqualTo("USER");
    }

    @Test
    @DisplayName("이메일로 회원을 조회")
    void findByEmail() {
        // given
        Member member = new Member("user@example.com", "1234");
        entityManager.persistAndFlush(member);

        // when
        Optional<Member> foundMember = memberRepository.findByEmail("user@example.com");

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("user@example.com");
        assertThat(foundMember.get().getPassword()).isEqualTo("1234");
    }


    @Test
    @DisplayName("ID로 회원을 조회")
    void findById() {
        // given
        Member member = new Member("admin@example.com", "1234");
        Member savedMember = entityManager.persistAndFlush(member);

        // when
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("admin@example.com");
    }

    @Test
    @DisplayName("회원을 삭제")
    void deleteMember() {
        // given
        Member member = new Member("delete@example.com", "1234");
        Member savedMember = entityManager.persistAndFlush(member);

        // when
        memberRepository.deleteById(savedMember.getId());

        // then
        Optional<Member> deletedMember = memberRepository.findById(savedMember.getId());
        assertThat(deletedMember).isEmpty();
    }
}