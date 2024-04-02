package toyproject.studyscheduler.study.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.common.domain.BaseEntity;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;

import java.time.LocalDate;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorColumn(name = "study_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StudyInformation studyInformation;

    @Embedded
    private StudyPeriod studyPeriod;

    @Embedded
    private StudyPlan studyPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    protected Study(StudyInformation studyInformation, StudyPeriod studyPeriod, StudyPlan studyPlan, Member member) {
        this.studyInformation = studyInformation;
        this.studyPeriod = studyPeriod;
        this.studyPlan = studyPlan;
        this.member = member;
    }

    public LocalDate getStartDate() {
        return this.studyPeriod.getStartDate();
    }

    public abstract int getTotalQuantity();

    public abstract int calculatePlanQuantityPerDay(int planMinutesInWeekday);
}
