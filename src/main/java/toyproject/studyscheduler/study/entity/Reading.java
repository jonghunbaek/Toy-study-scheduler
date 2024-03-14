package toyproject.studyscheduler.study.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.entity.Study;
import toyproject.studyscheduler.member.entity.Member;

import java.time.LocalDate;

@DiscriminatorValue("reading")
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
