package toyproject.studyscheduler.study.entity.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class StudyBaseInfo {

    private String title;
    private String description;
    private boolean isTermination;

    @Builder
    private StudyBaseInfo(String title, String description, boolean isTermination) {
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
    }
}
