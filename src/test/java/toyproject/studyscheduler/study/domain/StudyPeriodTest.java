package toyproject.studyscheduler.study.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class StudyPeriodTest {

    @DisplayName("시작할 스터디 기간 생성시 종료일은 최대 값으로 설정한다.")
    @Test
    void fromStarting() {
        StudyPeriod studyPeriod = StudyPeriod.fromStarting(LocalDate.now());

        assertThat(studyPeriod.getEndDate()).isEqualTo(LocalDate.MAX);
    }
}