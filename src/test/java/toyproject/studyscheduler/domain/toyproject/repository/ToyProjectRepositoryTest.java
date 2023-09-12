package toyproject.studyscheduler.domain.toyproject.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.study.toyproject.requiredfunction.FunctionType;
import toyproject.studyscheduler.domain.study.toyproject.requiredfunction.RequiredFunction;
import toyproject.studyscheduler.domain.study.toyproject.TechStack.TechCategory;
import toyproject.studyscheduler.domain.study.toyproject.TechStack.TechStack;
import toyproject.studyscheduler.domain.study.toyproject.repository.ToyProjectRepository;

@ActiveProfiles("test")
@DataJpaTest
class ToyProjectRepositoryTest {

    @Autowired
    ToyProjectRepository toyProjectRepository;

    @DisplayName("id값을 전달받아 저장된 ToyProject 하나를 조회한다.")
    @Test
    void findToyProjectById() {
        // given
        Member member = createMember();

        TechStack language = createTechStack("Java", TechCategory.LANGUAGE);
        TechStack framework = createTechStack("SpringBoot", TechCategory.FRAMEWORK);



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

    private RequiredFunction createFunction(FunctionType functionType, ToyProject toyProject) {
        return RequiredFunction.builder()
            .functionType(functionType)
            .toyProject(toyProject)
            .build();
    }


    private TechStack createTechStack(String title, TechCategory techCategory) {
        return TechStack.builder()
            .title(title)
            .category(techCategory)
            .build();
    }
}