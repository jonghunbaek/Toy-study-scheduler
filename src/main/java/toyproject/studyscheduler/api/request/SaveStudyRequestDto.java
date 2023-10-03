package toyproject.studyscheduler.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SaveStudyRequestDto {

    private String title;
    private String description;
    private String studyType;
    private int totalExpectedPeriod;
    private int planTimeInWeekday;
    private int planTimeInWeekend;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Long memberId;

    // Reading detail
    private String authorName;
    private int totalPage;
    private int readPagePerMin;

    // Lecture detail
    private String teacherName;
    private int totalRuntime;
}
