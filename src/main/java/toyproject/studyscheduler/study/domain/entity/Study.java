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
import toyproject.studyscheduler.study.exception.StudyException;

import java.time.LocalDate;

import static toyproject.studyscheduler.common.response.ResponseCode.*;


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

    public LocalDate calculateExpectedDate(int totalStudyMinutes, LocalDate calculationStartDate) {
        studyInformation.validateTermination();

        PeriodCalculator calculator = PeriodCalculator.from(studyPlan, calculationStartDate);
        int expectedPeriod = calculator.calculateExpectedPeriod(getTotalMinutes() - totalStudyMinutes);

        return calculationStartDate.plusDays(expectedPeriod - 1);
    }

    public abstract int getTotalMinutes();

    public abstract boolean terminateIfSatisfiedStudyQuantity(int totalMinutes, LocalDate studyDate);

    protected void terminate(LocalDate studyDate) {
        studyInformation.terminate();
        studyPeriod.terminate(studyDate);
    }

    protected void updateStudy(StudyInformation information, StudyPeriod period, StudyPlan plan) {
        this.studyInformation = information;
        this.studyPeriod = period;
        this.studyPlan = plan;
    }

    public void validateStudyDateEarlierThanStartDate(LocalDate studyDate) {
        if (studyDate.isBefore(studyPeriod.getStartDate())) {
            throw new StudyException("studyDate :: " + studyDate, E30003);
        }
    }
}
