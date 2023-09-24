package toyproject.studyscheduler.domain.function;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static toyproject.studyscheduler.domain.function.FunctionType.*;
import static toyproject.studyscheduler.domain.techstack.TechCategory.*;
import static toyproject.studyscheduler.domain.techstack.TechCategory.CLOUD;

@ActiveProfiles("test")
@DataJpaTest
class RequiredFunctionRepositoryTest {

    @Autowired
    RequiredFunctionRepository requiredFunctionRepository;
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

        RequiredFunction addMember = createFunction("회원 가입", "존재하는 유저 정보가 없을 경우 신규 생성", CREATE, toyProject);
        RequiredFunction login = createFunction("로그인", "존재하는 유저 정보가 있을 경우 세션 생성", READ, toyProject);
        RequiredFunction logout = createFunction("로그아웃", "세션 종료", ETC, toyProject);
        RequiredFunction updateMember = createFunction("회원 정보 수정", "회원의 정보를 수정", UPDATE, toyProject);
        RequiredFunction deleteMember = createFunction("회원 탈퇴", "존재하는 유저 정보를 삭제", DELETE, toyProject);

        memberRepository.save(member);
        toyProjectRepository.save(toyProject);
        requiredFunctionRepository.saveAll(List.of(addMember, login, logout, updateMember, deleteMember));

        // when
        List<RequiredFunction> functions = requiredFunctionRepository.findAll();

        // then
        assertThat(functions).hasSize(5)
            .extracting("title", "description", "functionType")
            .containsExactlyInAnyOrder(
                tuple("회원 가입", "존재하는 유저 정보가 없을 경우 신규 생성", CREATE),
                tuple("로그인", "존재하는 유저 정보가 있을 경우 세션 생성", READ),
                tuple("로그아웃", "세션 종료", ETC),
                tuple("회원 정보 수정", "회원의 정보를 수정", UPDATE),
                tuple("회원 탈퇴", "존재하는 유저 정보를 삭제", DELETE)
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

    private RequiredFunction createFunction(String title, String description, FunctionType functionType, ToyProject toyProject) {
        return RequiredFunction.builder()
            .title(title)
            .description(description)
            .functionType(functionType)
            .toyProject(toyProject)
            .build();
    }
}