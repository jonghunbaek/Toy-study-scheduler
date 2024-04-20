package toyproject.studyscheduler.study.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;

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

    @Override
    public int getTotalMinutes() {
        return this.totalRuntime;
    }

    public void updateLecture(StudyInformation information, StudyPeriod period, StudyPlan plan, String teacherName, int totalRuntime) {
        super.updateStudy(information, period, plan);

        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }

    @Override
    public boolean terminateIfSatisfiedStudyQuantity(int totalMinutes, LocalDate studyDate) {
        if (totalMinutes >= totalRuntime) {
            terminate(studyDate);
            return true;
        }

        return false;
    }
}
