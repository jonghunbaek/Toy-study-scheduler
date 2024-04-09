package toyproject.studyscheduler.study.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.study.application.dto.LectureSaveDto;
import toyproject.studyscheduler.study.application.dto.ReadingSaveDto;
import toyproject.studyscheduler.study.web.dto.LectureCreationDto;
import toyproject.studyscheduler.study.web.dto.ReadingCreationDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

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
        LectureSaveDto lectureSaveDto = createLectureSaveDto("김영한의 Spring", true, LocalDate.of(2024,4,1),LocalDate.of(2024,4,21));

        // when
        LectureCreationDto studyDto = (LectureCreationDto) studyService.createStudy(lectureSaveDto, 1L);

        // then
        assertThat(studyDto.getTeacherName()).isEqualTo("김영한");
    }

    @DisplayName("시작할 강의 학습을 생성한다.")
    @Test
    void createLectureWhenStarting() {
        // given
        LectureSaveDto lectureSaveDto = createLectureSaveDto("김영한의 Spring", false, LocalDate.of(2024, 4, 1), null);

        // when
        LectureCreationDto studyDto = (LectureCreationDto) studyService.createStudy(lectureSaveDto, 1L);

        // then
        assertThat(studyDto.getTeacherName()).isEqualTo("김영한");
        assertThat(studyDto.getExpectedEndDate()).isEqualTo(LocalDate.of(2024, 4,14));
    }

    @DisplayName("종료된 독서 학습을 생성한다.")
    @Test
    void createReadingWhenTerminated() {
        // given
        ReadingSaveDto readingSaveDto = createReadingSaveDto("클린 코드", true, LocalDate.of(2024,4,1),LocalDate.of(2024,4,21));

        // when
        ReadingCreationDto studyDto = (ReadingCreationDto) studyService.createStudy(readingSaveDto, 1L);

        // then
        assertThat(studyDto.getAuthorName()).isEqualTo("로버트 마틴");
    }

    @DisplayName("시작할 독서 학습을 생성한다.")
    @Test
    void createReadingWhenStarting() {
        // given
        ReadingSaveDto readingSaveDto = createReadingSaveDto("클린 코드", false, LocalDate.of(2024,4,1), null);

        ReadingCreationDto studyDto = (ReadingCreationDto) studyService.createStudy(readingSaveDto, 1L);

        // then
        assertThat(studyDto.getAuthorName()).isEqualTo("로버트 마틴");
        assertThat(studyDto.getExpectedEndDate()).isEqualTo(LocalDate.of(2024, 4, 8));
    }

    @DisplayName("특정 기간에 수행한 모든 학습을 조회한다.")
    @Test
    void getStudiesByPeriod() {
        // given
        LectureSaveDto lecture1 = createLectureSaveDto("김영한의 Spring", true, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 21));
        studyService.createStudy(lecture1, 1L);

        LectureSaveDto lecture2 = createLectureSaveDto("김영한의 JPA", true, LocalDate.of(2024, 4, 30), LocalDate.of(2024, 5, 21));
        studyService.createStudy(lecture2, 1L);

        ReadingSaveDto reading1 = createReadingSaveDto("클린 코드", false, LocalDate.of(2024, 3, 1), null);
        studyService.createStudy(reading1, 1L);

        ReadingSaveDto reading2 = createReadingSaveDto("모던 자바 인액션", true, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3,21));
        studyService.createStudy(reading2, 1L);

        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 30);

        // when
        

        // then

    }

    private ReadingSaveDto createReadingSaveDto(String title, boolean isTermination, LocalDate startDate, LocalDate endDate) {
        return ReadingSaveDto.builder()
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

    private LectureSaveDto createLectureSaveDto(String title, boolean isTermination, LocalDate startDate, LocalDate endDate) {
        return LectureSaveDto.builder()
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