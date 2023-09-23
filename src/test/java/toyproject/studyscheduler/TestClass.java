package toyproject.studyscheduler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

public class TestClass {

    @DisplayName("")
    @Test
    void test() {
        // given
        LocalDate from = LocalDate.from(YearMonth.of(2023, 07));
        // when
        System.out.println(from);
        // then

    }
}
