package toyproject.studyscheduler.domain.study.reading;

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
class ReadingRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ReadingRepository readingRepository;

    @DisplayName("01_주어진 아이디로 독서 학습의 상세내용을 조회한다.")
    @Test
    void findReadingById() {
        // given
        String title = "클린 코드";
        String description = "클린한 코드를 통해 유지보수성을 높이자";
        String authorName = "로버트 c.마틴";
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);

        Member member = createMember();
        memberRepository.save(member);

        Reading reading = createReading(title, description, authorName, startDate, endDate, member);

        // when
        Reading savedReading = readingRepository.save(reading);

        // then
        assertThat(savedReading)
            .extracting("title", "description", "authorName", "startDate")
            .contains("클린 코드", "클린한 코드를 통해 유지보수성을 높이자", "로버트 c.마틴", startDate);
    }

    @DisplayName("02_특정기간에 수행한 독서 학습들을 모두 조회 한다.")
    @Test
    void findReadingByPeriod() {
        // given
        LocalDate startDate1 = LocalDate.of(2023, 7, 1);
        LocalDate endDate1 = LocalDate.of(2023, 7, 21);
        LocalDate startDate2 = LocalDate.of(2023, 7, 11);
        LocalDate endDate2 = LocalDate.of(2023, 7, 31);

        Member member = createMember();
        memberRepository.save(member);

        Reading reading1 = createReading("클린 코드", "클린한 코드를 통해 유지보수성을 높이자", "로버트 c.마틴", startDate1, endDate1, member);
        Reading reading2 = createReading("프로그래머의 뇌", "뇌가 데이터를 기억하는 방식에 대해 학습해 프로그래밍에 도움을 주자", "펠리너 헤르만스", startDate2, endDate2, member);
        readingRepository.saveAll(List.of(reading1, reading2));

        // when
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);

        List<Reading> readings = readingRepository.findAllByPeriod(startDate, endDate);

        // then
        assertThat(readings).hasSize(2)
            .extracting("title", "description", "authorName", "startDate")
            .containsExactlyInAnyOrder(
                tuple("클린 코드", "클린한 코드를 통해 유지보수성을 높이자", "로버트 c.마틴", startDate1),
                tuple("프로그래머의 뇌", "뇌가 데이터를 기억하는 방식에 대해 학습해 프로그래밍에 도움을 주자", "펠리너 헤르만스", startDate2)
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

    private static Reading createReading(String title, String description, String authorName, LocalDate startDate, LocalDate endDate, Member member) {
        return Reading.builder()
            .title(title)
            .authorName(authorName)
            .description(description)
            .totalPage(500)
            .planTimeInWeekday(30)
            .planTimeInWeekend(30)
            .readPagePerMin(2)
            .totalExpectedPeriod(250)
            .startDate(startDate)
            .expectedEndDate(endDate)
            .member(member)
            .build();
    }
}