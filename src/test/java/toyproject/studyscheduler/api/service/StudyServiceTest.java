package toyproject.studyscheduler.api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.api.request.FindStudyRequestDto;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.lecture.LectureRepository;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.reading.ReadingRepository;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.study.repository.StudyTimeRepository;
import toyproject.studyscheduler.domain.study.toyproject.ToyProjectRepository;
import toyproject.studyscheduler.domain.techstack.TechCategory;
import toyproject.studyscheduler.domain.techstack.TechStack;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static toyproject.studyscheduler.domain.techstack.TechCategory.FRAMEWORK;
import static toyproject.studyscheduler.domain.techstack.TechCategory.LANGUAGE;
import static toyproject.studyscheduler.domain.function.FunctionType.CREATE;
import static toyproject.studyscheduler.domain.function.FunctionType.READ;

@ActiveProfiles("test")
@SpringBootTest
class StudyServiceTest {

    @Autowired
    StudyService studyService;
    @Autowired
    StudyRepository studyRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    ReadingRepository readingRepository;
    @Autowired
    ToyProjectRepository toyProjectRepository;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("주어진 여러개의 아이디로 여러개의 학습 상세내용을 조회한다.")
    @Test
    void findStudiesByIds() {
        // given
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 8, 3);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject = createToyProject(startDate, endDate, member);
        RequiredFunction function1 = createFunction(CREATE, toyProject);
        RequiredFunction function2 = createFunction(READ, toyProject);
        TechStack stack1 = createTechStack("Java", LANGUAGE, toyProject);
        TechStack stack2 = createTechStack("Spring", FRAMEWORK, toyProject);

        Lecture lecture = createLecture(startDate, endDate, member);
        Reading reading = createReading(startDate, endDate, member);

        Lecture savedLecture = lectureRepository.save(lecture);
        Reading savedReading = readingRepository.save(reading);
        ToyProject savedToyProject = toyProjectRepository.save(toyProject);

        List<FindStudyRequestDto> requestDto = new ArrayList<>(List.of(new FindStudyRequestDto(savedLecture.getId()),
            new FindStudyRequestDto(savedReading.getId()),
            new FindStudyRequestDto(savedToyProject.getId())));

        // TODO : 싱글테이블 전략에서 dtype 조회하는 방법 찾기
        // when
//        List<FindStudyResponseDto>
//
//        // then
//        assertThat(studies).hasSize(3)
//            .extracting("title", "description", "startDate")
//            .containsExactlyInAnyOrder(
//                tuple("김영한의 스프링", "스프링 핵심 강의", startDate),
//                tuple("클린 코드", "클린 코드를 배우기 위한 도서", startDate),
//                tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", startDate)
//            );

    }

    @DisplayName("특정기간에 수행한 학습들을 모두 조회 한다.")
    @Test
    void findStudiesByPeriod() {
        // given
        LocalDate startDate1 = LocalDate.of(2023, 7, 1);
        LocalDate endDate1 = LocalDate.of(2023, 8, 3);
        LocalDate startDate2 = LocalDate.of(2023, 7, 8);
        LocalDate endDate2 = LocalDate.of(2023, 7, 16);
        LocalDate startDate3 = LocalDate.of(2023, 7, 31);
        LocalDate endDate3 = LocalDate.of(2023, 8, 30);

        Member member = createMember();
        memberRepository.save(member);

        ToyProject toyProject = createToyProject(startDate1, endDate1, member);
        RequiredFunction function1 = createFunction(CREATE, toyProject);
        RequiredFunction function2 = createFunction(READ, toyProject);
        TechStack stack1 = createTechStack("Java", LANGUAGE, toyProject);
        TechStack stack2 = createTechStack("Spring", FRAMEWORK, toyProject);

        Lecture lecture = createLecture(startDate2, endDate2, member);
        Reading reading = createReading(startDate3, endDate3, member);
        List<Study> studies1 = studyRepository.saveAll(List.of(lecture, reading, toyProject));

        // when
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);

        List<Study> studies = studyRepository.findAllByPeriod(startDate, endDate);

        // then
        assertThat(studies).hasSize(3)
            .extracting("title", "description", "startDate")
            .containsExactlyInAnyOrder(
                tuple("스터디 스케쥴러", "개인의 학습의 진도율을 관리", startDate1),
                tuple("김영한의 스프링", "스프링 핵심 강의", startDate2),
                tuple("클린 코드", "클린 코드를 배우기 위한 도서", startDate3)
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

    private TechStack createTechStack(String title, TechCategory techCategory, ToyProject toyProject) {
        return TechStack.builder()
            .title(title)
            .techCategory(techCategory)
            .toyProject(toyProject)
            .build();
    }

    private RequiredFunction createFunction(FunctionType functionType, ToyProject toyProject) {
        return RequiredFunction.builder()
            .functionType(READ)
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