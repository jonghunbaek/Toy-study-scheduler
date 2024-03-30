package toyproject.studyscheduler.study.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.study.entity.domain.StudyBaseInfo;
import toyproject.studyscheduler.study.entity.domain.StudyPeriod;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorColumn(name = "study_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StudyBaseInfo studyBaseInfo;

    @Embedded
    private StudyPeriod studyPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    protected Study(StudyBaseInfo studyBaseInfo, StudyPeriod studyPeriod, Member member) {
        this.studyBaseInfo = studyBaseInfo;
        this.studyPeriod = studyPeriod;
        this.member = member;
    }
}
