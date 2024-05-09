package toyproject.studyscheduler.study.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.StudyType;

import java.time.LocalDate;

@DiscriminatorValue("reading")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reading extends Study {

    private String authorName;
    private int totalPage;
    private int readPagePerMin;

    @Builder
    private Reading(StudyInformation studyInformation, StudyPeriod studyPeriod, StudyPlan studyPlan, Member member,
                    String authorName, int totalPage, int readPagePerMin) {
        super(studyInformation, studyPeriod, studyPlan, member);
        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }

    public void updateReading(StudyInformation information, StudyPeriod period, StudyPlan plan, String authorName, int totalPage, int readPagePerMin) {
        super.updateStudy(information, period, plan);

        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }

    @Override
    public int calculateRemainingQuantity(int totalStudyMinutes) {
        int remaining = this.totalPage - (totalStudyMinutes * readPagePerMin);

        return Math.max(remaining, 0);
    }

    @Override
    public boolean terminateIfSatisfiedStudyQuantity(int totalStudyMinutes, LocalDate studyDate) {
        int pagesRead = readPagePerMin * totalStudyMinutes;

        if (pagesRead >= totalPage) {
            terminate(studyDate);
            return true;
        }

        return false;
    }

    @Override
    public int getTotalMinutes() {
        return (int) Math.ceil(((double) totalPage / readPagePerMin));
    }

    @Override
    public StudyType getType() {
        return StudyType.READING;
    }
}
