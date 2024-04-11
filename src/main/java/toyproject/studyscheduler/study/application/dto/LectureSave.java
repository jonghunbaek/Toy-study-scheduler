package toyproject.studyscheduler.study.application.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.entity.Lecture;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class LectureSave extends StudySave {

    private String teacherName;

    @Max(message = "총 재생 시간은 최대 6,000분 이하여야 합니다.", value = 6000)
    @Min(message = "총 재생 시간은 최소 1분 이상이어야 합니다.", value = 1)
    private int totalRuntime;

    @Builder
    private LectureSave(String studyType, String title, String description, boolean isTermination,
                        LocalDate startDate, LocalDate endDate,
                        int planMinutesInWeekday, int planMinutesInWeekend,
                        String teacherName, int totalRuntime) {

        super(studyType, title, description, isTermination, startDate, endDate, planMinutesInWeekday, planMinutesInWeekend);
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }

    @Override
    protected Study createStudy(StudyInformation information, StudyPeriod period, StudyPlan plan, Member member) {
        return Lecture.builder()
            .studyInformation(information)
            .studyPeriod(period)
            .studyPlan(plan)
            .teacherName(this.teacherName)
            .totalRuntime(this.totalRuntime)
            .member(member)
            .build();
    }
}
