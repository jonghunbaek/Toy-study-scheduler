package toyproject.studyscheduler.study.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.study.entity.domain.StudyTimeSchedule;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorColumn(name = "study_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Embedded
    private StudyTimeSchedule studyTimeSchedule;

    private boolean isTermination;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    protected Study(String title, String description, boolean isTermination, Member member,
                    int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, LocalDate planEndDate, LocalDate realEndDate) {
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
        this.member = member;
        if (isTermination) {
            this.studyTimeSchedule = StudyTimeSchedule.fromTerminatedStudy(planTimeInWeekday,planTimeInWeekend, startDate, realEndDate);
            return;
        }

        this.studyTimeSchedule = StudyTimeSchedule.fromStartingStudy(planTimeInWeekday,planTimeInWeekend, startDate, planEndDate);
    }

    protected abstract LocalDate calculateExpectedEndDate();

    public void terminateStudyIn(LocalDate realEndDate) {
        this.isTermination = true;
//        this.realEndDate = realEndDate;
    }
}
