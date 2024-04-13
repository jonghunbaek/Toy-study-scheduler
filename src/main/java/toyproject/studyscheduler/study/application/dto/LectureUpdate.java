package toyproject.studyscheduler.study.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class LectureUpdate extends StudyUpdate {

    private String teacherName;

    @Max(message = "총 재생 시간은 최대 6,000분 이하여야 합니다.", value = 6000)
    @Min(message = "총 재생 시간은 최소 1분 이상이어야 합니다.", value = 1)
    private int totalRuntime;

    @Builder
    private LectureUpdate(String studyType, Long studyId, String title, String description, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend, String teacherName, int totalRuntime) {
        super(studyType, studyId, title, description, startDate, endDate, planMinutesInWeekday, planMinutesInWeekend);
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }
}
