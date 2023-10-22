package toyproject.studyscheduler.api.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.api.controller.request.SaveStudyRequestDto;
import toyproject.studyscheduler.api.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.api.controller.response.FindStudyTimeResponseDto;
import toyproject.studyscheduler.domain.function.RequiredFunctionRepository;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.StudyTime;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.study.repository.StudyTimeRepository;
import toyproject.studyscheduler.domain.techstack.TechCategory;
import toyproject.studyscheduler.domain.techstack.TechStack;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.techstack.TechStackRepository;
import toyproject.studyscheduler.util.StudyUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static toyproject.studyscheduler.domain.function.FunctionType.*;

@ActiveProfiles("test")
@SpringBootTest
class StudyServiceTest {

    @Autowired
    StudyService studyService;
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
        Member member = createMember();
        memberRepository.save(member);

        LocalDate lectureStartDate = LocalDate.of(2023, 7, 8);
        int lecturePTD = 50;
        int lecturePTK = 90;
        int totalRuntime = 600;
        int lectureExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(lecturePTD, lecturePTK, lectureStartDate)
                .calculatePeriodBy(totalRuntime);
        Lecture lecture = createLecture(lecturePTD, lecturePTK, lectureStartDate, lectureExpectedPeriod, totalRuntime, member);

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

        Lecture savedLecture = studyRepository.save(lecture);

        // when
        FindStudyResponseDto study = studyService.findStudyById(savedLecture.getId());

        // then
        assertThat(study)
                .extracting("title", "description", "isTermination", "realEndDate")
                .contains("김영한의 스프링", "스프링 핵심 강의", true, LocalDate.of(2023,8,10));
    }

    @DisplayName("특정기간에 수행한 학습, 학습 시간들을 모두 조회 한다.")
    @Test
    void findStudiesByPeriod() {
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
        List<RequiredFunction> functions = List.of(createFunction(CREATE, 70), createFunction(READ, 100), createFunction(UPDATE, 150));
        List<Integer> totalExpectedTime = functions.stream()
                .map(RequiredFunction::getExpectedTime)
                .toList();
        int toyExpectedPeriod = studyUtil.setUpPeriodCalCulatorBy(toyPTD, toyPTK, toyStartDate)
                .calculatePeriodBy(totalExpectedTime);
        ToyProject toyProject = createToyProject(toyPTD, toyPTK, toyStartDate, toyExpectedPeriod, member);
        functions.forEach(toyProject::addRequiredFunction);

        studyRepository.saveAll(List.of(lecture, reading, toyProject));

        List<LocalDate> dates = List.of(
            LocalDate.of(2023, 8, 3),
            LocalDate.of(2023, 8, 8),
            LocalDate.of(2023, 8, 14),
            LocalDate.of(2023, 8, 20),
            LocalDate.of(2023, 9, 1)
        );
        List<Integer> totalCompleteTimes = List.of(0, 30, 70, 95, 118, 148);
        List<Integer> completeTimeTodays = List.of(30, 40, 25, 23, 30);

        List<StudyTime> studyTimes = new ArrayList<>();
        createStudyTimes(studyTimes, lecture, dates, totalCompleteTimes, completeTimeTodays);
        createStudyTimes(studyTimes, reading, dates, totalCompleteTimes, completeTimeTodays);
        createStudyTimes(studyTimes, toyProject, dates, totalCompleteTimes, completeTimeTodays);

        studyTimeRepository.saveAll(studyTimes);

        // when
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 31);

        List<FindStudyTimeResponseDto> dtos = studyService.findAllBy(startDate, endDate);

        // then
        assertThat(dtos).hasSize(12)
            .extracting("title", "description", "isTermination", "totalCompleteTime", "totalLearningRate", "completeTimeToday", "date")
            .containsExactlyInAnyOrder(
                tuple("김영한의 스프링", "스프링 핵심 강의", false, 30, 5.0, 30, LocalDate.of(2023,8,3)),
                tuple("김영한의 스프링", "스프링 핵심 강의", false, 70, 11.67, 40, LocalDate.of(2023,8,8)),
                tuple("김영한의 스프링", "스프링 핵심 강의", false, 95, 15.83, 25, LocalDate.of(2023,8,14)),
                tuple("김영한의 스프링", "스프링 핵심 강의", false, 118, 19.67, 23, LocalDate.of(2023,8,20)),
                tuple("클린 코드", "클린 코드를 배우기 위한 도서", false, 30, 12.0, 30, LocalDate.of(2023,8,3)),
                tuple("클린 코드", "클린 코드를 배우기 위한 도서", false, 70, 28.0, 40, LocalDate.of(2023,8,8)),
                tuple("클린 코드", "클린 코드를 배우기 위한 도서", false, 95, 38.0, 25, LocalDate.of(2023,8,14)),
                tuple("클린 코드", "클린 코드를 배우기 위한 도서", false, 118, 47.2, 23, LocalDate.of(2023,8,20)),
                tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", false, 30, 9.38, 30, LocalDate.of(2023,8,3)),
                tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", false, 70, 21.88, 40, LocalDate.of(2023,8,8)),
                tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", false, 95, 29.69, 25, LocalDate.of(2023,8,14)),
                tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", false, 118, 36.88, 23, LocalDate.of(2023,8,20))
            );
    }

    private Member createMember() {
        return Member.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .name("hong")
            .accountType(AccountType.ACTIVE)
            .originProfileImage("1234")
            .storedProfileImage("4151")
            .build();
    }

    private void createStudyTimes(List<StudyTime> studyTimes, Study study, List<LocalDate> dates, List<Integer> totalCompleteTimes, List<Integer> completeTimeTodays) {
        for (int i=0; i<dates.size(); i++) {
            studyTimes.add(createStudyTime(study, dates.get(i), totalCompleteTimes.get(i), completeTimeTodays.get(i)));
        }
    }

    private StudyTime createStudyTime(Study study, LocalDate today, int totalCompleteTime, int completeTimeToday) {
        return StudyTime.builder()
            .totalCompleteTime(totalCompleteTime)
            .date(today)
            .completeTimeToday(completeTimeToday)
            .study(study)
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