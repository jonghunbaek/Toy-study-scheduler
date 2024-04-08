package toyproject.studyscheduler.study.application.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.StudyType;
import toyproject.studyscheduler.study.domain.entity.Reading;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ReadingSaveDto extends StudySaveSpec {

    @NotBlank
    private String authorName;

    @Max(5000)
    @Min(1)
    private int totalPage;

    @Max(30)
    @Min(1)
    private int readPagePerMin;

    @Builder
    private ReadingSaveDto(String studyType, String title, String description, boolean isTermination,
                           LocalDate startDate, LocalDate endDate,
                           int planMinutesInWeekday, int planMinutesInWeekend,
                           String authorName, int totalPage, int readPagePerMin) {

        super(studyType, title, description, isTermination, startDate, endDate, planMinutesInWeekday, planMinutesInWeekend);
        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }

    @Override
    protected Study createStudy(StudyInformation information, StudyPeriod period, StudyPlan plan) {
        return Reading.builder()
            .studyInformation(information)
            .studyPeriod(period)
            .studyPlan(plan)
            .authorName(this.authorName)
            .totalPage(this.totalPage)
            .readPagePerMin(this.readPagePerMin)
            .build();
    }
}
