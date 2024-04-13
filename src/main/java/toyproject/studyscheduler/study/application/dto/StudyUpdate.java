package toyproject.studyscheduler.study.application.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.StudyType;

import java.time.LocalDate;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "studyType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LectureUpdate.class, name = StudyType.Values.LECTURE),
        @JsonSubTypes.Type(value = ReadingSave.class, name = StudyType.Values.READING)
})
@Getter
@NoArgsConstructor
public abstract class StudyUpdate {

    private String studyType;

    @NotNull(message = "학습 id는 필수 값입니다.")
    @Min(1)
    private Long studyId;

    @NotBlank(message = "제목은 필수 값입니다.")
    private String title;

    @Size(message = "학습에 대한 설명은 최대 500글자 까지 작성할 수 있습니다.",max = 500)
    private String description;

    @NotNull(message = "시작일은 필수 값입니다.")
    private LocalDate startDate;

    @NotNull(message = "시작일은 필수 값입니다.")
    private LocalDate endDate;

    @Max(message = "학습 계획 시간은 최대 720분 이하여야 합니다.", value = 720)
    @Min(message = "학습 계획 시간은 최소 1분 이상이어야 합니다.", value = 1)
    private int planMinutesInWeekday;

    @Max(message = "학습 계획 시간은 최대 720분 이하여야 합니다.", value = 720)
    @Min(message = "학습 계획 시간은 최소 1분 이상이어야 합니다.", value = 1)
    private int planMinutesInWeekend;

    protected StudyUpdate(String studyType, Long studyId, String title, String description, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend) {
        this.studyType = studyType;
        this.studyId = studyId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
    }
}
