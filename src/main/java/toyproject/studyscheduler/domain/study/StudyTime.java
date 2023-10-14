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

    // TODO : 로직 수정해야함
    // 학습율을 저장할 변수와 총 학습시간(분)을 저장하는 변수 추가 필요
    private double calculateLearningRate() {
        return Math.round((((float) totalCompleteTime / study.getTotalExpectedMin())*100)*100)/100.0;
    }
}
