package toyproject.studyscheduler.domain.toyproject.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.requiredfunction.FunctionType;
import toyproject.studyscheduler.domain.study.requiredfunction.RequiredFunction;
import toyproject.studyscheduler.domain.toyproject.Category;
import toyproject.studyscheduler.domain.toyproject.TechStack;
import toyproject.studyscheduler.domain.toyproject.ToyProject;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class ToyProjectRepositoryTest {

    @Autowired
    ToyProjectRepository toyProjectRepository;

    @DisplayName("id값을 전달받아 저장된 ToyProject 하나를 조회한다.")
    @Test
    void findToyProjectById() {
        // given
        TechStack language = createTechStack("Java", Category.LANGUAGE);
        TechStack framework = createTechStack("SpringBoot", Category.FRAMEWORK);
        Member member = createMember();

        RequiredFunction function1 = createFunction("회원가입", "신규 회원 가입",
            LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 12),
            member);
        RequiredFunction function2 = createFunction("로그인", "가입된 회원 로그인",
            LocalDate.of(2023, 8, 13), LocalDate.of(2023, 8, 15),
            member);
        // when

        // then

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

    private RequiredFunction createFunction(String title, String description, LocalDate startDate, LocalDate endDate, Member member) {
        return RequiredFunction.builder()
            .title(title)
            .description(description)
            .functionType(FunctionType.READ)
            .totalExpectedTime(300)
            .planTimeInWeekDay(60)
            .planTimeInWeekend(120)
            .startDate(startDate)
            .endDate(endDate)
            .member(member)
            .build();
    }


    private TechStack createTechStack(String title, Category category) {
        return TechStack.builder()
            .title(title)
            .category(category)
            .build();
    }
}