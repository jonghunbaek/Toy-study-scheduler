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
    @Enumerated(EnumType.STRING)
    private StudyType studyType;

    protected int totalExpectedPeriod;

    private int totalExpectedMin;

    private int planTimeInWeekday;

    private int planTimeInWeekend;

    private LocalDate startDate;

    private LocalDate expectedEndDate;

    private boolean isTermination;

    private LocalDate realEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    protected Study(String title, String description, int totalExpectedPeriod, int totalExpectedMin, int planTimeInWeekday, int planTimeInWeekend,
                    LocalDate startDate, boolean isTermination, LocalDate realEndDate, Member member) {
        super(title, description);
        this.totalExpectedPeriod = totalExpectedPeriod;
        this.totalExpectedMin = totalExpectedMin;
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

    protected void updateTotalExpectedMin(int totalExpectedMin) {
        this.totalExpectedMin = totalExpectedMin;
    }
}
