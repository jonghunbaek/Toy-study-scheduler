package toyproject.studyscheduler.domain.study;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.user.User;


@NoArgsConstructor
@Getter
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int totalExpectedTime;

    private int completeTime;

    private int planTimeInWeekDay;

    private int planTimeInWeekend;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
