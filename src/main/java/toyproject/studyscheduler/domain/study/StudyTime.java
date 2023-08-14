package toyproject.studyscheduler.domain.study;

import jakarta.persistence.*;
import toyproject.studyscheduler.domain.BaseEntity;

@Entity
public class StudyTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalCompleteTime;

    private int completeTimePerDay;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;
}
