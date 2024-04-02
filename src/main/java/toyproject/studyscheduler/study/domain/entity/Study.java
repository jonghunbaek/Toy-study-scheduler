package toyproject.studyscheduler.study.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.common.domain.BaseEntity;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.PeriodCalculator;
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

    // TODO :: 계산 전 해당 학습의 종료 여부를 검증 및 관련 테스트 작성
    public LocalDate calculateExpectedDate() {
        PeriodCalculator calculator = createCalculator();
        int expectedPeriod = calculator.calculateExpectedPeriod(getTotalQuantity());

        return studyPeriod.getStartDate()
            .plusDays(expectedPeriod - 1);
    }

    private PeriodCalculator createCalculator() {
        int planQuantityInWeekday = calculatePlanQuantityPerDay(studyPlan.getPlanMinutesInWeekday());
        int planQuantityInWeekend = calculatePlanQuantityPerDay(studyPlan.getPlanMinutesInWeekend());

        return new PeriodCalculator(planQuantityInWeekday, planQuantityInWeekend, studyPeriod.getStartDate());
    }

    protected abstract int getTotalQuantity();

    protected abstract int calculatePlanQuantityPerDay(int planMinutesInWeekday);
}
