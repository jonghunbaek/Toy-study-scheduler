package toyproject.studyscheduler.study.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor
public class ReadingSaveDto implements StudyDtoSpec {

    private String title;
    private String description;
    private boolean isTermination;
    private LocalDate startDate;
    private LocalDate endDate = LocalDate.MAX;
    private int planMinutesInWeekday;
    private int planMinutesInWeekend;

    @Getter
    private String authorName;

    @Getter
    private int totalPage;

    @Getter
    private int readPagePerMin;

    @Builder
    private ReadingSaveDto(String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend, String authorName, int totalPage, int readPagePerMin) {
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }

    @Override
    public Study toEntity() {
        return null;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean isTermination() {
        return this.isTermination;
    }

    @Override
    public LocalDate getStartDate() {
        return this.startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return this.endDate;
    }

    @Override
    public int getPlanMinutesInWeekday() {
        return this.planMinutesInWeekday;
    }

    @Override
    public int getPlanMinutesInWeekend() {
        return this.planMinutesInWeekend;
    }
}
