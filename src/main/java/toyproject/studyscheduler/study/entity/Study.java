package toyproject.studyscheduler.study.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.member.entity.Member;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorColumn(name = "study_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int planTimeInWeekday;

    private int planTimeInWeekend;

    private LocalDate startDate;

    private LocalDate expectedEndDate;

    private LocalDate realEndDate;

    private boolean isTermination;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    protected Study(String title, String description, int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, LocalDate expectedEndDate, LocalDate realEndDate, boolean isTermination, Member member) {
        this.title = title;
        this.description = description;
        this.planTimeInWeekday = planTimeInWeekday;
        this.planTimeInWeekend = planTimeInWeekend;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.realEndDate = realEndDate;
        this.isTermination = isTermination;
        this.member = member;
    }

    protected Study(String title, String description, int totalExpectedPeriod, int planTimeInWeekday, int planTimeInWeekend,
                    LocalDate startDate, boolean isTermination, LocalDate realEndDate, Member member) {
        this.title = title;
        this.description = description;
        this.planTimeInWeekday = planTimeInWeekday;
        this.planTimeInWeekend = planTimeInWeekend;
        this.startDate = startDate;
        this.expectedEndDate = startDate.plusDays(totalExpectedPeriod - 1);
        this.member = member;
        this.isTermination = isTermination;
        if (isTermination) {
            this.realEndDate = realEndDate;
            return;
        }
        this.realEndDate = LocalDate.EPOCH;
    }

    public void terminateStudyIn(LocalDate realEndDate) {
        this.isTermination = true;
        this.realEndDate = realEndDate;
    }
}
