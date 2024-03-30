package toyproject.studyscheduler.study.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.study.entity.domain.StudyType;
import toyproject.studyscheduler.study.entity.Lecture;
import toyproject.studyscheduler.study.entity.Reading;

import java.time.LocalDate;

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

    public Lecture toLectureEntity(Member member) {
        return null;
    }

    public Reading toReadingEntity(Member member) {
        return null;
    }
}
