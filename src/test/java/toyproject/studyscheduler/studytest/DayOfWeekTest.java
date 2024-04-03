package toyproject.studyscheduler.studytest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("DayOfWeek 학습 테스트")
    @Test
    void dayOfWeekStudyTest() {
        // given
        LocalDate localDate = LocalDate.of(2023, 9,29);

        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        // when
        DayOfWeek plus1 = dayOfWeek.plus(1);
        DayOfWeek plus2 = plus1.plus(1);
        DayOfWeek plus3 = plus2.plus(1);
        DayOfWeek plus4 = plus3.plus(1);

        // then
        assertThat(plus1.getValue()).isEqualTo(6);
        assertThat(plus2.getValue()).isEqualTo(7);
        assertThat(plus3.getValue()).isEqualTo(1);
        assertThat(plus4.getValue()).isEqualTo(2);
    }
}
