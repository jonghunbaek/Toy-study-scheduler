package toyproject.studyscheduler.dailystudy.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.common.domain.BaseEntity;
import toyproject.studyscheduler.study.domain.entity.Study;

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
        this.totalLearningRate = 0;
    }

//    private double calculateLearningRate() {
//        return Math.round((((float) totalCompleteTime / study.getTotalExpectedMin())*100)*100)/100.0;
//    }
}
