package toyproject.studyscheduler.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.study.entity.Study;
import toyproject.studyscheduler.study.entity.Lecture;
import toyproject.studyscheduler.study.entity.Reading;
import toyproject.studyscheduler.member.entity.domain.AccountType;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.member.repository.MemberRepository;
import toyproject.studyscheduler.common.util.StudyUtil;

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

    @DisplayName("주어진 여러개의 아이디로 여러개의 학습 상세내용을 조회한다.")
    @Test
    void findStudiesByIds() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        StudyUtil studyUtil = new StudyUtil();

        LocalDate lectureStartDate = LocalDate.of(2023, 7, 8);
        int lecturePTD = 50;
        int lecturePTK = 90;
        int totalRuntime = 600;
        int lectureExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(lecturePTD, lecturePTK, lectureStartDate)
                .calculatePeriodBy(totalRuntime);
        Lecture lecture = createLecture(lecturePTD, lecturePTK, lectureStartDate, lectureExpectedPeriod, totalRuntime, member);

        LocalDate readingStartDate = LocalDate.of(2023, 7, 31);
        int readingPTD = 40;
        int readingPTK = 60;
        int totalPage = 500;
        int readPagePerMin = 2;
        int readingExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(readingPTD, readingPTK, readingStartDate)
                .calculatePeriodBy(totalPage, readPagePerMin);
        Reading reading = createReading(readingPTD, readingPTK, readingStartDate, readingExpectedPeriod, totalPage, readPagePerMin, member);

        studyRepository.saveAll(List.of(lecture, reading));

        // when
        List<Study> studies = studyRepository.findAllById(List.of(lecture.getId(), reading.getId()));

        // then
        assertThat(studies).hasSize(2)
                .extracting("title", "description", "startDate", "expectedEndDate")
                .containsExactlyInAnyOrder(
                        tuple("김영한의 스프링", "스프링 핵심 강의", lectureStartDate, lectureStartDate.plusDays(lectureExpectedPeriod - 1)),
                        tuple("클린 코드", "클린 코드를 배우기 위한 도서", readingStartDate, readingStartDate.plusDays(readingExpectedPeriod - 1))
                );
    }

    @DisplayName("특정기간에 수행한 학습들을 모두 조회 한다.")
    @Test
    void findStudiesByPeriod() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        StudyUtil studyUtil = new StudyUtil();

        LocalDate lectureStartDate = LocalDate.of(2023, 7, 8);
        int lecturePTD = 50;
        int lecturePTK = 90;
        int totalRuntime = 600;
        int lectureExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(lecturePTD, lecturePTK, lectureStartDate)
                .calculatePeriodBy(totalRuntime);
        Lecture lecture = createLecture(lecturePTD, lecturePTK, lectureStartDate, lectureExpectedPeriod, totalRuntime, member);

        LocalDate readingStartDate = LocalDate.of(2023, 8, 1);
        int readingPTD = 40;
        int readingPTK = 60;
        int totalPage = 500;
        int readPagePerMin = 2;
        int readingExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(readingPTD, readingPTK, readingStartDate)
                .calculatePeriodBy(totalPage, readPagePerMin);
        Reading reading = createReading(readingPTD, readingPTK, readingStartDate, readingExpectedPeriod, totalPage, readPagePerMin, member);

        studyRepository.saveAll(List.of(lecture, reading));

        // when
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);

        List<Study> studies = studyRepository.findAllByPeriod(startDate, endDate);

        // then
        assertThat(studies).hasSize(1)
                .extracting("title", "description", "startDate", "expectedEndDate")
                .containsExactlyInAnyOrder(
                        tuple("김영한의 스프링", "스프링 핵심 강의", lectureStartDate, lectureStartDate.plusDays(lectureExpectedPeriod - 1))
                );
    }

    private static Member createMember() {
        return Member.builder()
                .email("hong@gmail.com")
                .password("zxcv1234")
                .name("hong")
                .accountType(AccountType.ACTIVE)
                .build();
    }

    private Reading createReading(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, int totalExpectedPeriod, int totalPage, int readPagePerMin, Member member) {
        return Reading.builder()
                .title("클린 코드")
                .description("클린 코드를 배우기 위한 도서")
                .planTimeInWeekday(planTimeInWeekday)
                .planTimeInWeekend(planTimeInWeekend)
                .readPagePerMin(2)
                .totalExpectedPeriod(totalExpectedPeriod)
                .startDate(startDate)
                .member(member)
                .authorName("로버트 c.마틴")
                .totalPage(totalPage)
                .readPagePerMin(readPagePerMin)
                .build();
    }

    private static Lecture createLecture(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, int totalExpectedPeriod, int totalRuntime, Member member) {
        return Lecture.builder()
                .title("김영한의 스프링")
                .description("스프링 핵심 강의")
                .teacherName("김영한")
                .totalExpectedPeriod(totalExpectedPeriod)
                .planTimeInWeekday(planTimeInWeekday)
                .planTimeInWeekend(planTimeInWeekend)
                .startDate(startDate)
                .member(member)
                .totalRuntime(totalRuntime)
                .build();
    }
}