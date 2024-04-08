package toyproject.studyscheduler.study.application.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.StudyType;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "studyType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LectureSaveDto.class, name = StudyType.Values.LECTURE),
        @JsonSubTypes.Type(value = ReadingSaveDto.class, name = StudyType.Values.READING)
})
@Getter
@NoArgsConstructor
public abstract class StudySave {

    private String studyType;

    @NotBlank(message = "제목은 필수 값입니다.")
    private String title;

    @Size(message = "학습에 대한 설명은 최대 500글자 까지 작성할 수 있습니다.",max = 500)
    private String description;

    private boolean isTermination;

    @NotNull(message = "시작일은 필수 값입니다.")
    private LocalDate startDate;

    private LocalDate endDate;

    @Max(message = "학습 계획 시간은 최대 720분 이하여야 합니다.", value = 720)
    @Min(message = "학습 계획 시간은 최소 1분 이상이어야 합니다.", value = 1)
    private int planMinutesInWeekday;

    @Max(message = "학습 계획 시간은 최대 720분 이하여야 합니다.", value = 720)
    @Min(message = "학습 계획 시간은 최소 1분 이상이어야 합니다.", value = 1)
    private int planMinutesInWeekend;

    protected StudySave(String studyType, String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend) {
        this.studyType = studyType;
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
    }

    public Study toStudy() {
        StudyInformation information = createStudyInfo();
        StudyPeriod period = createStudyPeriod();
        StudyPlan plan = createStudyPlan();

        return createStudy(information, period, plan);
    }

    protected abstract Study createStudy(StudyInformation information, StudyPeriod period, StudyPlan plan);

    private StudyInformation createStudyInfo() {
        return StudyInformation.builder()
            .title(this.title)
            .description(this.description)
            .isTermination(this.isTermination)
            .build();
    }

    private StudyPeriod createStudyPeriod() {
        if (isTermination) {
            return StudyPeriod.fromTerminated(this.startDate, this.endDate);
        }

        return StudyPeriod.fromStarting(this.startDate);
    }

    private StudyPlan createStudyPlan() {
        if (isTermination) {
            return StudyPlan.fromTerminated();
        }

        return StudyPlan.fromStarting(this.planMinutesInWeekday, this.planMinutesInWeekend);
    }
}
