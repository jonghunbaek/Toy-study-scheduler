package toyproject.studyscheduler.domain.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.requiredfunction.FunctionType;
import toyproject.studyscheduler.domain.study.requiredfunction.RequiredFunction;
import toyproject.studyscheduler.domain.toyproject.ToyProject;
import toyproject.studyscheduler.domain.user.AccountType;
import toyproject.studyscheduler.domain.user.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class StudyRepositoryTest {

    @Autowired
    StudyRepository studyRepository;

    @DisplayName("주어진 기간에 수행한 학습들을 모두 조회 한다.")
    @Test
    void getStudiesByPeriod() {
        // given
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);

        User user = createUser();
        ToyProject toyProject = createToyProject();
        Study lecture = createLecture(startDate, endDate, user);
        Study reading = createReading(startDate, endDate, user);
        Study requiredFunction = createFunction(startDate, endDate, user, toyProject);
        studyRepository.saveAll(List.of(lecture, reading, requiredFunction));

        // when 
        LocalDate checkStartDate = LocalDate.of(2023, 7, 1);
        LocalDate checkEndDate = LocalDate.of(2023, 7, 31);

        List<Study> studies = studyRepository.findAllByPeriodIn(checkStartDate, checkEndDate);

        // then
        assertThat(studies).hasSize(3)
                .extracting("title", "description", "startDate")
                .containsExactlyInAnyOrder(
                        tuple("김영한의 스프링", "스프링 핵심 강의", startDate),
                        tuple("클린 코드", "클린 코드를 배우기 위한 도서", startDate),
                        tuple("강의 조회", "강의를 조회한다.", startDate)
                );
     }

    private ToyProject createToyProject() {
        return ToyProject.builder()
                .title("스터디 스케쥴러")
                .description("개인의 학습의 진도율을 관리")
                .build();
    }

    private RequiredFunction createFunction(LocalDate startDate, LocalDate endDate, User user, ToyProject toyProject) {
        return RequiredFunction.builder()
                .title("강의 조회")
                .description("강의를 조회한다.")
                .functionType(FunctionType.READ)
                .totalExpectedTime(300)
                .planTimeInWeekDay(60)
                .planTimeInWeekend(120)
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .toyProject(toyProject)
                .build();
    }

    private static Reading createReading(LocalDate startDate, LocalDate endDate, User user) {
        return Reading.builder()
                .title("클린 코드")
                .authorName("로버트 c.마틴")
                .description("클린 코드를 배우기 위한 도서")
                .totalPage(500)
                .planTimeInWeekDay(30)
                .planTimeInWeekend(30)
                .readPagePerMin(2)
                .totalExpectedTime(250)
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .build();
    }

    private static Lecture createLecture(LocalDate startDate, LocalDate endDate, User user) {
        return Lecture.builder()
                .title("김영한의 스프링")
                .description("스프링 핵심 강의")
                .teacherName("김영한")
                .totalExpectedTime(600)
                .planTimeInWeekDay(30)
                .planTimeInWeekend(100)
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .build();
    }

    private static User createUser() {
        return User.builder()
                .email("hong@gmail.com")
                .password("zxcv1234")
                .name("hong")
                .accountType(AccountType.ACTIVE)
                .originProfileImage("1234")
                .storedProfileImage("4151")
                .build();
    }
}