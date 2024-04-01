package toyproject.studyscheduler.study.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class StudyPeriod {

    private LocalDate startDate;
    private LocalDate endDate;

    private StudyPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static StudyPeriod fromStarting(LocalDate startDate) {
        return new StudyPeriod(startDate, LocalDate.MIN);
    }

    public static StudyPeriod fromTerminated(LocalDate startDate, LocalDate endDate) {
        return new StudyPeriod(startDate, endDate);
    }
}
