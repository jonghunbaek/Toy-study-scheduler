package toyproject.studyscheduler.study.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.exception.StudyException;

import java.time.LocalDate;

import static toyproject.studyscheduler.common.response.ResponseCode.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class StudyPeriod {

    public static final LocalDate TEMP_END_DATE = LocalDate.parse("9999-12-31");

    private LocalDate startDate;
    private LocalDate endDate;

    private StudyPeriod(LocalDate startDate, LocalDate endDate) {
        validateStartDtEarlierEndDt(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validateStartDtEarlierEndDt(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new StudyException("startDate, endDate :: " + startDate + ", " + endDate, E30001);
        }
    }

    public static StudyPeriod fromStarting(LocalDate startDate) {
        return new StudyPeriod(startDate, TEMP_END_DATE);
    }

    public static StudyPeriod fromTerminated(LocalDate startDate, LocalDate endDate) {
        return new StudyPeriod(startDate, endDate);
    }

    public void terminate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
