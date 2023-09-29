package toyproject.studyscheduler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class StudyPeriodUtilTest {

    @DisplayName("강의 학습의 총 예상 기간을 계산한다.")
    @Test
    void calculateTotalExpectedPeriodWithLecture() {
        // given
        int planTimeInWeekDay = 60;
        int planTimeInWeekend = 120;
        int totalRunTime = 600;
        LocalDate startDate = LocalDate.of(2023, 9, 15);

        StudyPeriodUtil studyPeriodUtil = new StudyPeriodUtil();
        // when
        int totalExpectedPeriod = studyPeriodUtil.setUpBasicInfo(planTimeInWeekDay, planTimeInWeekend, startDate)
            .calculateTotalExpectedPeriod(totalRunTime);

        // then
        assertThat(totalExpectedPeriod).isEqualTo(8);
    }
}