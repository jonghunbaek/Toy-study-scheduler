package toyproject.studyscheduler.domain.study.reading;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.user.User;

import java.time.LocalDate;

@DiscriminatorValue("Reading")
@NoArgsConstructor
@Getter
@Entity
public class Reading extends Study {

    private String authorName;

    private int totalPage;

    private int readPagePerMin;

    @Builder
    private Reading(String title, String description, int totalExpectedTime, int planTimeInWeekDay, int planTimeInWeekend,
                   LocalDate startDate, LocalDate endDate, User user, String authorName, int totalPage, int readPagePerMin) {
        super(title, description, totalExpectedTime, planTimeInWeekDay, planTimeInWeekend, startDate, endDate, user);
        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }
}
