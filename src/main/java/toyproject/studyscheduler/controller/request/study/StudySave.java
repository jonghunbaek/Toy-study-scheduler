package toyproject.studyscheduler.controller.request.study;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import toyproject.studyscheduler.controller.request.SaveRequiredFunctionDto;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.StudyType;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudySave {

    private String title;
    private String description;
    private StudyType studyType;
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

    // ToyProject detail
    @Builder.Default
    private List<SaveRequiredFunctionDto> functions = new ArrayList<>();

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
        List<RequiredFunction> functions = toRequiredFunctionEntity();

        int totalExpectedMin = functions.stream()
            .mapToInt(RequiredFunction::getExpectedTime)
            .sum();

        return ToyProject.builder()
            .title(title)
            .description(description)
            .totalExpectedPeriod(totalExpectedPeriod)
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .member(member)
            .totalExpectedMin(totalExpectedMin)
            .functions(functions)
            .build();
    }

    private List<RequiredFunction> toRequiredFunctionEntity() {
        return functions.stream()
            .map(function -> RequiredFunction.builder()
                .title(function.getTitle())
                .description(function.getDescription())
                .functionType(function.getFunctionType())
                .expectedTime(function.getExpectedTime())
                .build())
            .toList();
    }
}
