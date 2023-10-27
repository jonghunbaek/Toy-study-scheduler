package toyproject.studyscheduler.domain.study;

import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.studytime.StudyTime;

import java.time.LocalDate;


class StudyTimeTest {

    private StudyTime createStudyTime(Study lecture, LocalDate today, int completeTimeToday, int totalCompleteTime) {
        return StudyTime.builder()
                .totalCompleteTime(totalCompleteTime)
                .date(today)
                .completeTimeToday(completeTimeToday)
                .study(lecture)
                .build();
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

    private static Lecture createLecture(Member member) {
        return Lecture.builder()
            .title("김영한의 스프링")
            .description("스프링 핵심 강의")
            .teacherName("김영한")
            .planTimeInWeekday(30)
            .planTimeInWeekend(100)
            .startDate(LocalDate.of(2023,8,12))
            .member(member)
            .build();
    }
}