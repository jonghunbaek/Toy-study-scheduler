package toyproject.studyscheduler.domain.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.toyproject.requiredfunction.FunctionType;
import toyproject.studyscheduler.domain.study.toyproject.requiredfunction.RequiredFunction;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.study.toyproject.repository.ToyProjectRepository;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class StudyRepositoryTest {

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ToyProjectRepository toyProjectRepository;

    @DisplayName("주어진 여러개의 아이디로 여러개의 학습 상세내용을 조회한다.")
    @Test
    void getStudiesByIds() {
        // given
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject = createToyProject();
        toyProjectRepository.save(toyProject);

        Lecture lecture = createLecture(startDate, endDate, member);
        Reading reading = createReading(startDate, endDate, member);
        RequiredFunction function = createFunction(startDate, endDate, member, toyProject);
        studyRepository.saveAll(List.of(lecture, reading, function));

        // when
        List<Study> studies = studyRepository.findAllById(List.of(lecture.getId(), reading.getId(), function.getId()));

        // then
        assertThat(studies).hasSize(3)
                .extracting("title", "description", "startDate")
                .containsExactlyInAnyOrder(
                        tuple("김영한의 스프링", "스프링 핵심 강의", startDate),
                        tuple("클린 코드", "클린 코드를 배우기 위한 도서", startDate),
                        tuple("강의 조회", "강의를 조회한다.", startDate)
                );
    }

    @DisplayName("특정기간에 수행한 학습들을 모두 조회 한다.")
    @Test
    void getStudiesByPeriod() {
        // given
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject = createToyProject();
        toyProjectRepository.save(toyProject);

        Study lecture = createLecture(startDate, endDate, member);
        Study reading = createReading(startDate, endDate, member);
        Study requiredFunction = createFunction(startDate, endDate, member, toyProject);
        studyRepository.saveAll(List.of(lecture, reading, requiredFunction));

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
                        tuple("강의 조회", "강의를 조회한다.", startDate)
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