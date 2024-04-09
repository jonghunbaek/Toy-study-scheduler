package toyproject.studyscheduler.study.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.study.exception.StudyException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static toyproject.studyscheduler.common.response.ResponseCode.*;

class StudyPeriodTest {

    @DisplayName("시작할 스터디 기간 생성시 종료일은 최대 값으로 설정한다.")
    @Test
    void fromStarting() {
        StudyPeriod studyPeriod = StudyPeriod.fromStarting(LocalDate.now());

        assertThat(studyPeriod.getEndDate()).isEqualTo(StudyPeriod.TEMP_END_DATE);
    }

    @DisplayName("새로운 학습 기간을 생성할 때 시작일이 종료일보다 앞서 있는지 검증한다.")
    @Test
    void validateStartDtEarlierEndDt() {
        LocalDate startDate = LocalDate.of(2024, 04, 07);
        LocalDate endDate = LocalDate.of(2023, 04, 06);

        assertThatThrownBy(() -> StudyPeriod.fromTerminated(startDate, endDate))
            .isInstanceOf(StudyException.class)
            .hasMessage("종료일이 시작일보다 앞서있습니다. detail => startDate, endDate :: " + startDate +", " + endDate);
    }
}