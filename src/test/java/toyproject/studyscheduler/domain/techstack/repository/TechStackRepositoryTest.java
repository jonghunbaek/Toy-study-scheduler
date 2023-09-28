package toyproject.studyscheduler.domain.techstack.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.study.toyproject.ToyProjectRepository;
import toyproject.studyscheduler.domain.techstack.TechCategory;
import toyproject.studyscheduler.domain.techstack.TechStack;
import toyproject.studyscheduler.domain.techstack.TechStackRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static toyproject.studyscheduler.domain.techstack.TechCategory.*;

@ActiveProfiles("test")
@DataJpaTest
class TechStackRepositoryTest {

    @Autowired
    TechStackRepository techStackRepository;
    @Autowired
    ToyProjectRepository toyProjectRepository;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("01_저장된 TechStack을 모두 조회한다.")
    @Test
    void findAll() {
        // given
        LocalDate startDate = LocalDate.of(2023, 9, 10);
        LocalDate endDate = LocalDate.of(2023, 9, 29);

        Member member = createMember();
        ToyProject toyProject = createToyProject(startDate, endDate, member);

        TechStack language = createTechStack("Java", "주 사용 언어",LANGUAGE, toyProject);
        TechStack framework = createTechStack("Spring", "프레임 워크", FRAMEWORK, toyProject);
        TechStack ide = createTechStack("IntelliJ", "IDE는 인텔리제이", IDE, toyProject);
        TechStack db = createTechStack("MySQL", "DB는 일단 MySQL",  DATABASE, toyProject);
        TechStack os = createTechStack("window11", "OS는 윈도우 11",  OS, toyProject);
        TechStack build = createTechStack("gradle", "build도구는 gradle",  BUILD, toyProject);
        TechStack cloud = createTechStack("aws", "클라우드로 aws사용",  CLOUD, toyProject);

        memberRepository.save(member);
        toyProjectRepository.save(toyProject);
        techStackRepository.saveAll(List.of(language,framework,ide,db,os,build,cloud));

        // when
        List<TechStack> techStacks = techStackRepository.findAll();

        // then
        assertThat(techStacks).hasSize(7)
            .extracting("title", "techCategory", "description")
            .containsExactlyInAnyOrder(
                tuple("Java", LANGUAGE, "주 사용 언어"),
                tuple("Spring", FRAMEWORK, "프레임 워크"),
                tuple("IntelliJ", IDE, "IDE는 인텔리제이"),
                tuple("MySQL", DATABASE, "DB는 일단 MySQL"),
                tuple("window11", OS, "OS는 윈도우 11"),
                tuple("gradle", BUILD, "build도구는 gradle"),
                tuple("aws", CLOUD, "클라우드로 aws사용")
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

    private ToyProject createToyProject(LocalDate startDate, LocalDate endDate, Member member) {
        return ToyProject.builder()
            .title("스터디 스케쥴러")
            .description("개인의 학습의 진도율을 관리")
            .totalExpectedTime(300)
            .planTimeInWeekDay(60)
            .planTimeInWeekend(120)
            .startDate(startDate)
            .endDate(endDate)
            .member(member)
            .build();
    }

     TechStack createTechStack(String title, String description, TechCategory techCategory, ToyProject toyProject) {
        return TechStack.builder()
            .title(title)
            .description(description)
            .techCategory(techCategory)
            .toyProject(toyProject)
            .build();
     }
}