package toyproject.studyscheduler.domain.techstack.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.techstack.TechCategory;
import toyproject.studyscheduler.domain.techstack.TechStack;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static toyproject.studyscheduler.domain.techstack.TechCategory.*;

@ActiveProfiles("test")
@DataJpaTest
class TechStackRepositoryTest {

    @Autowired
    TechStackRepository techStackRepository;

    @DisplayName("토이프로젝트에 저장된 TechStack을 모두 가져온다.")
    @Test
    void findAllByToyProject() {
        // given
        LocalDate startDate = LocalDate.of(2023, 9, 10);
        LocalDate endDate = LocalDate.of(2023, 9, 29);


        Member member = createMember();
        TechStack language = createTechStack("Java", LANGUAGE);
        TechStack framework = createTechStack("Spring", FRAMEWORK);
        TechStack ide = createTechStack("IntelliJ", IDE);
        TechStack db = createTechStack("MySQL", DATABASE);
        TechStack os = createTechStack("window11", OS);
        TechStack build = createTechStack("gradle", BUILD);
        TechStack cloud = createTechStack("aws", CLOUD);

        ToyProject toyProject = createToyProject(startDate, endDate, member, List.of(language, framework, ide, db, os, build, cloud));

        techStackRepository.saveAll(List.of(language, framework, ide, db, os, build, cloud));


        // when

        // then

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

    private ToyProject createToyProject(LocalDate startDate, LocalDate endDate, Member member, List<TechStack> stacks) {
        return ToyProject.builder()
            .title("스터디 스케쥴러")
            .description("개인의 학습의 진도율을 관리")
            .totalExpectedTime(300)
            .planTimeInWeekDay(60)
            .planTimeInWeekend(120)
            .startDate(startDate)
            .endDate(endDate)
            .member(member)
            .stacks(stacks)
            .build();
    }

     TechStack createTechStack(String title, TechCategory techCategory) {
        return TechStack.builder()
            .title(title)
            .techCategory(techCategory)
            .build();
     }
}