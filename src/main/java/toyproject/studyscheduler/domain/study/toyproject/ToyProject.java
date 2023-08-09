package toyproject.studyscheduler.domain.study.toyproject;

import jakarta.persistence.*;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.study.Study;

import java.util.List;

public class ToyProject extends BaseEntity implements Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int completeTime;

    private int planTimeInWeekDay;

    private int planTimeInWeekend;

    //todo : 이너클래스와의 차이를 생각해보자.
    @OneToMany(fetch = FetchType.LAZY)
    private List<PlannedFunction> functions;

}
