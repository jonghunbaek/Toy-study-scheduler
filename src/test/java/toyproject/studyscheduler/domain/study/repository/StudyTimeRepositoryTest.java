package toyproject.studyscheduler.domain.study.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.StudyTime;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.requiredfunction.FunctionType;
import toyproject.studyscheduler.domain.study.requiredfunction.RequiredFunction;
import toyproject.studyscheduler.domain.toyproject.ToyProject;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@ActiveProfiles("test")
@DataJpaTest
class StudyTimeRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StudyTimeRepository studyTimeRepository;

    @Autowired
    StudyRepository studyRepository;

    @DisplayName("특정기간에 수행한 일간 학습량을 모두 조회 한다.")
    @Test
    void getStudyTimePerDay() {
        // given
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 23);

        Member member = createMember();
        memberRepository.save(member);

        Study lecture = createLecture(startDate, endDate, member);
        studyRepository.save(lecture);

        LocalDate firstDay = LocalDate.of(2023, 8, 3);
        int completeTimeFirst = 30;
        int totalCompleteTime = 30;
        StudyTime studyTimeFirst = createStudyTime(lecture, firstDay, totalCompleteTime, completeTimeFirst);

        LocalDate secondDay = LocalDate.of(2023, 8, 8);
        int completeTimeSecond = 40;
        totalCompleteTime += completeTimeSecond;
        StudyTime studyTimeSecond = createStudyTime(lecture, secondDay, totalCompleteTime, completeTimeSecond);

        LocalDate thirdDay = LocalDate.of(2023, 8, 14);
        int completeTimeThird = 25;
        totalCompleteTime += completeTimeThird;
        StudyTime studyTimeThird = createStudyTime(lecture, thirdDay, totalCompleteTime, completeTimeThird);

        LocalDate fourthDay = LocalDate.of(2023, 8, 20);
        int completeTimeFourth = 23;
        totalCompleteTime += completeTimeFourth;
        StudyTime studyTimeFourth = createStudyTime(lecture, fourthDay, totalCompleteTime, completeTimeFourth);

        studyTimeRepository.saveAll(List.of(studyTimeFirst, studyTimeSecond, studyTimeThird, studyTimeFourth));

        // when
        List<StudyTime> allByPeriod = studyTimeRepository.findAllByPeriod(startDate, endDate);

        // then
        assertThat(allByPeriod).hasSize(4)
                .extracting("totalCompleteTime", "completeTimeToday")
                .containsExactlyInAnyOrder(
                        tuple(30, 30),
                        tuple(70, 40),
                        tuple(95, 25),
                        tuple(118, 23)
                );
    }

    private StudyTime createStudyTime(Study study, LocalDate today, int totalCompleteTime, int completeTimeToday) {
        return StudyTime.builder()
                .totalCompleteTime(totalCompleteTime)
                .today(today)
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

    private ToyProject createToyProject() {
        return ToyProject.builder()
                .title("스터디 스케쥴러")
                .description("개인의 학습의 진도율을 관리")
                .build();
    }

    private RequiredFunction createFunction(LocalDate startDate, LocalDate endDate, Member member, ToyProject toyProject) {
        return RequiredFunction.builder()
                .title("강의 조회")
                .description("강의를 조회한다.")
                .functionType(FunctionType.READ)
                .totalExpectedTime(300)
                .planTimeInWeekDay(60)
                .planTimeInWeekend(120)
                .startDate(startDate)
                .endDate(endDate)
                .member(member)
                .toyProject(toyProject)
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