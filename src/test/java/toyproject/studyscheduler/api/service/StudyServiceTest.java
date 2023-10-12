package toyproject.studyscheduler.api.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.api.controller.request.SaveStudyRequestDto;
import toyproject.studyscheduler.api.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.domain.function.RequiredFunctionRepository;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.lecture.LectureRepository;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.reading.ReadingRepository;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.study.toyproject.ToyProjectRepository;
import toyproject.studyscheduler.domain.techstack.TechCategory;
import toyproject.studyscheduler.domain.techstack.TechStack;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.techstack.TechStackRepository;
import toyproject.studyscheduler.util.StudyUtil;

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
    StudyService studyService;
    @Autowired
    StudyRepository studyRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    ReadingRepository readingRepository;
    @Autowired
    ToyProjectRepository toyProjectRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RequiredFunctionRepository requiredFunctionRepository;
    @Autowired
    TechStackRepository techStackRepository;
    @Autowired
    StudyUtil studyUtil;

    @AfterEach
    void cleanUp() {
        requiredFunctionRepository.deleteAllInBatch();
        techStackRepository.deleteAllInBatch();
        studyRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("여러 종류의 학습을 저장할 때 Discriminator 타입을 구분해서 저장한다.")
    @Test
    void saveStudyAccordingToType() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        int planTimeInWeekday = 60;
        int planTimeInWeekend = 120;
        LocalDate startDate1 = LocalDate.of(2023, 9, 10);
        int totalRuntime = 600;
        int totalExpectedPeriod1 = studyUtil.setUpPeriodCalCulatorBy(planTimeInWeekday, planTimeInWeekend, startDate1)
            .calculatePeriodBy(totalRuntime);

        SaveStudyRequestDto lectureDto = SaveStudyRequestDto.builder()
            .studyType("lecture")
            .title("김영한의 스프링")
            .description("스프링 핵심 강의")
            .teacherName("김영한")
            .totalExpectedPeriod(totalExpectedPeriod1)
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate1)
            .totalRuntime(totalRuntime)
            .memberId(savedMember.getId())
            .build();

        planTimeInWeekday = 30;
        planTimeInWeekend = 60;
        LocalDate startDate2 = LocalDate.of(2023, 9, 11);
        int totalPage = 700;
        int readPagePerMin = 2;
        int totalExpectedPeriod2 = studyUtil.setUpPeriodCalCulatorBy(planTimeInWeekday, planTimeInWeekend, startDate2)
            .calculatePeriodBy(totalPage, readPagePerMin);

        SaveStudyRequestDto readingDto = SaveStudyRequestDto.builder()
            .studyType("reading")
            .title("클린 코드")
            .description("클린한 코드를 통해 유지보수성을 높이자")
            .authorName("로버트 c.마틴")
            .totalExpectedPeriod(totalExpectedPeriod2)
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate2)
            .totalPage(totalPage)
            .readPagePerMin(readPagePerMin)
            .memberId(savedMember.getId())
            .build();

        // when
        studyService.saveStudy(lectureDto);
        studyService.saveStudy(readingDto);

        List<Study> studies = studyRepository.findAll();

        // then
        assertThat(studies).hasSize(2)
            .extracting("studyType", "title", "description", "planTimeInWeekday", "planTimeInWeekend", "startDate", "expectedEndDate")
            .containsExactlyInAnyOrder(
                tuple("lecture", "김영한의 스프링", "스프링 핵심 강의", 60, 120, startDate1, startDate1.plusDays(totalExpectedPeriod1)),
                tuple("reading", "클린 코드", "클린한 코드를 통해 유지보수성을 높이자", 30, 60, startDate2, startDate2.plusDays(totalExpectedPeriod2))
            );

        assertThat(studies.get(0)).extracting("teacherName", "totalRuntime")
                .contains("김영한", 600);
        assertThat(studies.get(1)).extracting("authorName", "readPagePerMin")
                .contains("로버트 c.마틴", 2);

    }

    @DisplayName("주어진 아이디로 종료되지 않은 학습 상세내용을 조회한다.")
    @Test
    void findStudyById() {
        // given
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);

        Member member = createMember();
        memberRepository.save(member);

        Lecture lecture = createLecture(startDate, endDate, member);
        System.out.println(lecture.getRealEndDate());
        Lecture savedLecture = studyRepository.save(lecture);

        // when
        FindStudyResponseDto study = studyService.findStudyById(savedLecture.getId());

        // then
        assertThat(study)
            .extracting("title", "description", "isTermination", "realEndDate")
            .contains("김영한의 스프링", "스프링 핵심 강의", false, LocalDate.EPOCH);
    }

    @DisplayName("주어진 아이디로 종료된 학습 상세내용을 조회한다.")
    @Test
    void findStudyByIdWithTerminating() {
        // given
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);
        LocalDate realEndDate = LocalDate.of(2023, 8, 10);

        Member member = createMember();
        memberRepository.save(member);

        Lecture lecture = createTerminatedLecture(startDate, endDate, realEndDate, member);
        Lecture savedLecture = studyRepository.save(lecture);

        // when
        FindStudyResponseDto study = studyService.findStudyById(savedLecture.getId());

        // then
        assertThat(study)
                .extracting("title", "description", "isTermination", "realEndDate")
                .contains("김영한의 스프링", "스프링 핵심 강의", true, LocalDate.of(2023,8,10));
    }

    @DisplayName("특정기간에 수행한 학습들을 모두 조회 한다.")
    @Test
    void findStudiesByPeriod() {
        // given
        LocalDate startDate1 = LocalDate.of(2023, 7, 1);
        LocalDate endDate1 = LocalDate.of(2023, 8, 3);
        LocalDate startDate2 = LocalDate.of(2023, 7, 8);
        LocalDate endDate2 = LocalDate.of(2023, 7, 16);
        LocalDate startDate3 = LocalDate.of(2023, 7, 31);
        LocalDate endDate3 = LocalDate.of(2023, 8, 30);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject = createToyProject(startDate1, endDate1, member);
        RequiredFunction function1 = createFunction(CREATE, toyProject);
        RequiredFunction function2 = createFunction(READ, toyProject);
        TechStack stack1 = createTechStack("Java", LANGUAGE, toyProject);
        TechStack stack2 = createTechStack("Spring", FRAMEWORK, toyProject);

        Lecture lecture = createLecture(startDate2, endDate2, member);
        Reading reading = createReading(startDate3, endDate3, member);
        studyRepository.saveAll(List.of(lecture, reading));
        toyProjectRepository.save(toyProject);

        // when
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);

        List<Study> studies = studyRepository.findAllByPeriod(startDate, endDate);

        // then
        assertThat(studies).hasSize(3)
            .extracting("title", "description", "startDate")
            .containsExactlyInAnyOrder(
                tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", startDate1),
                tuple("김영한의 스프링", "스프링 핵심 강의", startDate2),
                tuple("클린 코드", "클린 코드를 배우기 위한 도서", startDate3)
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

    private ToyProject createToyProject(LocalDate startDate, LocalDate endDate, Member member) {
        return ToyProject.builder()
            .title("스터디 스케쥴러")
            .description("개인의 학습의 진도율을 관리")
            .totalExpectedPeriod(300)
            .planTimeInWeekday(60)
            .planTimeInWeekend(120)
            .startDate(startDate)
            .expectedEndDate(endDate)
            .member(member)
            .build();
    }

    private TechStack createTechStack(String title, TechCategory techCategory, ToyProject toyProject) {
        return TechStack.builder()
            .title(title)
            .techCategory(techCategory)
            .toyProject(toyProject)
            .build();
    }

    private RequiredFunction createFunction(FunctionType functionType, ToyProject toyProject) {
        return RequiredFunction.builder()
            .functionType(READ)
            .toyProject(toyProject)
            .build();
    }

    private static Reading createReading(LocalDate startDate, LocalDate endDate, Member member) {
        return Reading.builder()
            .title("클린 코드")
            .authorName("로버트 c.마틴")
            .description("클린 코드를 배우기 위한 도서")
            .totalPage(500)
            .planTimeInWeekday(30)
            .planTimeInWeekend(30)
            .readPagePerMin(2)
            .totalExpectedPeriod(250)
            .startDate(startDate)
            .expectedEndDate(endDate)
            .member(member)
            .build();
    }

    private static Lecture createLecture(LocalDate startDate, LocalDate endDate, Member member) {
        return Lecture.builder()
            .title("김영한의 스프링")
            .description("스프링 핵심 강의")
            .teacherName("김영한")
            .totalExpectedPeriod(600)
            .planTimeInWeekday(30)
            .planTimeInWeekend(100)
            .startDate(startDate)
            .expectedEndDate(endDate)
            .member(member)
            .build();
    }

    private static Lecture createTerminatedLecture(LocalDate startDate, LocalDate endDate, LocalDate realEndDate, Member member) {
        return Lecture.builder()
                .title("김영한의 스프링")
                .description("스프링 핵심 강의")
                .teacherName("김영한")
                .totalExpectedPeriod(600)
                .planTimeInWeekday(30)
                .planTimeInWeekend(100)
                .startDate(startDate)
                .expectedEndDate(endDate)
                .member(member)
                .isTermination(true)
                .realEndDate(realEndDate)
                .build();
    }
}