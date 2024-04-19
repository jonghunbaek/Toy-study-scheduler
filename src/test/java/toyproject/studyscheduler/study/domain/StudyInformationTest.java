package toyproject.studyscheduler.study.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.study.exception.StudyException;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("학습 정보를 종료 상태로 변경한다.")
    @Test
    void terminate() {
        // given
        StudyInformation studyInformation = StudyInformation.builder()
                .title("title")
                .description("description")
                .isTermination(false)
                .build();

        // when
        studyInformation.terminate();

        // then
        assertThat(studyInformation.isTermination()).isTrue();
    }
}