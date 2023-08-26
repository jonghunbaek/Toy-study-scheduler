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
    private StudyTime(int totalCompleteTime, int completeTimeToday, LocalDate date, Study study) {
        this.totalCompleteTime = totalCompleteTime;
        this.completeTimeToday = completeTimeToday;
        this.date = date;
        this.study = study;
    }

    public double calculateLearningRate() {
        return Math.round((((float) totalCompleteTime /study.getTotalExpectedTime())*100)*100)/100.0;
    }

    public int calculateTotalCompleteTime(int completeTimeToday) {
        return totalCompleteTime + completeTimeToday;
    }
}
