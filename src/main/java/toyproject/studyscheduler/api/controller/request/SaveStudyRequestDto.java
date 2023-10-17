package toyproject.studyscheduler.api.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;

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
    private Long memberId;

    // Reading detail
    private String authorName;
    private int totalPage;
    private int readPagePerMin;

    // Lecture detail
    private String teacherName;
    private int totalRuntime;

    public Lecture toLectureEntity(Member member) {
        return Lecture.builder()
            .title(title)
            .description(description)
            .totalExpectedPeriod(totalExpectedPeriod)
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .member(member)
            .teacherName(teacherName)
            .totalRuntime(totalRuntime)
            .build();
    }

    public Reading toReadingEntity(Member member) {
        return Reading.builder()
            .title(title)
            .description(description)
            .totalExpectedPeriod(totalExpectedPeriod)
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .member(member)
            .authorName(authorName)
            .totalPage(totalPage)
            .readPagePerMin(readPagePerMin)
            .build();
    }

    public ToyProject toToyProjectEntity(Member member) {
        return ToyProject.builder()
            .title(title)
            .description(description)
            .totalExpectedPeriod(totalExpectedPeriod)
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .member(member)
            .build();
    }
}
