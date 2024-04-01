package toyproject.studyscheduler.study.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class StudyInformation {

    private String title;
    private String description;
    private boolean isTermination;

    @Builder
    private StudyInformation(String title, String description, boolean isTermination) {
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
    }
}
