package toyproject.studyscheduler.studytest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class LocalDateTest {

    @DisplayName("String을 인자로 LocalDate를 생성할 수 있는 지 테스트한다.")
    @Test
    void localDateOf() {
        // given
        String date = "2024-03-01";

        // when
        LocalDate localDate = LocalDate.parse(date);

        // then
        Assertions.assertThat(localDate).isEqualTo(LocalDate.of(2024,3,1));
    }
}
