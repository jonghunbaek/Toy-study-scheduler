package toyproject.studyscheduler.dailystudy.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.common.config.querydsl.QuerydslConfig;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.member.repository.MemberRepository;
import toyproject.studyscheduler.study.application.dto.Period;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.entity.Study;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.study.domain.entity.Reading;
import toyproject.studyscheduler.study.repository.StudyRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@DataJpaTest
class DailyStudyRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    DailyStudyRepository dailyStudyRepository;

    @DisplayName("특정 학습, 특정 기간을 조건으로 일일 학습을 모두 조회한다.")
    @ParameterizedTest
    @MethodSource("argumentsPeriod")
    void findAllByStudy(Period period, int size, List<LocalDate> results) {
        // given
        Member member = memberRepository.findById(1L)
            .orElseThrow();
        Reading reading = studyRepository.save(createReading(member));

        List<LocalDate> studyDates = List.of(
            LocalDate.of(2024, 4, 1),
            LocalDate.of(2024, 4, 2),
            LocalDate.of(2024, 4, 3),
            LocalDate.of(2024, 4, 4),
            LocalDate.of(2024, 4, 5)
        );
        List<Integer> completeMinutesTodays = List.of(30, 40, 25, 23, 30);
        dailyStudyRepository.saveAll(createDailyStudies(reading, studyDates, completeMinutesTodays));

        // when
        List<DailyStudy> dailyStudies = dailyStudyRepository.findDailyStudyByConditions(reading.getId(), period);

        // then
        assertThat(dailyStudies.size()).isEqualTo(size);
        IntStream.range(0, dailyStudies.size())
                        .forEach(i -> assertThat(dailyStudies.get(i).getStudyDate()).isEqualTo(results.get(i)));
    }

    private static Stream<Arguments> argumentsPeriod() {
        return Stream.of(
                Arguments.of(
                        null,
                        5,
                        List.of(
                                LocalDate.of(2024, 4, 1),
                                LocalDate.of(2024, 4, 2),
                                LocalDate.of(2024, 4, 3),
                                LocalDate.of(2024, 4, 4),
                                LocalDate.of(2024, 4, 5)
                        )
                ),
                Arguments.of(
                        new Period(LocalDate.of(2024,4,2), null),
                        4,
                        List.of(
                                LocalDate.of(2024, 4, 2),
                                LocalDate.of(2024, 4, 3),
                                LocalDate.of(2024, 4, 4),
                                LocalDate.of(2024, 4, 5)
                        )
                ),
                Arguments.of(
                        new Period(null, LocalDate.of(2024,4,4)),
                        4,
                        List.of(
                                LocalDate.of(2024, 4, 1),
                                LocalDate.of(2024, 4, 2),
                                LocalDate.of(2024, 4, 3),
                                LocalDate.of(2024, 4, 4)
                        )
                ),
                Arguments.of(
                        new Period(LocalDate.of(2024,4,2), LocalDate.of(2024,4,4)),
                        3,
                        List.of(
                                LocalDate.of(2024, 4, 2),
                                LocalDate.of(2024, 4, 3),
                                LocalDate.of(2024, 4, 4)
                        )
                )
        );
    }

    private Reading createReading(Member member) {
        StudyInformation information = createInformation("클린 코드", "클린 코드를 작성하는 방법", true);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 1));
        StudyPlan plan = StudyPlan.fromStarting(30, 60);

        return Reading.builder()
            .studyInformation(information)
            .studyPeriod(period)
            .studyPlan(plan)
            .authorName("로버트 마틴")
            .readPagePerMin(2)
            .totalPage(600)
            .member(member)
            .build();
    }

    private StudyInformation createInformation(String title, String description, boolean isTermination) {
        return StudyInformation.builder()
            .title(title)
            .description(description)
            .isTermination(isTermination)
            .build();
    }

    private List<DailyStudy> createDailyStudies(Study study, List<LocalDate> studyDates, List<Integer> completeMinutesTodays) {
        return IntStream.range(0, studyDates.size())
            .mapToObj(i -> createDailyStudy(study, studyDates.get(i), completeMinutesTodays.get(i)))
            .collect(toList());
    }

    private DailyStudy createDailyStudy(Study study, LocalDate today, int completeTimeToday) {
        return DailyStudy.builder()
                .studyDate(today)
                .completeMinutesToday(completeTimeToday)
                .study(study)
                .build();
    }
}