package toyproject.studyscheduler.study.application.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.StudyType;
import toyproject.studyscheduler.study.domain.entity.Lecture;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class LectureSaveDto extends StudySaveSpec {

    @NotBlank
    private String teacherName;

    @Max(6000)
    @Min(1)
    private int totalRuntime;

    @Builder
    private LectureSaveDto(String studyType, String title, String description, boolean isTermination,
                           LocalDate startDate, LocalDate endDate,
                           int planMinutesInWeekday, int planMinutesInWeekend,
                           String teacherName, int totalRuntime) {

        super(studyType, title, description, isTermination, startDate, endDate, planMinutesInWeekday, planMinutesInWeekend);
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }

    @Override
    protected Study createStudy(StudyInformation information, StudyPeriod period, StudyPlan plan) {
        return Lecture.builder()
            .studyInformation(information)
            .studyPeriod(period)
            .studyPlan(plan)
            .teacherName(this.teacherName)
            .totalRuntime(this.totalRuntime)
            .build();
    }
}
