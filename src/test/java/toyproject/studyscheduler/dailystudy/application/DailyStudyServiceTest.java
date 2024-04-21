package toyproject.studyscheduler.dailystudy.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.dailystudy.repository.DailyStudyRepository;
import toyproject.studyscheduler.dailystudy.web.dto.DailyStudyCreation;
import toyproject.studyscheduler.dailystudy.web.dto.RemainingStudyDays;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.application.dto.LectureSave;
import toyproject.studyscheduler.study.exception.StudyException;
import toyproject.studyscheduler.study.web.dto.StudyDetail;

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
    @Autowired
    DailyStudyRepository dailyStudyRepository;

    @DisplayName("일일 학습을 생성할 때 실제 수행한 총 학습시간이 예정된 학습량 이상이 되면 학습을 종료처리한다.")
    @Test
    void creatDailyStudyWhenSatisfiedTerminationCondition() {
        // given
        LectureSave lectureSave = createLectureSave("김영한의 Spring", false, LocalDate.of(2024, 4, 1), null);
        StudyDetail studyDetail = studyService.createStudy(lectureSave, 1L);
        DailyStudySave dailyStudySave = createDailyStudySave(studyDetail.getStudyId(), 500, LocalDate.of(2024, 4, 20));

        // when
        DailyStudyCreation dailyStudyCreation = dailyStudyService.createDailyStudy(dailyStudySave);
        DailyStudy dailyStudy = dailyStudyRepository.findById(dailyStudyCreation.getDailyStudyId())
            .orElseThrow();

        // then
        assertAll(
            () -> assertTrue(dailyStudyCreation.isTermination()),
            () -> assertEquals(LocalDate.of(2024, 4, 20), dailyStudy.getStudy().getStudyPeriod().getEndDate())
        );
    }

    @DisplayName("종료된 학습의 일일 학습을 생성하면 예외가 발생한다.")
    @Test
    void createDailyStudyAfterValidateTermination() {
        // given
        LectureSave lecture = createLectureSave("김영한의 Spring", true, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 21));
        StudyDetail studyDetail = studyService.createStudy(lecture, 1L);
        DailyStudySave dailyStudySave = createDailyStudySave(studyDetail.getStudyId(), 30, LocalDate.now());

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
        StudyDetail studyDetail = studyService.createStudy(lecture, 1L);
        DailyStudySave dailyStudySave = createDailyStudySave(studyDetail.getStudyId(), 30, LocalDate.of(2024, 3, 31));

        // when & then
        assertThatThrownBy(() -> dailyStudyService.createDailyStudy(dailyStudySave))
            .isInstanceOf(StudyException.class)
            .hasMessage("학습 수행일이 학습 시작일 보다 빠릅니다." + DETAIL_DELIMITER + "studyDate :: " + "2024-03-31");
    }

    @DisplayName("일일 학습 생성 시 학습일 기준 예상 종료일을 구한다.")
    @Test
    void createDailyStudyWithExpectedEndDate() {
        // given
        LectureSave lecture = createLectureSave("김영한의 Spring", false, LocalDate.of(2024, 4, 1), null);
        StudyDetail studyDetail = studyService.createStudy(lecture, 1L);
        DailyStudySave dailyStudySave = createDailyStudySave(studyDetail.getStudyId(), 60, LocalDate.of(2024, 4, 1));

        // when
        DailyStudyCreation dailyStudyCreation = dailyStudyService.createDailyStudy(dailyStudySave);

        // then
        assertThat(dailyStudyCreation.getExpectedEndDate()).isEqualTo(LocalDate.of(2024, 4, 13));
    }

    @DisplayName("오늘 날짜를 기준으로 남은 학습 기간, 예상 종료일을 구한다.")
    @Test
    void calculateExpectedEndDate() {
        // given
        LectureSave lecture = createLectureSave("김영한의 Spring", false, LocalDate.of(2024, 4, 1), null);
        StudyDetail studyDetail = studyService.createStudy(lecture, 1L);

        DailyStudySave dailyStudySave1 = createDailyStudySave(studyDetail.getStudyId(), 30, LocalDate.of(2024, 4, 1));
        DailyStudySave dailyStudySave2 = createDailyStudySave(studyDetail.getStudyId(), 20, LocalDate.of(2024, 4, 3));
        DailyStudySave dailyStudySave3 = createDailyStudySave(studyDetail.getStudyId(), 50, LocalDate.of(2024, 4, 5));
        dailyStudyService.createDailyStudy(dailyStudySave1);
        dailyStudyService.createDailyStudy(dailyStudySave2);
        dailyStudyService.createDailyStudy(dailyStudySave3);

        LocalDate now = LocalDate.of(2024, 4, 6);

        // when
        RemainingStudyDays remainingStudyDays = dailyStudyService.calculateExpectedEndDate(studyDetail.getStudyId(), now);

        // then
        assertAll(
            () -> assertEquals(LocalDate.of(2024,4,14), studyDetail.getExpectedEndDate()), // 최초 예상 종료일
            () -> assertEquals(LocalDate.of(2024,4,15), remainingStudyDays.getExpectedEndDate())  // 학습 중 예상 종료일
        );
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

    private DailyStudySave createDailyStudySave(Long studyId, int completeMinutesToday, LocalDate studyDate) {
        return DailyStudySave.builder()
                .studyId(studyId)
                .content("오늘 학습한 내용")
                .completeMinutesToday(completeMinutesToday)
                .studyDate(studyDate)
                .build();
    }
}