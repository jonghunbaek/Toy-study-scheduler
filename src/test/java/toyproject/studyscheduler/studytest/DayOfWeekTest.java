package toyproject.studyscheduler.studytest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

public class DayOfWeekTest {

    @DisplayName("DayOfWeek.plus() 관련 학습 테스트")
    @Test
    void dayOfWeekPlus() {
        // given
        DayOfWeek dayOfWeek = DayOfWeek.SUNDAY;

        // when
        dayOfWeek = dayOfWeek.plus(1);

        // then
        Assertions.assertThat(dayOfWeek).isEqualTo(DayOfWeek.MONDAY);
    }
}
