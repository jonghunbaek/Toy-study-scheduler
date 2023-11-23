package toyproject.studyscheduler.api.service.studytime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.controller.response.FindStudyTimeResponseDto;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.studytime.StudyTime;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.studytime.repository.StudyTimeRepository;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.service.studytime.StudyTimeService;
import toyproject.studyscheduler.util.StudyUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static toyproject.studyscheduler.domain.function.FunctionType.*;

@ActiveProfiles("test")
@SpringBootTest
class StudyTimeServiceTest {

    @Autowired
    StudyTimeService studyTimeService;
    @Autowired
    StudyTimeRepository studyTimeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StudyRepository studyRepository;
    @Autowired
    StudyUtil studyUtil;

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

        List<FindStudyTimeResponseDto> dtos = studyTimeService.findAllBy(startDate, endDate);

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