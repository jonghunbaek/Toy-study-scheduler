package toyproject.studyscheduler.study.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.entity.Study;
import toyproject.studyscheduler.study.domain.entity.Lecture;
import toyproject.studyscheduler.study.domain.entity.Reading;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class StudyRepositoryTest {

    @Autowired
    StudyRepository studyRepository;
    @Autowired
    MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = createMember();
        memberRepository.save(member);
    }

    @DisplayName("종료된 학습을 저장한다.")
    @Test
    void saveTerminatedStudy() {
        // given
        StudyInformation studyInformation = createInformation("클린 코드", "클린 코드를 작성하는 방법", true);
        StudyPeriod period = StudyPeriod.fromTerminated(LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 10));
        StudyPlan plan = StudyPlan.fromTerminated();
        Reading reading = createReading(studyInformation, period, plan, member);

        // when
        Reading savedReading = studyRepository.save(reading);

        // then
        assertThat(savedReading.getAuthorName()).isEqualTo("로버트 마틴");
    }

    @DisplayName("시작할 학습을 저장한다.")
    @Test
    void saveStartingStudy() {
        // given
        StudyInformation studyInformation = createInformation("클린 코드", "클린 코드를 작성하는 방법", true);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 1));
        StudyPlan plan = StudyPlan.fromStarting(30, 60);
        Reading reading = createReading(studyInformation, period, plan, member);

        // when
        Reading savedReading = studyRepository.save(reading);

        // then
        assertThat(savedReading.getAuthorName()).isEqualTo("로버트 마틴");
    }

    @DisplayName("특정 기간에 수행한 학습들을 모두 조회 한다.")
    @Test
    void findStudiesByPeriod() {
        // given
        StudyInformation lectureInformation1 = createInformation("김영한의 JPA", "JPA강의", true);
        StudyPeriod lecturePeriod1 = StudyPeriod.fromTerminated(LocalDate.of(2024, 3, 3), LocalDate.of(2024, 3, 31));
        StudyPlan lecturePlan1 = StudyPlan.fromTerminated();
        Lecture lecture1 = createLecture(lectureInformation1, lecturePeriod1, lecturePlan1, member);

        StudyInformation lectureInformation2 = createInformation("김영한의 Spring", "Spring강의", false);
        StudyPeriod lecturePeriod2 = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 3));
        StudyPlan lecturePlan2 = StudyPlan.fromStarting(30, 60);
        Lecture lecture2 = createLecture(lectureInformation2, lecturePeriod2, lecturePlan2, member);

        StudyInformation readingInformation = createInformation("클린 코드", "클린 코드를 작성하는 방법", true);
        StudyPeriod readingPeriod = StudyPeriod.fromTerminated(LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 10));
        StudyPlan readingPlan = StudyPlan.fromTerminated();
        Reading reading = createReading(readingInformation, readingPeriod, readingPlan, member);

        studyRepository.saveAll(List.of(lecture1, lecture2, reading));

        // when
        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 30);

        List<Study> studies = studyRepository.findAllByPeriod(startDate, endDate);

        // then
        assertThat(studies).hasSize(2)
                .extracting("studyInformation.title", "studyInformation.description", "studyPeriod.startDate", "studyPeriod.endDate")
                .containsExactlyInAnyOrder(
                        tuple("김영한의 Spring", "Spring강의", LocalDate.of(2024, 4, 3), LocalDate.of(9999,12,31)),
                        tuple("클린 코드", "클린 코드를 작성하는 방법", LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 10))
                );
    }

    private StudyInformation createInformation(String title, String description, boolean isTermination) {
        return StudyInformation.builder()
            .title(title)
            .description(description)
            .isTermination(isTermination)
            .build();
    }

    private Lecture createLecture(StudyInformation information, StudyPeriod period, StudyPlan plan, Member member) {
        return Lecture.builder()
            .studyInformation(information)
            .studyPeriod(period)
            .studyPlan(plan)
            .teacherName("김영한")
            .totalRuntime(447)
            .member(member)
            .build();
    }

    private Reading createReading(StudyInformation information, StudyPeriod period, StudyPlan plan, Member member) {
        return Reading.builder()
            .studyInformation(information)
            .studyPeriod(period)
            .studyPlan(plan)
            .authorName("로버트 마틴")
            .readPagePerMin(2)
            .totalPage(600)
            .member(member)
            .build();
    }

    private static Member createMember() {
        return Member.builder()
                .email("hong@gmail.com")
                .password("zxcv1234")
                .name("hong")
                .build();
    }
}