package toyproject.studyscheduler.study.application.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.StudyType;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

import static toyproject.studyscheduler.study.domain.StudyPeriod.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "studyType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LectureUpdate.class, name = StudyType.Values.LECTURE),
        @JsonSubTypes.Type(value = ReadingUpdate.class, name = StudyType.Values.READING)
})
@NoArgsConstructor
public abstract class StudyUpdate {

    private String studyType;

    @NotBlank(message = "제목은 필수 값입니다.")
    private String title;

    @Size(message = "학습에 대한 설명은 최대 500글자 까지 작성할 수 있습니다.",max = 500)
    private String description;

    @NotNull(message = "시작일은 필수 값입니다.")
    private LocalDate startDate;

    private LocalDate endDate = TEMP_END_DATE;

    @Max(message = "학습 계획 시간은 최대 720분 이하여야 합니다.", value = 720)
    @Min(message = "학습 계획 시간은 최소 1분 이상이어야 합니다.", value = 1)
    private int planMinutesInWeekday;

    @Max(message = "학습 계획 시간은 최대 720분 이하여야 합니다.", value = 720)
    @Min(message = "학습 계획 시간은 최소 1분 이상이어야 합니다.", value = 1)
    private int planMinutesInWeekend;

    protected StudyUpdate(String studyType, String title, String description, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend) {
        this.studyType = studyType;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
    }

    public void update(Study study) {
        boolean isTermination = study.getStudyInformation().isTermination();
        StudyInformation information = createStudyInfo();
        StudyPeriod period = createStudyPeriod(isTermination);
        StudyPlan plan = createStudyPlan(isTermination);

        updateStudy(study, information, period, plan);
    }

    private StudyInformation createStudyInfo() {
        return StudyInformation.builder()
                .title(this.title)
                .description(this.description)
                .build();
    }

    private StudyPeriod createStudyPeriod(boolean isTermination) {
        if (isTermination) {
            return fromTerminated(this.startDate, this.endDate);
        }

        return fromStarting(this.startDate);
    }

    private StudyPlan createStudyPlan(boolean isTermination) {
        if (isTermination) {
            return StudyPlan.fromTerminated();
        }

        return StudyPlan.fromStarting(this.planMinutesInWeekday, this.planMinutesInWeekend);
    }

    protected abstract void updateStudy(Study study, StudyInformation information, StudyPeriod period, StudyPlan plan);
}
