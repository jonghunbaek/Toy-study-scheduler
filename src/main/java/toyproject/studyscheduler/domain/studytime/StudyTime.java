package toyproject.studyscheduler.domain.studytime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.study.Study;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StudyTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalCompleteTime;

    private double totalLearningRate;

    private int completeTimeToday;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @Builder
    private StudyTime(int totalCompleteTime, int completeTimeToday, LocalDate date, Study study) {
        this.totalCompleteTime = totalCompleteTime + completeTimeToday;
        this.completeTimeToday = completeTimeToday;
        this.date = date;
        this.study = study;
        this.totalLearningRate = calculateLearningRate();
    }

    private double calculateLearningRate() {
        return Math.round((((float) totalCompleteTime / study.getTotalExpectedMin())*100)*100)/100.0;
    }
}
