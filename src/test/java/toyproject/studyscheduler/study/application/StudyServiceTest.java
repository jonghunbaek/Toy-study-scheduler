package toyproject.studyscheduler.study.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.study.application.dto.LectureSave;
import toyproject.studyscheduler.study.application.dto.LectureUpdate;
import toyproject.studyscheduler.study.application.dto.Period;
import toyproject.studyscheduler.study.application.dto.ReadingSave;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.exception.StudyException;
import toyproject.studyscheduler.study.web.dto.LectureDetail;
import toyproject.studyscheduler.study.web.dto.ReadingDetail;
import toyproject.studyscheduler.study.web.dto.StudyDetail;
import toyproject.studyscheduler.study.web.dto.StudyInAction;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
        LectureSave lectureSave = createLectureSave("김영한의 Spring", true, LocalDate.of(2024,4,1),LocalDate.of(2024,4,21));

        // when
        LectureDetail studyDto = (LectureDetail) studyService.createStudy(lectureSave, 1L);

        // then
        assertThat(studyDto.getTeacherName()).isEqualTo("김영한");
    }

    @DisplayName("시작할 강의 학습을 생성한다.")
    @Test
    void createLectureWhenStarting() {
        // given
        LectureSave lectureSave = createLectureSave("김영한의 Spring", false, LocalDate.of(2024, 4, 1), null);

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
        ReadingSave readingSave = createReadingSave("클린 코드", true, LocalDate.of(2024,4,1),LocalDate.of(2024,4,21));

        // when
        ReadingDetail studyDto = (ReadingDetail) studyService.createStudy(readingSave, 1L);

        // then
        assertThat(studyDto.getAuthorName()).isEqualTo("로버트 마틴");
    }

    @DisplayName("시작할 독서 학습을 생성한다.")
    @Test
    void createReadingWhenStarting() {
        // given
        ReadingSave readingSave = createReadingSave("클린 코드", false, LocalDate.of(2024,4,1), null);

        ReadingDetail studyDto = (ReadingDetail) studyService.createStudy(readingSave, 1L);

        // then
        assertThat(studyDto.getAuthorName()).isEqualTo("로버트 마틴");
        assertThat(studyDto.getExpectedEndDate()).isEqualTo(LocalDate.of(2024, 4, 8));
    }

    @DisplayName("특정 기간에 수행한 모든 학습을 조회한다.")
    @Test
    void getStudiesByPeriod() {
        // given
        LectureSave lecture1 = createLectureSave("김영한의 Spring", true, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 21));
        studyService.createStudy(lecture1, 1L);

        LectureSave lecture2 = createLectureSave("김영한의 JPA", true, LocalDate.of(2024, 4, 30), LocalDate.of(2024, 5, 21));
        studyService.createStudy(lecture2, 1L);

        ReadingSave reading1 = createReadingSave("클린 코드", false, LocalDate.of(2024, 3, 1), null);
        studyService.createStudy(reading1, 1L);

        ReadingSave reading2 = createReadingSave("모던 자바 인액션", true, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3,21));
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

    @DisplayName("학습 아이디에 해당하는 학습을 조회할 때 존재하지 않으면 예외를 던진다.")
    @Test
    void findByIdWhenNotExists() {
        // when & then
        assertThatThrownBy(() -> studyService.findStudyById(1L))
            .isInstanceOf(StudyException.class)
            .hasMessage("일치하는 학습을 찾을 수 없습니다. detail => studyId :: 1");
    }

    @DisplayName("종료되지 않은 학습 내용을 수정한다.")
    @Test
    void updateLecture() {
        // given
        LectureSave lectureSave = createLectureSave("변경 전 제목", false, LocalDate.of(2024, 4, 1), null);
        StudyDetail studyDetail = studyService.createStudy(lectureSave, 1L);

        LectureUpdate lectureUpdate = createLectureUpdate(LocalDate.of(2024, 4, 11), null, 10, 30);

        // when
        studyService.updateStudy(lectureUpdate);
        StudyDetail updatedStudy = studyService.findStudyById(studyDetail.getStudyId());

        // then
        assertAll(
                () -> assertEquals("변경 후 제목", updatedStudy.getTitle()),
                () -> assertEquals(LocalDate.of(2024, 4, 11), updatedStudy.getStartDate()),
                () -> assertEquals(10, updatedStudy.getPlanMinutesInWeekday()),
                () -> assertEquals(30, updatedStudy.getPlanMinutesInWeekend()),
                () -> assertEquals(TEMP_END_DATE, studyDetail.getEndDate())
        );
    }

    private LectureUpdate createLectureUpdate(LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend) {
        return LectureUpdate.builder()
                .studyId(1L)
                .title("변경 후 제목")
                .description("변경 후 내용")
                .startDate(startDate)
                .endDate(endDate)
                .planMinutesInWeekday(planMinutesInWeekday)
                .planMinutesInWeekend(planMinutesInWeekend)
                .teacherName("강사 이름 변경 후")
                .totalRuntime(500)
                .build();
    }

    private ReadingSave createReadingSave(String title, boolean isTermination, LocalDate startDate, LocalDate endDate) {
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