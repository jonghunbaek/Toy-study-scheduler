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
import java.util.ArrayList;
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

        List<LocalDate> dates = List.of(LocalDate.of(2023, 8, 3),
            LocalDate.of(2023, 8, 8),
            LocalDate.of(2023, 8, 14),
            LocalDate.of(2023, 8, 20)
        );
        List<Integer> totalCompleteTimes = List.of(30, 70, 95, 118);
        List<Integer> completeTimeTodays = List.of(30, 40, 25, 23);

        studyTimeRepository.saveAll(createStudies(dates.size(), lecture, dates, totalCompleteTimes, completeTimeTodays));

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

    @DisplayName("특정기간에 수행한 누적 학습량을 모두 조회 한다.")
    @Test
    void getTotalCompleteTimePerDay() {
        // given
        LocalDate startDate = LocalDate.of(2023, 8, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 23);

        Member member = createMember();
        memberRepository.save(member);

        Study lecture = createLecture(startDate, endDate, member);
        studyRepository.save(lecture);

        List<LocalDate> dates = List.of(LocalDate.of(2023, 8, 3),
            LocalDate.of(2023, 8, 8),
            LocalDate.of(2023, 8, 14),
            LocalDate.of(2023, 8, 20)
        );
        List<Integer> totalCompleteTimes = List.of(30, 70, 95, 118);
        List<Integer> completeTimeTodays = List.of(30, 40, 25, 23);

        studyTimeRepository.saveAll(createStudies(dates.size(), lecture, dates, totalCompleteTimes, completeTimeTodays));


        // when
        StudyTime studyTime = studyTimeRepository.findFirstByStudyOrderByDateDesc(lecture);
        int completeTimeToday = 40;
        studyTimeRepository.save(createStudyTime(
            lecture,
            LocalDate.of(2023, 8, 23),
            studyTime.calculateTotalCompleteTime(completeTimeToday),
            completeTimeToday
        ));

        List<StudyTime> allByPeriod = studyTimeRepository.findAllByPeriod(startDate, endDate);

        // then
        assertThat(allByPeriod).hasSize(5)
            .extracting("totalCompleteTime", "completeTimeToday")
            .containsExactlyInAnyOrder(
                tuple(30, 30),
                tuple(70, 40),
                tuple(95, 25),
                tuple(118, 23),
                tuple(158, 40)
            );
    }

    private List<StudyTime> createStudies(int size, Study study, List<LocalDate> dates, List<Integer> totalCompleteTimes, List<Integer> completeTimeTodays) {
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