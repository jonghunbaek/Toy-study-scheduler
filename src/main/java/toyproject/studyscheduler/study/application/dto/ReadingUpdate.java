package toyproject.studyscheduler.study.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.entity.Reading;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReadingUpdate extends StudyUpdate {

    private String authorName;

    @Max(message = "총 페이지 수는 최대 2,000페이지 이하여야 합니다.", value = 2000)
    @Min(message = "총 재생 시간은 최소 1페이지 이상이어야 합니다.", value = 1)
    private int totalPage;

    @Max(message = "분당 읽는 페이지 수는 최대 30페이지 이하여야 합니다.", value = 30)
    @Min(message = "분당 읽는 페이지 수는 최소 1페이지 이상이어야 합니다.", value = 1)
    private int readPagePerMin;

    @Builder
    private ReadingUpdate(String studyType, Long studyId, String title, String description, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend, String authorName, int totalPage, int readPagePerMin) {
        super(studyType, title, description, startDate, endDate, planMinutesInWeekday, planMinutesInWeekend);
        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }

    @Override
    protected void updateStudy(Study study, StudyInformation information, StudyPeriod period, StudyPlan plan) {
        Reading reading = (Reading) study;

        reading.updateReading(information, period, plan, authorName, totalPage, readPagePerMin);
    }
}
