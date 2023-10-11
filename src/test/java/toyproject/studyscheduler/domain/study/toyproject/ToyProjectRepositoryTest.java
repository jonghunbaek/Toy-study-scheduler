package toyproject.studyscheduler.domain.study.toyproject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.techstack.TechCategory;
import toyproject.studyscheduler.domain.techstack.TechStack;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static toyproject.studyscheduler.domain.function.FunctionType.CREATE;
import static toyproject.studyscheduler.domain.function.FunctionType.READ;
import static toyproject.studyscheduler.domain.techstack.TechCategory.FRAMEWORK;
import static toyproject.studyscheduler.domain.techstack.TechCategory.LANGUAGE;

@ActiveProfiles("test")
@DataJpaTest
class ToyProjectRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ToyProjectRepository toyProjectRepository;

    @DisplayName("주어진 아이디로 토이프로젝트 학습의 상세내용을 조회한다.")
    @Test
    void findToyProjectById() {
        // given
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject = createToyProject("스터디 스케줄러", "개인의 학습 내용을 수치화해서 조회", startDate, endDate, member);

        RequiredFunction function1 = createFunction("회원가입", "신규 회원을 생성", CREATE, toyProject);
        RequiredFunction function2 = createFunction("로그인", "기존 회원 존재 여부 확인", READ, toyProject);

        TechStack stack1 = createTechStack("Java", LANGUAGE, toyProject);
        TechStack stack2 = createTechStack("Spring", FRAMEWORK, toyProject);

        // when
        ToyProject savedToyProject = toyProjectRepository.save(toyProject);

        // then
        assertThat(savedToyProject)
            .extracting("title", "description", "startDate")
            .contains("스터디 스케줄러", "개인의 학습 내용을 수치화해서 조회", startDate);

        assertThat(savedToyProject.getTechStacks()).hasSize(2)
            .extracting("title", "techCategory")
            .containsExactlyInAnyOrder(
                tuple("Java", LANGUAGE),
                tuple("Spring", FRAMEWORK)
            );

        assertThat(savedToyProject.getRequiredFunctions()).hasSize(2)
            .extracting("title", "description", "functionType")
            .containsExactlyInAnyOrder(
                tuple("회원가입", "신규 회원을 생성", CREATE),
                tuple("로그인", "기존 회원 존재 여부 확인", READ)
            );
    }

    @DisplayName("특정기간(기간 경계값 안에 포함)에 수행한 토이 프로젝트들을 모두 조회 한다.")
    @Test
    void findToyProjectsByPeriod() {
        // given
        LocalDate startDate1 = LocalDate.of(2023, 7, 1);
        LocalDate endDate1 = LocalDate.of(2023, 7, 21);
        LocalDate startDate2 = LocalDate.of(2023, 7, 11);
        LocalDate endDate2 = LocalDate.of(2023, 7, 31);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject1 = createToyProject("스터디 스케줄러", "개인의 학습 내용을 수치화해서 조회", startDate1, endDate1, member);
        RequiredFunction toy1AddMember = createFunction("회원가입", "신규 회원을 생성", CREATE, toyProject1);
        RequiredFunction toy1Login = createFunction("로그인", "기존 회원 존재 여부 확인", READ, toyProject1);
        TechStack toy1Language = createTechStack("Java", LANGUAGE, toyProject1);
        TechStack toy1Framwork = createTechStack("Spring", FRAMEWORK, toyProject1);

        ToyProject toyProject2 = createToyProject("상품주문 애플리케이션", "동시성 이슈를 고려한 상품 주문", startDate2, endDate2, member);
        RequiredFunction toy2AddMember = createFunction("상품 주문", "신규 주문을 생성", CREATE, toyProject2);
        RequiredFunction toy2Login = createFunction("상품 조회", "저장된 모든 상품 목록을 조회", READ, toyProject2);
        TechStack toy2Language = createTechStack("javascript", LANGUAGE, toyProject2);
        TechStack toy2Framwork = createTechStack("Node.js", FRAMEWORK, toyProject2);

        toyProjectRepository.saveAll(List.of(toyProject1, toyProject2));

        // when
        LocalDate checkStartDate = LocalDate.of(2023, 7, 1);
        LocalDate checkEndDate = LocalDate.of(2023, 7, 31);
        List<ToyProject> toyProjects = toyProjectRepository.findAllByPeriod(checkStartDate, checkEndDate);

        // then
        assertThat(toyProjects).hasSize(2)
            .extracting("title", "description", "startDate", "expectedEndDate")
            .containsExactlyInAnyOrder(
                tuple("스터디 스케줄러", "개인의 학습 내용을 수치화해서 조회", startDate1, endDate1),
                tuple("상품주문 애플리케이션", "동시성 이슈를 고려한 상품 주문", startDate2, endDate2)
            );

        assertThat(toyProjects.get(0).getTechStacks()).hasSize(2)
            .extracting("title", "techCategory")
            .containsExactlyInAnyOrder(
                tuple("Java", LANGUAGE),
                tuple("Spring", FRAMEWORK)
            );

        assertThat(toyProjects.get(0).getRequiredFunctions()).hasSize(2)
            .extracting("title", "description", "functionType")
            .containsExactlyInAnyOrder(
                tuple("회원가입", "신규 회원을 생성", CREATE),
                tuple("로그인", "기존 회원 존재 여부 확인", READ)
            );

        assertThat(toyProjects.get(1).getTechStacks()).hasSize(2)
            .extracting("title", "techCategory")
            .containsExactlyInAnyOrder(
                tuple("javascript", LANGUAGE),
                tuple("Node.js", FRAMEWORK)
            );

        assertThat(toyProjects.get(1).getRequiredFunctions()).hasSize(2)
            .extracting("title", "description", "functionType")
            .containsExactlyInAnyOrder(
                tuple("상품 주문", "신규 주문을 생성", CREATE),
                tuple("상품 조회", "저장된 모든 상품 목록을 조회", READ)
            );
    }

    @DisplayName("특정기간(경계값)에 수행한 토이 프로젝트들을 모두 조회 한다.")
    @Test
    void findAllByPeriodWithBoundaryVal() {
        // given
        LocalDate startDate1 = LocalDate.of(2023, 6, 1);
        LocalDate endDate1 = LocalDate.of(2023, 7, 1);
        LocalDate startDate2 = LocalDate.of(2023, 7, 31);
        LocalDate endDate2 = LocalDate.of(2023, 8, 16);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject1 = createToyProject("스터디 스케줄러", "개인의 학습 내용을 수치화해서 조회", startDate1, endDate1, member);
        RequiredFunction toy1AddMember = createFunction("회원가입", "신규 회원을 생성", CREATE, toyProject1);
        RequiredFunction toy1Login = createFunction("로그인", "기존 회원 존재 여부 확인", READ, toyProject1);
        TechStack toy1Language = createTechStack("Java", LANGUAGE, toyProject1);
        TechStack toy1Framwork = createTechStack("Spring", FRAMEWORK, toyProject1);

        ToyProject toyProject2 = createToyProject("상품주문 애플리케이션", "동시성 이슈를 고려한 상품 주문", startDate2, endDate2, member);
        RequiredFunction toy2AddMember = createFunction("상품 주문", "신규 주문을 생성", CREATE, toyProject2);
        RequiredFunction toy2Login = createFunction("상품 조회", "저장된 모든 상품 목록을 조회", READ, toyProject2);
        TechStack toy2Language = createTechStack("javascript", LANGUAGE, toyProject2);
        TechStack toy2Framwork = createTechStack("Node.js", FRAMEWORK, toyProject2);

        toyProjectRepository.saveAll(List.of(toyProject1, toyProject2));

        // when
        LocalDate checkStartDate = LocalDate.of(2023, 7, 1);
        LocalDate checkEndDate = LocalDate.of(2023, 7, 31);
        List<ToyProject> toyProjects = toyProjectRepository.findAllByPeriod(checkStartDate, checkEndDate);

        // then
        assertThat(toyProjects).hasSize(2)
            .extracting("title", "description", "startDate", "expectedEndDate")
            .containsExactlyInAnyOrder(
                tuple("스터디 스케줄러", "개인의 학습 내용을 수치화해서 조회", startDate1, endDate1),
                tuple("상품주문 애플리케이션", "동시성 이슈를 고려한 상품 주문", startDate2, endDate2)
            );

        assertThat(toyProjects.get(0).getTechStacks()).hasSize(2)
            .extracting("title", "techCategory")
            .containsExactlyInAnyOrder(
                tuple("Java", LANGUAGE),
                tuple("Spring", FRAMEWORK)
            );

        assertThat(toyProjects.get(0).getRequiredFunctions()).hasSize(2)
            .extracting("title", "description", "functionType")
            .containsExactlyInAnyOrder(
                tuple("회원가입", "신규 회원을 생성", CREATE),
                tuple("로그인", "기존 회원 존재 여부 확인", READ)
            );

        assertThat(toyProjects.get(1).getTechStacks()).hasSize(2)
            .extracting("title", "techCategory")
            .containsExactlyInAnyOrder(
                tuple("javascript", LANGUAGE),
                tuple("Node.js", FRAMEWORK)
            );

        assertThat(toyProjects.get(1).getRequiredFunctions()).hasSize(2)
            .extracting("title", "description", "functionType")
            .containsExactlyInAnyOrder(
                tuple("상품 주문", "신규 주문을 생성", CREATE),
                tuple("상품 조회", "저장된 모든 상품 목록을 조회", READ)
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

    private ToyProject createToyProject(String title, String description, LocalDate startDate, LocalDate endDate, Member member) {
        return ToyProject.builder()
            .title(title)
            .description(description)
            .totalExpectedPeriod(300)
            .planTimeInWeekday(60)
            .planTimeInWeekend(120)
            .startDate(startDate)
            .expectedEndDate(endDate)
            .member(member)
            .build();
    }

    private TechStack createTechStack(String title, TechCategory techCategory, ToyProject toyProject) {
        return TechStack.builder()
            .title(title)
            .techCategory(techCategory)
            .toyProject(toyProject)
            .build();
    }

    private RequiredFunction createFunction(String title, String description, FunctionType functionType, ToyProject toyProject) {
        return RequiredFunction.builder()
            .title(title)
            .description(description)
            .functionType(functionType)
            .toyProject(toyProject)
            .build();
    }
}