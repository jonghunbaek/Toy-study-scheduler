package toyproject.studyscheduler.domain.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.techstack.TechCategory;
import toyproject.studyscheduler.domain.techstack.TechStack;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.util.StudyUtil;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static toyproject.studyscheduler.domain.function.FunctionType.*;

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

        LocalDate toyStartDate = LocalDate.of(2023, 7, 1);
        int toyPTD = 60;
        int toyPTK = 120;
        List<RequiredFunction> functions = List.of(createFunction(CREATE, 300), createFunction(READ, 500));
        List<Integer> totalExpectedTime = functions.stream()
                .map(RequiredFunction::getExpectedTime)
                .toList();
        int toyExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(toyPTD, toyPTK, toyStartDate)
                .calculatePeriodBy(totalExpectedTime);
        ToyProject toyProject = createToyProject(toyPTD, toyPTK, toyStartDate, toyExpectedPeriod, functions, member);

        studyRepository.saveAll(List.of(lecture, reading, toyProject));

        // when
        List<Study> studies = studyRepository.findAllById(List.of(lecture.getId(), reading.getId(), toyProject.getId()));

//        studies.forEach(study -> System.out.println("dtype TEST ==============" + study.getSubType()));
        // then
        assertThat(studies).hasSize(3)
                .extracting("title", "description", "startDate", "expectedEndDate")
                .containsExactlyInAnyOrder(
                        tuple("김영한의 스프링", "스프링 핵심 강의", lectureStartDate, lectureStartDate.plusDays(lectureExpectedPeriod - 1)),
                        tuple("클린 코드", "클린 코드를 배우기 위한 도서", readingStartDate, readingStartDate.plusDays(readingExpectedPeriod - 1)),
                        tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", toyStartDate, toyStartDate.plusDays(toyExpectedPeriod - 1))
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

        LocalDate toyStartDate = LocalDate.of(2023, 7, 1);
        int toyPTD = 60;
        int toyPTK = 120;
        List<RequiredFunction> functions = List.of(createFunction(CREATE, 300), createFunction(READ, 500));
        List<Integer> totalExpectedTime = functions.stream()
                .map(RequiredFunction::getExpectedTime)
                .toList();
        int toyExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(toyPTD, toyPTK, toyStartDate)
                .calculatePeriodBy(totalExpectedTime);
        ToyProject toyProject = createToyProject(toyPTD, toyPTK, toyStartDate, toyExpectedPeriod, functions, member);

        studyRepository.saveAll(List.of(lecture, reading, toyProject));

        // when
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);

        List<Study> studies = studyRepository.findAllByPeriod(startDate, endDate);

        // then
        assertThat(studies).hasSize(2)
                .extracting("title", "description", "startDate", "expectedEndDate")
                .containsExactlyInAnyOrder(
                        tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", toyStartDate, toyStartDate.plusDays(toyExpectedPeriod - 1)),
                        tuple("김영한의 스프링", "스프링 핵심 강의", lectureStartDate, lectureStartDate.plusDays(lectureExpectedPeriod - 1))
                );
    }

    private static Member createMember() {
        return Member.builder()
                .email("hong@gmail.com")
                .password("zxcv1234")
                .name("hong")
                .accountType(AccountType.ACTIVE)
                .originProfileImage("1234")
                .storedProfileImage("4151")
                .build();
    }

    private ToyProject createToyProject(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, int totalExpectedPeriod, List<RequiredFunction> functions,  Member member) {
        return ToyProject.builder()
                .title("스터디 스케쥴러")
                .description("개인의 학습의 진도율을 관리")
                .totalExpectedPeriod(totalExpectedPeriod)
                .planTimeInWeekday(planTimeInWeekday)
                .planTimeInWeekend(planTimeInWeekend)
                .startDate(startDate)
                .member(member)
                .functions(functions)
                .build();
    }

    private TechStack createTechStack(String title, TechCategory techCategory, ToyProject toyProject) {
        return TechStack.builder()
                .title(title)
                .techCategory(techCategory)
                .toyProject(toyProject)
                .build();
    }

    private RequiredFunction createFunction(FunctionType functionType, int expectedTime) {
        return RequiredFunction.builder()
                .functionType(functionType)
                .expectedTime(expectedTime)
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