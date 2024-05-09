package toyproject.studyscheduler.study.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.StudyType;

import java.time.LocalDate;

@DiscriminatorValue("lecture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Lecture extends Study {

    private String teacherName;

    private int totalRuntime;

    @Builder
    private Lecture(StudyInformation studyInformation, StudyPeriod studyPeriod, StudyPlan studyPlan, Member member,
                    String teacherName, int totalRuntime) {
        super(studyInformation, studyPeriod, studyPlan, member);
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }

    public void updateLecture(StudyInformation information, StudyPeriod period, StudyPlan plan, String teacherName, int totalRuntime) {
        super.updateStudy(information, period, plan);

        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }

    @Override
    public int calculateRemainingQuantity(int totalStudyMinutes) {
        int remaining = this.totalRuntime - totalStudyMinutes;

        return Math.max(remaining, 0);
    }

    @Override
    public boolean terminateIfSatisfiedStudyQuantity(int totalStudyMinutes, LocalDate studyDate) {
        if (totalStudyMinutes >= totalRuntime) {
            terminate(studyDate);
            return true;
        }

        return false;
    }

    @Override
    public int getTotalMinutes() {
        return this.totalRuntime;
    }

    @Override
    public StudyType getType() {
        return StudyType.LECTURE;
    }
}
