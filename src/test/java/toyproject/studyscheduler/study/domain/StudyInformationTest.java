package toyproject.studyscheduler.study.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.study.exception.StudyException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StudyInformationTest {

    @DisplayName("학습이 종료된 상태면 예외를 던진다.")
    @Test
    void validateTermination() {
        // given
        StudyInformation studyInformation = StudyInformation.builder()
            .title("title")
            .description("description")
            .isTermination(true)
            .build();

        // when & then
        assertThatThrownBy(() -> studyInformation.validateTermination())
            .isInstanceOf(StudyException.class)
            .hasMessage("해당 학습은 이미 종료되었습니다.");
    }
}