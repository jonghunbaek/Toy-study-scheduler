package toyproject.studyscheduler.domain.study;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseInfoEntity;
import toyproject.studyscheduler.domain.member.Member;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorColumn(name = "study_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Study extends BaseInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="study_type", insertable = false, updatable = false)
    private String studyType;

    private int totalExpectedPeriod;

    private int planTimeInWeekday;

    private int planTimeInWeekend;

    private LocalDate startDate;

    private LocalDate expectedEndDate;

    private boolean isTermination;

    private LocalDate realEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    protected Study(String title, String description, int totalExpectedPeriod, int planTimeInWeekday, int planTimeInWeekend,
                    LocalDate startDate, LocalDate expectedEndDate, boolean isTermination, LocalDate realEndDate, Member member) {
        super(title, description);
        this.totalExpectedPeriod = totalExpectedPeriod;
        this.planTimeInWeekday = planTimeInWeekday;
        this.planTimeInWeekend = planTimeInWeekend;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.member = member;
        this.isTermination = isTermination;
        if (isTermination) {
            this.realEndDate = realEndDate;
            return;
        }
        this.realEndDate = LocalDate.MAX;
    }

    public void terminateStudyIn(LocalDate realEndDate) {
        this.isTermination = true;
        this.realEndDate = realEndDate;
    }
}
