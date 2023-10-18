package toyproject.studyscheduler.domain.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.StudyTime;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static toyproject.studyscheduler.domain.function.FunctionType.*;


@ActiveProfiles("test")
@DataJpaTest
class StudyTimeRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StudyTimeRepository studyTimeRepository;
    @Autowired
    StudyRepository studyRepository;

    @DisplayName("특정기간에 수행한 강의 학습의 일간 학습량, 누적 학습량, 누적 학습율을 모두 조회 한다.")
    @Test
    void getStudyTimeWithLecturePerDay() {
        // given
        LocalDate startDate = LocalDate.of(2023, 8, 1);

        Member member = createMember();
        memberRepository.save(member);

        Study lecture = createLecture(startDate, member);
        studyRepository.save(lecture);

        List<LocalDate> dates = List.of(LocalDate.of(2023, 8, 3),
            LocalDate.of(2023, 8, 8),
            LocalDate.of(2023, 8, 14),
            LocalDate.of(2023, 8, 20),
            LocalDate.of(2023, 9, 1)
        );
        List<Integer> totalCompleteTimes = List.of(0, 30, 70, 95, 118, 148);
        List<Integer> completeTimeTodays = List.of(30, 40, 25, 23, 30);

        studyTimeRepository.saveAll(createStudyTimes(dates.size(), lecture, dates, totalCompleteTimes, completeTimeTodays));

        // when
        LocalDate start = LocalDate.of(2023, 8, 1);
        LocalDate end = LocalDate.of(2023, 8, 30);
        List<StudyTime> allByPeriod = studyTimeRepository.findAllByPeriod(start, end);

        // then
        assertThat(allByPeriod).hasSize(4)
                .extracting("totalCompleteTime", "totalLearningRate", "completeTimeToday", "date")
                .containsExactlyInAnyOrder(
                        tuple(30, 5.0, 30, LocalDate.of(2023,8,3)),
                        tuple(70, 11.67, 40, LocalDate.of(2023,8,8)),
                        tuple(95, 15.83, 25, LocalDate.of(2023,8,14)),
                        tuple(118, 19.67, 23, LocalDate.of(2023,8,20))
                );
    }

    @DisplayName("특정기간에 수행한 독서 학습의 일간 학습량, 누적 학습량, 누적 학습율을 모두 조회 한다.")
    @Test
    void getStudyTimeWithReadingPerDay() {
        // given
        LocalDate startDate = LocalDate.of(2023, 8, 1);

        Member member = createMember();
        memberRepository.save(member);

        Reading reading = createReading(startDate, member);
        studyRepository.save(reading);

        List<LocalDate> dates = List.of(LocalDate.of(2023, 8, 3),
            LocalDate.of(2023, 8, 8),
            LocalDate.of(2023, 8, 14),
            LocalDate.of(2023, 8, 20),
            LocalDate.of(2023, 9, 1)
        );
        List<Integer> totalCompleteTimes = List.of(0, 30, 70, 95, 118, 148);
        List<Integer> completeTimeTodays = List.of(30, 40, 25, 23, 30);

        studyTimeRepository.saveAll(createStudyTimes(dates.size(), reading, dates, totalCompleteTimes, completeTimeTodays));

        // when
        LocalDate start = LocalDate.of(2023, 8, 1);
        LocalDate end = LocalDate.of(2023, 8, 30);
        List<StudyTime> allByPeriod = studyTimeRepository.findAllByPeriod(start, end);

        // then
        assertThat(allByPeriod).hasSize(4)
            .extracting("totalCompleteTime", "totalLearningRate", "completeTimeToday", "date")
            .containsExactlyInAnyOrder(
                tuple(30, 12.0, 30, LocalDate.of(2023,8,3)),
                tuple(70, 28.0, 40, LocalDate.of(2023,8,8)),
                tuple(95, 38.0, 25, LocalDate.of(2023,8,14)),
                tuple(118, 47.2, 23, LocalDate.of(2023,8,20))
            );
    }

    @DisplayName("특정기간에 수행한 토이프로젝트 학습의 일간 학습량, 누적 학습량, 누적 학습율을 모두 조회 한다.")
    @Test
    void getStudyTimeWithToyProjectPerDay() {
        // given
        LocalDate startDate = LocalDate.of(2023, 8, 1);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject = createToyProject(startDate, member);
        createFunction(READ, toyProject, 100);
        createFunction(CREATE, toyProject, 70);
        createFunction(UPDATE, toyProject, 150);
        studyRepository.save(toyProject);
        
        List<LocalDate> dates = List.of(LocalDate.of(2023, 8, 3),
            LocalDate.of(2023, 8, 8),
            LocalDate.of(2023, 8, 14),
            LocalDate.of(2023, 8, 20),
            LocalDate.of(2023, 9, 1)
        );
        List<Integer> totalCompleteTimes = List.of(0, 30, 70, 95, 118, 148);
        List<Integer> completeTimeTodays = List.of(30, 40, 25, 23, 30);

        studyTimeRepository.saveAll(createStudyTimes(dates.size(), toyProject, dates, totalCompleteTimes, completeTimeTodays));

        // when2
        LocalDate start = LocalDate.of(2023, 8, 1);
        LocalDate end = LocalDate.of(2023, 8, 30);
        List<StudyTime> allByPeriod = studyTimeRepository.findAllByPeriod(start, end);

        // then2
        assertThat(allByPeriod).hasSize(4)
            .extracting("totalCompleteTime", "totalLearningRate", "completeTimeToday", "date")
            .containsExactlyInAnyOrder(
                tuple(30, 9.38, 30, LocalDate.of(2023,8,3)),
                tuple(70, 21.88, 40, LocalDate.of(2023,8,8)),
                tuple(95, 29.69, 25, LocalDate.of(2023,8,14)),
                tuple(118, 36.88, 23, LocalDate.of(2023,8,20))
            );
    }

    private List<StudyTime> createStudyTimes(int size, Study study, List<LocalDate> dates, List<Integer> totalCompleteTimes, List<Integer> completeTimeTodays) {
        List<StudyTime> studyTimes = new ArrayList<>(size);
        for (int i=0; i<size; i++) {
            studyTimes.add(createStudyTime(study, dates.get(i), totalCompleteTimes.get(i), completeTimeTodays.get(i)));
        }

        return studyTimes;
    }

    private StudyTime createStudyTime(Study study, LocalDate today, int totalCompleteTime, int completeTimeToday) {
        return StudyTime.builder()
                .totalCompleteTime(totalCompleteTime)
                .date(today)
                .completeTimeToday(completeTimeToday)
                .study(study)
                .build();
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

    private ToyProject createToyProject(LocalDate startDate, Member member) {
        return ToyProject.builder()
            .title("스터디 스케쥴러")
            .description("개인의 학습의 진도율을 관리")
            .planTimeInWeekday(60)
            .planTimeInWeekend(120)
            .startDate(startDate)
            .member(member)
            .build();
    }

    private void createFunction(FunctionType functionType, ToyProject toyProject, int expectedTime) {
        RequiredFunction.builder()
            .functionType(functionType)
            .toyProject(toyProject)
            .expectedTime(expectedTime)
            .description("테스트용")
            .build();
    }

    private Reading createReading(LocalDate startDate, Member member) {
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
            .member(member)
            .build();
    }

    private static Lecture createLecture(LocalDate startDate, Member member) {
        return Lecture.builder()
                .title("김영한의 스프링")
                .description("스프링 핵심 강의")
                .teacherName("김영한")
                .planTimeInWeekday(30)
                .planTimeInWeekend(100)
                .startDate(startDate)
                .member(member)
                .totalRuntime(600)
                .build();
    }
}