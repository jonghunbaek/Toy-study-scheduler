package toyproject.studyscheduler.domain.study.toyproject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.techstack.TechCategory;
import toyproject.studyscheduler.domain.techstack.TechStack;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static toyproject.studyscheduler.domain.function.FunctionType.*;
import static toyproject.studyscheduler.domain.techstack.TechCategory.*;

class ToyProjectTest {

    @DisplayName("기술 스택을 추가한다.")
    @Test
    void addStack() {
        // given
        ToyProject toyProject = createToyProject();

        // when
        TechStack language = createTechStack("Java", LANGUAGE, toyProject);
        TechStack framework = createTechStack("Spring", FRAMEWORK, toyProject);

        // then
        assertThat(toyProject.getTechStacks()).hasSize(2)
            .extracting("title", "techCategory")
            .containsExactlyInAnyOrder(
                tuple("Java", LANGUAGE),
                tuple("Spring", FRAMEWORK)
            );
    }

    @DisplayName("기능 요구사항을 추가한다.")
    @Test
    void addRequiredFunction() {
        // given
        ToyProject toyProject = createToyProject();

        // when
        RequiredFunction addMember = createRequiredFunction("회원가입", "신규 회원을 등록한다.", CREATE, toyProject);
        RequiredFunction login = createRequiredFunction("로그인", "회원 등록 여부를 확인해 로그인을 결정", READ, toyProject);

        // then
        assertThat(toyProject.getRequiredFunctions()).hasSize(2)
            .extracting("title", "description", "functionType")
            .containsExactlyInAnyOrder(
                tuple("회원가입", "신규 회원을 등록한다.", CREATE),
                tuple("로그인", "회원 등록 여부를 확인해 로그인을 결정", READ)
            );
    }

    private ToyProject createToyProject() {
        return ToyProject.builder()
            .title("스터디 스케쥴러")
            .description("개인의 학습의 진도율을 관리")
            .totalExpectedPeriod(9)
            .planTimeInWeekday(60)
            .planTimeInWeekend(120)
            .startDate(LocalDate.of(2023, 8,23))
            .member(null)
            .build();
    }

    RequiredFunction createRequiredFunction(String title, String description, FunctionType functionType, ToyProject toyProject) {
        return RequiredFunction.builder()
            .title(title)
            .description(description)
            .functionType(functionType)
            .toyProject(toyProject)
            .build();
    }

    TechStack createTechStack(String title, TechCategory techCategory, ToyProject toyProject) {
        return TechStack.builder()
            .title(title)
            .techCategory(techCategory)
            .toyProject(toyProject)
            .build();
    }

}