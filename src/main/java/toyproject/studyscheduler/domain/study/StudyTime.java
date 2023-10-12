package toyproject.studyscheduler.domain.study;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StudyTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalCompleteTime;

    private int completeTimeToday;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @Builder
    private StudyTime(int previousTotalCompleteTime, int completeTimeToday, LocalDate date, Study study) {
        this.totalCompleteTime = previousTotalCompleteTime + completeTimeToday;
        this.completeTimeToday = completeTimeToday;
        this.date = date;
        this.study = study;
    }

    // TODO : 로직 수정해야함
    public double calculateLearningRate() {
        return Math.round((((float) totalCompleteTime /study.getTotalExpectedPeriod())*100)*100)/100.0;
    }
}
