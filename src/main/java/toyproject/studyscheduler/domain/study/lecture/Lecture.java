package toyproject.studyscheduler.domain.study.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.user.User;

import java.time.LocalDate;

@DiscriminatorValue("Lecture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Lecture extends Study {

    private String teacherName;

    @Builder
    public Lecture(String title, String description, int totalExpectedTime, int planTimeInWeekDay, int planTimeInWeekend,
                   LocalDate startDate, LocalDate endDate, User user, String teacherName) {
        super(title, description, totalExpectedTime, planTimeInWeekDay, planTimeInWeekend, startDate, endDate, user);
        this.teacherName = teacherName;
    }
}
