package toyproject.studyscheduler.study.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.study.entity.domain.StudyInformation;
import toyproject.studyscheduler.study.entity.domain.StudyPeriod;

@DiscriminatorValue("reading")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reading extends Study {

    private String authorName;
    private int totalPage;
    private int readPagePerMin;

    @Builder
    private Reading(StudyInformation studyInformation, StudyPeriod studyPeriod, Member member, String authorName, int totalPage, int readPagePerMin) {
        super(studyInformation, studyPeriod, member);
        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }
}
