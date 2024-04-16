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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class DailyStudyServiceTest {

    @Autowired
    StudyService studyService;
    @Autowired
    DailyStudyService dailyStudyService;

    @DisplayName("일일 학습을 생성한다.")
    @Test
    void createDailyStudy() {
        // given
        LectureSave lecture1 = createLectureSave("김영한의 Spring", true, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 21));
        studyService.createStudy(lecture1, 1L);
        DailyStudySave dailyStudySave = new DailyStudySave(1L, "오늘 학습한 내용", 30, LocalDate.now());

        // when
        DailyStudyCreation dailyStudyCreation = dailyStudyService.createDailyStudy(dailyStudySave);

        // then
        assertThat(dailyStudyCreation.getCompleteMinutesToday()).isEqualTo(30);
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