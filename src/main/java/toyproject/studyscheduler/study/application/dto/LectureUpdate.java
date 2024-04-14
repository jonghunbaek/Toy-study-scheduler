package toyproject.studyscheduler.study.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.entity.Lecture;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor
public class LectureUpdate extends StudyUpdate {

    private String teacherName;

    @Max(message = "총 재생 시간은 최대 6,000분 이하여야 합니다.", value = 6000)
    @Min(message = "총 재생 시간은 최소 1분 이상이어야 합니다.", value = 1)
    private int totalRuntime;

    @Builder
    private LectureUpdate(String studyType, Long studyId, String title, String description, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend, String teacherName, int totalRuntime) {
        super(studyType, title, description, startDate, endDate, planMinutesInWeekday, planMinutesInWeekend);
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }

    @Override
    protected void updateStudy(Study study, StudyInformation information, StudyPeriod period, StudyPlan plan) {
        Lecture lecture = (Lecture) study;

        lecture.updateLecture(information, period, plan, teacherName, totalRuntime);
    }
}
