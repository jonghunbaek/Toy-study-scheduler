package toyproject.studyscheduler.domain.study.repository;

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

    @DisplayName("특정기간에 수행한 강의 학습의 일간 학습량, 누적 학습량, 누적 학습율을 모두 조회 한다.")
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
            LocalDate.of(2023, 8, 20),
            LocalDate.of(2023, 9, 1)
        );
        List<Integer> totalCompleteTimes = List.of(0, 30, 70, 95, 118, 148);
        List<Integer> completeTimeTodays = List.of(30, 40, 25, 23, 30);

        studyTimeRepository.saveAll(createStudyTimes(dates.size(), lecture, dates, totalCompleteTimes, completeTimeTodays));

        // when
        List<StudyTime> allByPeriod = studyTimeRepository.findAllByPeriod(startDate, endDate);

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

    private static Lecture createLecture(LocalDate startDate, LocalDate endDate, Member member) {
        return Lecture.builder()
                .title("김영한의 스프링")
                .description("스프링 핵심 강의")
                .teacherName("김영한")
                .planTimeInWeekday(30)
                .planTimeInWeekend(100)
                .startDate(startDate)
                .expectedEndDate(endDate)
                .member(member)
                .totalRuntime(600)
                .build();
    }
}