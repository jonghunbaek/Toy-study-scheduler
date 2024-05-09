package toyproject.studyscheduler.study.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.common.domain.BaseEntity;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.*;
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

    /**
     * 학습 기본 정보
     */
    @Embedded
    private StudyInformation studyInformation;

    /**
     * 학습 기간. 새로 시작한 학습의 경우 임시 종료일이 설정됨
     */
    @Embedded
    private StudyPeriod studyPeriod;

    /**
     * 학습 계획. 평일, 주말의 계획 학습 시간
     */
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

    /**
     * 학습 예상 종료일을 계산
     * @param totalStudyMinutes - 해당 학습에 대해 총 학습한 시간
     * @param calculationStartDate - 계산 시작일
     */
    public final LocalDate calculateExpectedDate(int totalStudyMinutes, LocalDate calculationStartDate) {
        studyInformation.validateTermination();

        PeriodCalculator calculator = PeriodCalculator.from(studyPlan, calculationStartDate);
        int expectedPeriod = calculator.calculateExpectedPeriod(getTotalMinutes() - totalStudyMinutes);

        return calculationStartDate.plusDays(expectedPeriod - 1);
    }

    protected final void terminate(LocalDate studyDate) {
        studyInformation.terminate();
        studyPeriod.terminate(studyDate);
    }

    protected final void updateStudy(StudyInformation information, StudyPeriod period, StudyPlan plan) {
        this.studyInformation = information;
        this.studyPeriod = period;
        this.studyPlan = plan;
    }

    /**
     * 일일 학습일이 학습 시작일 보다 앞서있는지 검증
     */
    public final void validateStudyDateEarlierThanStartDate(LocalDate studyDate) {
        studyPeriod.validateStudyDateEalierThanStartDate(studyDate);
    }

    /**
     * 학습한 총 시간(분)을 인자로 받아 남은 학습량을 계산해 반환. 강의는 분, 도서는 쪽수를 반환
     */
    public abstract int calculateRemainingQuantity(int totalStudyMinutes);

    /**
     * 학습한 총 시간(분)을 인자로 받아 해당 학습이 종료될 수 있는지 검증 후 만족하면 종료
     */
    public abstract boolean terminateIfSatisfiedStudyQuantity(int totalStudyMinutes, LocalDate studyDate);

    /**
     * 학습량을 시간(분)으로 변환해 반환
     */
    protected abstract int getTotalMinutes();

    public abstract StudyType getType();
}
