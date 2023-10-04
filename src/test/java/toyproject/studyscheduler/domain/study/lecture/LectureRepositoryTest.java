package toyproject.studyscheduler.domain.study.lecture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class LectureRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LectureRepository lectureRepository;

    @DisplayName("01_주어진 아이디로 강의 학습의 상세내용을 조회한다.")
    @Test
    void findLectureById() {
        // given
        String title = "김영한의 스프링";
        String description = "스프링 핵심 강의";
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);
        String teacherName = "김영한";

        Member member = createMember();
        memberRepository.save(member);

        Lecture lecture = createLecture(title, description, startDate, endDate, member, teacherName);
        // when
        Lecture savedLecture = lectureRepository.save(lecture);

        // then
        assertThat(savedLecture)
            .extracting("title", "description", "teacherName", "startDate")
            .contains("김영한의 스프링", "스프링 핵심 강의", "김영한", startDate);
    }

    @DisplayName("02_특정기간에 수행한 강의 학습들을 모두 조회 한다.")
    @Test
    void findLecturesByPeriod() {
        // given
        LocalDate startDate1 = LocalDate.of(2023, 7, 1);
        LocalDate endDate1 = LocalDate.of(2023, 7, 21);
        LocalDate startDate2 = LocalDate.of(2023, 7, 11);
        LocalDate endDate2 = LocalDate.of(2023, 7, 31);

        Member member = createMember();
        memberRepository.save(member);

        Lecture lecture1 = createLecture("김영한의 스프링", "스프링 핵심 강의", startDate1, endDate1, member, "김영한");
        Lecture lecture2 = createLecture("백기선의 JPA", "JPA 핵심 강의", startDate2, endDate2, member, "백기선");
        lectureRepository.saveAll(List.of(lecture1, lecture2));

        // when
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);

        List<Lecture> lectures = lectureRepository.findAllByPeriod(startDate, endDate);

        // then
        assertThat(lectures).hasSize(2)
            .extracting("title", "description", "startDate", "teacherName")
            .containsExactlyInAnyOrder(
                tuple("김영한의 스프링", "스프링 핵심 강의", startDate1, "김영한"),
                tuple("백기선의 JPA", "JPA 핵심 강의", startDate2, "백기선")
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

    private static Lecture createLecture(String title, String description, LocalDate startDate, LocalDate endDate, Member member, String teacherName) {
        return Lecture.builder()
            .title(title)
            .description(description)
            .teacherName(teacherName)
            .totalExpectedPeriod(600)
            .planTimeInWeekday(30)
            .planTimeInWeekend(100)
            .startDate(startDate)
            .expectedEndDate(endDate)
            .member(member)
            .build();
    }

}