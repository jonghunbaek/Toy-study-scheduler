package toyproject.studyscheduler.study.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.study.application.dto.LectureSave;
import toyproject.studyscheduler.study.application.dto.Period;
import toyproject.studyscheduler.study.application.dto.ReadingSave;
import toyproject.studyscheduler.study.web.dto.LectureDetail;
import toyproject.studyscheduler.study.web.dto.ReadingDetail;
import toyproject.studyscheduler.study.web.dto.StudyInAction;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static toyproject.studyscheduler.study.domain.StudyPeriod.*;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class StudyServiceTest {

    @Autowired
    StudyService studyService;

    @DisplayName("종료된 강의 학습을 생성한다.")
    @Test
    void createLectureWhenTerminated() {
        // given
        LectureSave lectureSave = createLectureSaveDto("김영한의 Spring", true, LocalDate.of(2024,4,1),LocalDate.of(2024,4,21));

        // when
        LectureDetail studyDto = (LectureDetail) studyService.createStudy(lectureSave, 1L);

        // then
        assertThat(studyDto.getTeacherName()).isEqualTo("김영한");
    }

    @DisplayName("시작할 강의 학습을 생성한다.")
    @Test
    void createLectureWhenStarting() {
        // given
        LectureSave lectureSave = createLectureSaveDto("김영한의 Spring", false, LocalDate.of(2024, 4, 1), null);

        // when
        LectureDetail studyDto = (LectureDetail) studyService.createStudy(lectureSave, 1L);

        // then
        assertThat(studyDto.getTeacherName()).isEqualTo("김영한");
        assertThat(studyDto.getExpectedEndDate()).isEqualTo(LocalDate.of(2024, 4,14));
    }

    @DisplayName("종료된 독서 학습을 생성한다.")
    @Test
    void createReadingWhenTerminated() {
        // given
        ReadingSave readingSave = createReadingSaveDto("클린 코드", true, LocalDate.of(2024,4,1),LocalDate.of(2024,4,21));

        // when
        ReadingDetail studyDto = (ReadingDetail) studyService.createStudy(readingSave, 1L);

        // then
        assertThat(studyDto.getAuthorName()).isEqualTo("로버트 마틴");
    }

    @DisplayName("시작할 독서 학습을 생성한다.")
    @Test
    void createReadingWhenStarting() {
        // given
        ReadingSave readingSave = createReadingSaveDto("클린 코드", false, LocalDate.of(2024,4,1), null);

        ReadingDetail studyDto = (ReadingDetail) studyService.createStudy(readingSave, 1L);

        // then
        assertThat(studyDto.getAuthorName()).isEqualTo("로버트 마틴");
        assertThat(studyDto.getExpectedEndDate()).isEqualTo(LocalDate.of(2024, 4, 8));
    }

    @DisplayName("특정 기간에 수행한 모든 학습을 조회한다.")
    @Test
    void getStudiesByPeriod() {
        // given
        LectureSave lecture1 = createLectureSaveDto("김영한의 Spring", true, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 21));
        studyService.createStudy(lecture1, 1L);

        LectureSave lecture2 = createLectureSaveDto("김영한의 JPA", true, LocalDate.of(2024, 4, 30), LocalDate.of(2024, 5, 21));
        studyService.createStudy(lecture2, 1L);

        ReadingSave reading1 = createReadingSaveDto("클린 코드", false, LocalDate.of(2024, 3, 1), null);
        studyService.createStudy(reading1, 1L);

        ReadingSave reading2 = createReadingSaveDto("모던 자바 인액션", true, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3,21));
        studyService.createStudy(reading2, 1L);

        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 30);
        Period period = new Period(startDate, endDate);

        // when
        List<StudyInAction> studiesInAction = studyService.findStudiesByPeriod(period, 1L);

        // then
        assertThat(studiesInAction).hasSize(3)
            .extracting("title", "period.startDate", "period.endDate")
            .containsExactlyInAnyOrder(
                tuple("김영한의 Spring", LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 21)),
                tuple("김영한의 JPA", LocalDate.of(2024, 4, 30), LocalDate.of(2024, 5, 21)),
                tuple("클린 코드", LocalDate.of(2024, 3, 1), TEMP_END_DATE)
            );
    }

    private ReadingSave createReadingSaveDto(String title, boolean isTermination, LocalDate startDate, LocalDate endDate) {
        return ReadingSave.builder()
            .title(title)
            .description("클린 코드 작성 방법")
            .isTermination(isTermination)
            .startDate(startDate)
            .endDate(endDate)
            .planMinutesInWeekday(30)
            .planMinutesInWeekend(60)
            .authorName("로버트 마틴")
            .totalPage(600)
            .readPagePerMin(2)
            .build();
    }

    private LectureSave createLectureSaveDto(String title, boolean isTermination, LocalDate startDate, LocalDate endDate) {
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