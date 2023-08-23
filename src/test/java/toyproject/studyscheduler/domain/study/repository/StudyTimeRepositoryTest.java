package toyproject.studyscheduler.domain.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@DataJpaTest
class StudyTimeRepositoryTest {

    @DisplayName("특정기간에 수행한 일간 학습량을 모두 조회 한다.")
    @Test
    void getStudyTimePerDay() {
        // given


        // when

        // then

    }
}