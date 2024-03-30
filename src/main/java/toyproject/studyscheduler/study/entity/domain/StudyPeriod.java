package toyproject.studyscheduler.study.entity.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class StudyPeriod {

    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    private StudyPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
