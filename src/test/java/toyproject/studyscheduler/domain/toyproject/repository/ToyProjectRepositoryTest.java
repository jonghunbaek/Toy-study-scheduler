package toyproject.studyscheduler.domain.toyproject.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.toyproject.TechStack;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class ToyProjectRepositoryTest {

    @DisplayName("id값을 전달받아 저장된 ToyProject 하나를 조회한다.")
    @Test
    void findToyProjectById() {
        // given
        TechStack.builder()

            .build();

        // when

        // then

     }
}