package toyproject.studyscheduler.api.service.study;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.api.controller.request.SaveRequiredFunctionDto;
import toyproject.studyscheduler.api.controller.request.study.SaveStudyRequestDto;
import toyproject.studyscheduler.api.controller.request.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.api.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.domain.function.RequiredFunctionRepository;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.studytime.repository.StudyTimeRepository;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.techstack.TechStackRepository;
import toyproject.studyscheduler.util.StudyUtil;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static toyproject.studyscheduler.domain.function.FunctionType.*;
import static toyproject.studyscheduler.domain.study.StudyType.*;

@ActiveProfiles("test")
@SpringBootTest
class StudyServiceTest {

    @Autowired
    StudyFactory studyFactory;
    @Autowired
    StudyRepository studyRepository;
    @Autowired
    StudyTimeRepository studyTimeRepository;
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
        studyTimeRepository.deleteAllInBatch();
        studyRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("강의 학습을 저장하고 조회한다.")
    @Test
    void saveLecture() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        int planTimeInWeekday = 60;
        int planTimeInWeekend = 120;
        LocalDate startDate = LocalDate.of(2023, 9, 15);
        int totalRuntime = 600;
        int totalExpectedPeriod1 = studyUtil.setUpPeriodCalCulatorBy(planTimeInWeekday, planTimeInWeekend, startDate)
            .calculatePeriodBy(totalRuntime);

        SaveStudyRequestDto lectureDto = SaveStudyRequestDto.builder()
            .studyType(LECTURE)
            .title("김영한의 스프링")
            .description("스프링 핵심 강의")
            .teacherName("김영한")
            .totalExpectedPeriod(totalExpectedPeriod1)
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .totalRuntime(totalRuntime)
            .memberId(savedMember.getId())
            .build();

        // when
        StudyService studyService = studyFactory.findServiceBy(lectureDto.getStudyType());
        studyService.saveStudy(lectureDto);

        Lecture lecture = (Lecture) studyRepository.findAll().get(0);

        // then
        assertThat(lecture).extracting("title", "description", "totalExpectedPeriod", "totalExpectedMin", "expectedEndDate")
            .contains("김영한의 스프링", "스프링 핵심 강의", 8, 600, LocalDate.of(2023, 9,22));
    }

    @DisplayName("독서 학습을 저장하고 조회한다.")
    @Test
    void saveStudyAccordingToType() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);


        int planTimeInWeekday = 30;
        int planTimeInWeekend = 60;
        LocalDate startDate = LocalDate.of(2023, 9, 11);
        int totalPage = 700;
        int readPagePerMin = 2;
        int totalExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(planTimeInWeekday, planTimeInWeekend, startDate)
            .calculatePeriodBy(totalPage, readPagePerMin);

        SaveStudyRequestDto readingDto = SaveStudyRequestDto.builder()
            .studyType(READING)
            .title("클린 코드")
            .description("클린한 코드를 통해 유지보수성을 높이자")
            .authorName("로버트 c.마틴")
            .totalExpectedPeriod(totalExpectedPeriod)
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .totalPage(totalPage)
            .readPagePerMin(readPagePerMin)
            .memberId(savedMember.getId())
            .build();
        StudyService studyService = studyFactory.findServiceBy(readingDto.getStudyType());

        // when
        studyService.saveStudy(readingDto);

        Reading reading = (Reading) studyRepository.findAll().get(0);

        // then
        assertThat(reading).extracting("title", "description", "totalExpectedPeriod", "totalExpectedMin", "expectedEndDate")
            .contains("클린 코드", "클린한 코드를 통해 유지보수성을 높이자", 10, 350, LocalDate.of(2023, 9,20));
    }

    @DisplayName("토이프로젝트 학습을 저장하고 조회한다.")
    @Test
    void saveToyProject() {
        // given
        Member member = createMember();
        Member savedMember = memberRepository.save(member);

        SaveRequiredFunctionDto addMember = SaveRequiredFunctionDto.builder()
            .title("회원 가입")
            .description("신규 회원을 생성한다.")
            .functionType(CREATE)
            .expectedTime(70)
            .build();
        SaveRequiredFunctionDto loginMember = SaveRequiredFunctionDto.builder()
            .title("로그인")
            .description("회원의 존재여부를 확인 후 존재하면 로그인을 한다.")
            .functionType(READ)
            .expectedTime(100)
            .build();
        SaveRequiredFunctionDto updateMember = SaveRequiredFunctionDto.builder()
            .title("회원 정보 수정")
            .description("회원의 정보를 수정한다.")
            .functionType(UPDATE)
            .expectedTime(150)
            .build();
        List<SaveRequiredFunctionDto> functionDtos = List.of(addMember, loginMember, updateMember);

        LocalDate toyStartDate = LocalDate.of(2023, 7, 1);
        int toyPTD = 60;
        int toyPTK = 120;
        List<Integer> totalExpectedTime = functionDtos.stream()
            .map(SaveRequiredFunctionDto::getExpectedTime)
            .toList();
        int toyExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(toyPTD, toyPTK, toyStartDate)
            .calculatePeriodBy(totalExpectedTime);

        SaveStudyRequestDto toyDto = SaveStudyRequestDto.builder()
            .studyType(TOY)
            .title("스터디 스케쥴러")
            .description("개인의 학습의 진도율을 관리")
            .totalExpectedPeriod(toyExpectedPeriod)
            .planTimeInWeekday(toyPTD)
            .planTimeInWeekend(toyPTK)
            .startDate(toyStartDate)
            .memberId(savedMember.getId())
            .functions(functionDtos)
            .build();

        StudyService studyService = studyFactory.findServiceBy(toyDto.getStudyType());
        studyService.saveStudy(toyDto);

        // when
        ToyProject toyProject = (ToyProject) studyRepository.findAll().get(0);

        // then
        assertThat(toyProject).extracting("title", "description", "totalExpectedPeriod", "totalExpectedMin", "expectedEndDate")
            .contains("스터디 스케쥴러", "개인의 학습의 진도율을 관리", 4, 320, LocalDate.of(2023, 7,4));
    }

    @DisplayName("주어진 아이디로 종료되지 않은 학습 상세내용을 조회한다.")
    @Test
    void findStudyById() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        LocalDate lectureStartDate = LocalDate.of(2023, 7, 8);
        int lecturePTD = 50;
        int lecturePTK = 90;
        int totalRuntime = 600;
        int lectureExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(lecturePTD, lecturePTK, lectureStartDate)
                .calculatePeriodBy(totalRuntime);
        Lecture lecture = createLecture(lecturePTD, lecturePTK, lectureStartDate, lectureExpectedPeriod, totalRuntime, member);

        studyRepository.saveAndFlush(lecture);
        Lecture savedLecture = (Lecture) studyRepository.findAll().get(0);

        // when
        StudyService studyService = studyFactory.findServiceBy(toEnum(savedLecture.getStudyType()));
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
        Member member = createMember();
        memberRepository.save(member);

        LocalDate lectureStartDate = LocalDate.of(2023, 7, 8);
        LocalDate realEndDate = LocalDate.of(2023, 8, 10);
        int lecturePTD = 50;
        int lecturePTK = 90;
        int totalRuntime = 600;
        int lectureExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(lecturePTD, lecturePTK, lectureStartDate)
                .calculatePeriodBy(totalRuntime);
        Lecture lecture = createTerminatedLecture(lecturePTD, lecturePTK, lectureStartDate, lectureExpectedPeriod, totalRuntime, realEndDate, member);

        studyRepository.saveAndFlush(lecture);
        Lecture savedLecture = (Lecture) studyRepository.findAll().get(0);

        // when
        StudyService studyService = studyFactory.findServiceBy(toEnum(savedLecture.getStudyType()));
        FindStudyResponseDto study = studyService.findStudyById(savedLecture.getId());

        // then
        assertThat(study)
                .extracting("title", "description", "isTermination", "realEndDate")
                .contains("김영한의 스프링", "스프링 핵심 강의", true, LocalDate.of(2023,8,10));
    }

    @DisplayName("강의 학습의 평일, 주말 학습 계획 시간, 시작일을 인자로 받아 예상 학습기간을 구한다.")
    @Test
    void calculateExpectedPeriodByWithLecture() {
        // given
        int planTimeInWeekDay = 60;
        int planTimeInWeekend = 120;
        int totalRunTime = 600;
        LocalDate startDate = LocalDate.of(2023, 9, 15);

        StudyPlanTimeRequestDto lecturePlanTime = StudyPlanTimeRequestDto.builder()
            .studyType(LECTURE)
            .planTimeInWeekDay(planTimeInWeekDay)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .totalRunTime(totalRunTime)
            .build();

        // when
        StudyService studyService = studyFactory.findServiceBy(lecturePlanTime.getStudyType());
        int period = studyService.calculatePeriod(lecturePlanTime);

        // then
        assertThat(period).isEqualTo(8);
    }

    @DisplayName("독서 학습의 평일, 주말 학습 계획 시간, 시작일을 인자로 받아 예상 학습기간을 구한다.")
    @Test
    void calculateExpectedPeriodByWithReading() {
        // given
        int planTimeInWeekDay = 30;
        int planTimeInWeekend = 60;
        int totalPage = 700;
        int readPagePerMin = 2;
        LocalDate startDate = LocalDate.of(2023, 9, 11);

        StudyPlanTimeRequestDto readingPlanTime = StudyPlanTimeRequestDto.builder()
            .studyType(READING)
            .planTimeInWeekDay(planTimeInWeekDay)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .totalPage(totalPage)
            .readPagePerMin(readPagePerMin)
            .build();

        // when
        StudyService studyService = studyFactory.findServiceBy(readingPlanTime.getStudyType());
        int period = studyService.calculatePeriod(readingPlanTime);

        // then
        assertThat(period).isEqualTo(10);
    }

    @DisplayName("토이프로젝트 학습의 평일, 주말 학습 계획 시간, 시작일을 인자로 받아 예상 학습기간을 구한다.")
    @Test
    void calculateExpectedPeriodByWithToy() {
        // given
        int planTimeInWeekDay = 90;
        int planTimeInWeekend = 180;
        List<Integer> expectedTimes = List.of(300, 600, 250, 100, 500);
        LocalDate startDate = LocalDate.of(2023, 9, 11);

        StudyPlanTimeRequestDto toyPlanTime = StudyPlanTimeRequestDto.builder()
            .studyType(TOY)
            .planTimeInWeekDay(planTimeInWeekDay)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .expectedTimes(expectedTimes)
            .build();

        // when
        StudyService studyService = studyFactory.findServiceBy(toyPlanTime.getStudyType());
        int period = studyService.calculatePeriod(toyPlanTime);

        // then
        assertThat(period).isEqualTo(16);
    }

    private Member createMember() {
        return Member.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .name("hong")
            .accountType(AccountType.ACTIVE)
            .build();
    }

    private ToyProject createToyProject(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, int totalExpectedPeriod, Member member) {
        return ToyProject.builder()
            .title("스터디 스케쥴러")
            .description("개인의 학습의 진도율을 관리")
            .totalExpectedPeriod(totalExpectedPeriod)
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .member(member)
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

    private static Lecture createTerminatedLecture(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, int totalExpectedPeriod, int totalRuntime, LocalDate realEndDate, Member member) {
        return Lecture.builder()
                .title("김영한의 스프링")
                .description("스프링 핵심 강의")
                .teacherName("김영한")
                .totalExpectedPeriod(totalExpectedPeriod)
                .planTimeInWeekday(planTimeInWeekday)
                .planTimeInWeekend(planTimeInWeekend)
                .startDate(startDate)
                .member(member)
                .isTermination(true)
                .realEndDate(realEndDate)
                .totalRuntime(totalRuntime)
                .build();
    }
}