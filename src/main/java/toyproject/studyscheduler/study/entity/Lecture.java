package toyproject.studyscheduler.study.entity;

import jakarta.persistence.*;
import lombok.*;
import toyproject.studyscheduler.study.entity.Study;
import toyproject.studyscheduler.member.entity.Member;

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
                   LocalDate startDate, boolean isTermination, LocalDate realEndDate, Member member, String teacherName, int totalRuntime) {
        super(title, description, totalExpectedPeriod, totalRuntime, planTimeInWeekday, planTimeInWeekend,
            startDate, isTermination, realEndDate, member);

        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }
}
