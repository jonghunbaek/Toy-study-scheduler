package toyproject.studyscheduler.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import toyproject.studyscheduler.domain.study.StudyType;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
public class StudyPlanTimeRequestDto {

    private StudyType studyType;
    private int planTimeInWeekDay;
    private int planTimeInWeekend;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private int totalRunTime;

    private int totalPage;
    private int readPagePerMin;

    private List<Integer> expectedTimes;

    @Builder
    private StudyPlanTimeRequestDto(StudyType studyType, int planTimeInWeekDay, int planTimeInWeekend, LocalDate startDate, int totalRunTime, int totalPage, int readPagePerMin, List<Integer> expectedTimes) {
        this.studyType = studyType;
        this.planTimeInWeekDay = planTimeInWeekDay;
        this.planTimeInWeekend = planTimeInWeekend;
        this.startDate = startDate;
        this.totalRunTime = totalRunTime;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
        this.expectedTimes = expectedTimes;
    }
}
