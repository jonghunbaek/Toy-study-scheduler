package toyproject.studyscheduler.domain.study.lecture;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.study.Study;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Lecture extends BaseEntity implements Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;
    private int runtime;

    private int completeRuntime;

    private int planTimeInWeekDay;

    private int planTimeInWeekend;

    private String progress;
    @Builder
    public Lecture(String title, int runtime, int completeRuntime, String progress, int planTimeInWeekDay, int planTimeInWeekend) {
        this.title = title;
        this.runtime = runtime;
        this.completeRuntime = completeRuntime;
        this.progress = progress;
        this.planTimeInWeekDay = planTimeInWeekDay;
        this.planTimeInWeekend = planTimeInWeekend;
    }
}
