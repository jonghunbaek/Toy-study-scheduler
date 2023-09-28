package toyproject.studyscheduler.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StudyUtilTest {

    @DisplayName("강의 학습의 총 예상 기간을 계산한다.")
    @Test
    void calculateTotalExpectedPeriodWithLecture() {
        // given
        int planTimeInWeekDay = 60;
        int planTimeInWeekend = 120;
        int totalRunTime = 600;

        StudyUtil studyUtil = new StudyUtil();

        // when
        int totalExpectedPeriod = studyUtil.calculateTotalExpectedPeriod(planTimeInWeekDay, planTimeInWeekend, totalRunTime);

        // then
        assertThat(totalExpectedPeriod).isEqualTo(8);
    }
}