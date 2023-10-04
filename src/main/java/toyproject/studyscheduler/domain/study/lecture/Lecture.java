package toyproject.studyscheduler.domain.study.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.member.Member;

import java.time.LocalDate;

@DiscriminatorValue("lecture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Lecture extends Study {

    private String teacherName;

    private int totalRuntime;


    @Builder
    private Lecture(String title, String description, int totalExpectedPeriod, int planTimeInWeekday, int planTimeInWeekend,
                   LocalDate startDate, LocalDate expectedEndDate, Member member, String teacherName, int totalRuntime) {
        super(title, description, totalExpectedPeriod, planTimeInWeekday, planTimeInWeekend, startDate, expectedEndDate, member);
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }
}
