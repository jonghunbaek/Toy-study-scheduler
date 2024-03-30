package toyproject.studyscheduler.study.entity;

import jakarta.persistence.*;
import lombok.*;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.study.entity.domain.StudyBaseInfo;
import toyproject.studyscheduler.study.entity.domain.StudyPeriod;

@DiscriminatorValue("lecture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Lecture extends Study {

    private String teacherName;

    private int totalRuntime;

    @Builder
    private Lecture(StudyBaseInfo studyBaseInfo, StudyPeriod studyPeriod, Member member, String teacherName, int totalRuntime) {
        super(studyBaseInfo, studyPeriod, member);
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }
}
