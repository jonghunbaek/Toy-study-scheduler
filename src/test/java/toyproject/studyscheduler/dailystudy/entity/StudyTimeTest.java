package toyproject.studyscheduler.dailystudy.entity;

import toyproject.studyscheduler.member.entity.domain.AccountType;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.study.entity.Study;
import toyproject.studyscheduler.study.entity.Lecture;
import toyproject.studyscheduler.dailystudy.entity.StudyTime;

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