package toyproject.studyscheduler.domain.study.reading;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.member.Member;

import java.time.LocalDate;

@DiscriminatorValue("READING")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reading extends Study {

    private String authorName;

    private int totalPage;

    private int readPagePerMin;

    @Builder
    private Reading(String title, String description, int totalExpectedPeriod, int planTimeInWeekday, int planTimeInWeekend,
                    LocalDate startDate, boolean isTermination, LocalDate realEndDate, Member member, String authorName, int totalPage, int readPagePerMin) {
        super(title, description, totalExpectedPeriod, totalPage/readPagePerMin, planTimeInWeekday, planTimeInWeekend,
            startDate, isTermination, realEndDate, member);
        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }
}
