package toyproject.studyscheduler.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StudyUtilTest {

    @DisplayName("PeriodCalculator 객체를 생성해 총 예상 강의 학습기간을 계산한다.")
    @Test
    void calculateExpectedPeriod() {
        // given
        int planTimeInWeekDay = 60;
        int planTimeInWeekend = 120;
        LocalDate startDate = LocalDate.of(2023, 9, 10);
        int totalRunTime = 600;

        StudyUtil studyUtil = new StudyUtil();

        // when
        int period = studyUtil.setUpPeriodCalculatorInfos(planTimeInWeekDay, planTimeInWeekend, startDate)
                .calculateTotalExpectedPeriod(totalRunTime);

        // then
        assertThat(period).isEqualTo(8);
    }
}