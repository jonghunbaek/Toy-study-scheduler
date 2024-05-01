package toyproject.studyscheduler.dailystudy.web.dto;

import lombok.Getter;
import toyproject.studyscheduler.study.domain.StudyType;

import java.time.LocalDate;

@Getter
public class StudyRemaining {

    private final StudyType studyType;
    private final LocalDate expectedEndDate;
    private final int remainingQuantity;
    private final boolean isTermination;

    private StudyRemaining(StudyType studyType, LocalDate expectedEndDate, int remainingQuantity, boolean isTermination) {
        this.studyType = studyType;
        this.expectedEndDate = expectedEndDate;
        this.remainingQuantity = remainingQuantity;
        this.isTermination = isTermination;
    }

    public static StudyRemaining from(StudyType studyType, LocalDate expectedEndDate, int remainingQuantity, boolean isTermination) {
        return new StudyRemaining(studyType, expectedEndDate, remainingQuantity, isTermination);
    }
}
