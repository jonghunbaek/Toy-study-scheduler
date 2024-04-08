package toyproject.studyscheduler.study.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.study.application.dto.LectureSaveDto;
import toyproject.studyscheduler.study.application.dto.ReadingSaveDto;
import toyproject.studyscheduler.study.web.dto.LectureCreationDto;
import toyproject.studyscheduler.study.web.dto.ReadingCreationDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class StudyServiceTest {

    @Autowired
    StudyService studyService;

    @DisplayName("종료된 강의 학습을 생성한다.")
    @Test
    void createLectureWhenTerminated() {
        // given
        LectureSaveDto lectureSaveDto = LectureSaveDto.builder()
            .title("김영한의 Spring")
            .description("Spring강의")
            .isTermination(true)
            .startDate(LocalDate.of(2024, 4, 1))
            .endDate(LocalDate.of(2024, 4, 21))
            .planMinutesInWeekday(30)
            .planMinutesInWeekend(60)
            .teacherName("김영한")
            .totalRuntime(500)
            .build();

        // when
        LectureCreationDto studyDto = (LectureCreationDto) studyService.createStudy(lectureSaveDto);

        // then
        assertThat(studyDto.getTeacherName()).isEqualTo("김영한");
    }

    @DisplayName("시작할 강의 학습을 생성한다.")
    @Test
    void createLectureWhenStarting() {
        // given
        LectureSaveDto lectureSaveDto = LectureSaveDto.builder()
            .title("김영한의 Spring")
            .description("Spring강의")
            .isTermination(false)
            .startDate(LocalDate.of(2024, 4, 1))
            .planMinutesInWeekday(30)
            .planMinutesInWeekend(60)
            .teacherName("김영한")
            .totalRuntime(500)
            .build();

        // when
        LectureCreationDto studyDto = (LectureCreationDto) studyService.createStudy(lectureSaveDto);

        // then
        assertThat(studyDto.getTeacherName()).isEqualTo("김영한");
        assertThat(studyDto.getExpectedEndDate()).isEqualTo(LocalDate.of(2024, 4,14));
    }

    @DisplayName("종료된 강의 학습을 생성한다.")
    @Test
    void createReadingWhenTerminated() {
        // given
        ReadingSaveDto lectureSaveDto = ReadingSaveDto.builder()
            .title("클린 코드")
            .description("클린 코드 작성 방법")
            .isTermination(true)
            .startDate(LocalDate.of(2024, 4, 1))
            .endDate(LocalDate.of(2024, 4, 21))
            .planMinutesInWeekday(30)
            .planMinutesInWeekend(60)
            .authorName("로버트 마틴")
            .totalPage(600)
            .readPagePerMin(2)
            .build();

        // when
        ReadingCreationDto studyDto = (ReadingCreationDto) studyService.createStudy(lectureSaveDto);

        // then
        assertThat(studyDto.getAuthorName()).isEqualTo("로버트 마틴");
    }

    @DisplayName("시작할 강의 학습을 생성한다.")
    @Test
    void createReadingWhenStarting() {
        // given
        ReadingSaveDto lectureSaveDto = ReadingSaveDto.builder()
            .title("클린 코드")
            .description("클린 코드 작성 방법")
            .isTermination(false)
            .startDate(LocalDate.of(2024, 4, 1))
            .planMinutesInWeekday(30)
            .planMinutesInWeekend(60)
            .authorName("로버트 마틴")
            .totalPage(600)
            .readPagePerMin(2)
            .build();

        ReadingCreationDto studyDto = (ReadingCreationDto) studyService.createStudy(lectureSaveDto);

        // then
        assertThat(studyDto.getAuthorName()).isEqualTo("로버트 마틴");
        assertThat(studyDto.getExpectedEndDate()).isEqualTo(LocalDate.of(2024, 4, 8));
    }
}