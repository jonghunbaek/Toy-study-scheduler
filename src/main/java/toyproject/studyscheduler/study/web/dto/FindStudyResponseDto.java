package toyproject.studyscheduler.study.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.entity.domain.StudyType;
import toyproject.studyscheduler.study.entity.Lecture;
import toyproject.studyscheduler.study.entity.Reading;

import java.time.LocalDate;

import static toyproject.studyscheduler.study.entity.domain.StudyType.toEnum;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FindStudyResponseDto {

    private String title;
    private String description;
    private StudyType studyType;
    private int totalExpectedPeriod;
    private int planTimeInWeekday;
    private int planTimeInWeekend;
    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private boolean isTermination;
    private LocalDate realEndDate;
    private Long memberId;

    // Reading detail
    private String authorName;
    private int totalPage;
    private int readPagePerMin;

    // Lecture detail
    private String teacherName;
    private int totalRuntime;

    public static FindStudyResponseDto ofLecture(Lecture lecture) {
        return null;
    }

    public static FindStudyResponseDto ofReading(Reading reading) {
        return null;
    }
}
