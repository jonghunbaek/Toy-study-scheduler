package toyproject.studytest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MathCeilTest {

    @DisplayName("자바 라이브러리 Math.ceil()을 테스트한다.")
    @Test
    void math_ceil() {
        // given
        int a = 301;
        int b = 2;

        // when
        int result = (int) Math.ceil((double) a / b);

        // then
        assertThat(result).isEqualTo(151);
    }
}
