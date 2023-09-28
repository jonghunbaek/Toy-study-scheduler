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

    private int totalExpectedTime;

    private int planTimeInWeekDay;

    private int planTimeInWeekend;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    protected Study(String title, String description, int totalExpectedTime, int planTimeInWeekDay, int planTimeInWeekend,
                    LocalDate startDate, LocalDate endDate, Member member) {
        super(title, description);
        this.totalExpectedTime = totalExpectedTime;
        this.planTimeInWeekDay = planTimeInWeekDay;
        this.planTimeInWeekend = planTimeInWeekend;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
    }
}
