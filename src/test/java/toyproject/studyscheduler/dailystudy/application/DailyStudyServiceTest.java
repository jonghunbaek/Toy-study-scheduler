package toyproject.studyscheduler.dailystudy.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.web.dailystudy.DailyStudyCreation;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.application.dto.LectureSave;
import toyproject.studyscheduler.study.exception.StudyException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static toyproject.studyscheduler.common.exception.GlobalException.DETAIL_DELIMITER;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class DailyStudyServiceTest {

    @Autowired
    StudyService studyService;
    @Autowired
    DailyStudyService dailyStudyService;

    @DisplayName("종료된 학습의 일일 학습을 생성하면 예외가 발생한다.")
    @Test
    void createDailyStudyAfterValidateTermination() {
        // given
        LectureSave lecture = createLectureSave("김영한의 Spring", true, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 21));
        studyService.createStudy(lecture, 1L);
        DailyStudySave dailyStudySave = new DailyStudySave(1L, "오늘 학습한 내용", 30, LocalDate.now());

        // when & then
        assertThatThrownBy(() -> dailyStudyService.createDailyStudy(dailyStudySave))
            .isInstanceOf(StudyException.class)
            .hasMessage("해당 학습은 이미 종료되었습니다.");
    }

    @DisplayName("일일 학습의 수행일이 학습 시작일 보다 빠르면 예외가 발생한다.")
    @Test
    void createDailyStudyAfterValidateStudyDate() {
        // given
        LectureSave lecture = createLectureSave("김영한의 Spring", false, LocalDate.of(2024, 4, 1), null);
        studyService.createStudy(lecture, 1L);
        DailyStudySave dailyStudySave = new DailyStudySave(1L, "오늘 학습한 내용", 30, LocalDate.of(2024, 3, 31));

        // when & then
        assertThatThrownBy(() -> dailyStudyService.createDailyStudy(dailyStudySave))
            .isInstanceOf(StudyException.class)
            .hasMessage("학습 수행일이 학습 시작일 보다 빠릅니다." + DETAIL_DELIMITER + "studyDate :: " + "2024-03-31");
    }

    private LectureSave createLectureSave(String title, boolean isTermination, LocalDate startDate, LocalDate endDate) {
        return LectureSave.builder()
            .title(title)
            .description("Spring강의")
            .isTermination(isTermination)
            .startDate(startDate)
            .endDate(endDate)
            .planMinutesInWeekday(30)
            .planMinutesInWeekend(60)
            .teacherName("김영한")
            .totalRuntime(500)
            .build();
    }
}