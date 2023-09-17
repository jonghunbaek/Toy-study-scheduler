package toyproject.studyscheduler.api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.study.repository.StudyTimeRepository;
import toyproject.studyscheduler.domain.techstack.TechCategory;
import toyproject.studyscheduler.domain.techstack.TechStack;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static toyproject.studyscheduler.domain.techstack.TechCategory.FRAMEWORK;
import static toyproject.studyscheduler.domain.techstack.TechCategory.LANGUAGE;
import static toyproject.studyscheduler.domain.function.FunctionType.CREATE;
import static toyproject.studyscheduler.domain.function.FunctionType.READ;

@ActiveProfiles("test")
@SpringBootTest
class StudyServiceTest {

    @Autowired
    StudyRepository studyRepository;
    @Autowired
    StudyTimeRepository studyTimeRepository;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("특정기간에 수행한 학습들을 모두 조회 한다.")
    @Test
    void getStudiesByPeriod() {
        // given
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);

        Member member = createMember();
        memberRepository.save(member);

        RequiredFunction function1 = createFunction(CREATE);
        RequiredFunction function2 = createFunction(READ);

        TechStack stack1 = createTechStack("Java", LANGUAGE);
        TechStack stack2 = createTechStack("Spring", FRAMEWORK);

        Lecture lecture = createLecture(startDate, endDate, member);
        Reading reading = createReading(startDate, endDate, member);
        ToyProject toyProject = createToyProject(startDate, endDate, member, List.of(function1, function2), List.of(stack1, stack2));
        studyRepository.saveAll(List.of(lecture, reading, toyProject));

        // when
        LocalDate checkStartDate = LocalDate.of(2023, 7, 1);
        LocalDate checkEndDate = LocalDate.of(2023, 7, 31);

        List<Study> studies = studyRepository.findAllByPeriod(checkStartDate, checkEndDate);

        // then
        assertThat(studies).hasSize(3)
            .extracting("title", "description", "startDate")
            .containsExactlyInAnyOrder(
                tuple("김영한의 스프링", "스프링 핵심 강의", startDate),
                tuple("클린 코드", "클린 코드를 배우기 위한 도서", startDate),
                tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", startDate)
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

    private ToyProject createToyProject(LocalDate startDate, LocalDate endDate, Member member, List<RequiredFunction> functions, List<TechStack> stacks) {
        return ToyProject.builder()
            .title("스터디 스케쥴러")
            .description("개인의 학습의 진도율을 관리")
            .totalExpectedTime(300)
            .planTimeInWeekDay(60)
            .planTimeInWeekend(120)
            .startDate(startDate)
            .endDate(endDate)
            .member(member)
            .functions(functions)
            .stacks(stacks)
            .build();
    }

    private TechStack createTechStack(String title, TechCategory techCategory) {
        return TechStack.builder()
            .title(title)
            .techCategory(techCategory)
            .build();
    }

    private RequiredFunction createFunction(FunctionType functionType) {
        return RequiredFunction.builder()
            .functionType(READ)
            .build();
    }

    private static Reading createReading(LocalDate startDate, LocalDate endDate, Member member) {
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
            .member(member)
            .build();
    }

    private static Lecture createLecture(LocalDate startDate, LocalDate endDate, Member member) {
        return Lecture.builder()
            .title("김영한의 스프링")
            .description("스프링 핵심 강의")
            .teacherName("김영한")
            .totalExpectedTime(600)
            .planTimeInWeekDay(30)
            .planTimeInWeekend(100)
            .startDate(startDate)
            .endDate(endDate)
            .member(member)
            .build();
    }
}