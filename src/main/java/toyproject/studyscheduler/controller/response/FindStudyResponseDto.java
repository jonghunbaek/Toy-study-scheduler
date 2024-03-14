package toyproject.studyscheduler.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.StudyType;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;

import java.time.LocalDate;

import static toyproject.studyscheduler.domain.study.StudyType.toEnum;

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
        return FindStudyResponseDto.builder()
            .title(lecture.getTitle())
            .description(lecture.getDescription())
            .studyType(toEnum(lecture.getStudyType()))
            .totalExpectedPeriod(lecture.getTotalExpectedPeriod())
            .planTimeInWeekday(lecture.getPlanTimeInWeekday())
            .planTimeInWeekend(lecture.getPlanTimeInWeekend())
            .startDate(lecture.getStartDate())
            .isTermination(lecture.isTermination())
            .realEndDate(lecture.getRealEndDate())
            .memberId(lecture.getMember().getId())
            .teacherName(lecture.getTeacherName())
            .totalRuntime(lecture.getTotalRuntime())
            .build();
    }

    public static FindStudyResponseDto ofReading(Reading reading) {
        return FindStudyResponseDto.builder()
            .title(reading.getTitle())
            .description(reading.getDescription())
            .studyType(toEnum(reading.getStudyType()))
            .totalExpectedPeriod(reading.getTotalExpectedPeriod())
            .planTimeInWeekday(reading.getPlanTimeInWeekday())
            .planTimeInWeekend(reading.getPlanTimeInWeekend())
            .startDate(reading.getStartDate())
            .isTermination(reading.isTermination())
            .realEndDate(reading.getRealEndDate())
            .memberId(reading.getMember().getId())
            .totalPage(reading.getTotalPage())
            .readPagePerMin(reading.getReadPagePerMin())
            .authorName(reading.getAuthorName())
            .build();
    }
}
