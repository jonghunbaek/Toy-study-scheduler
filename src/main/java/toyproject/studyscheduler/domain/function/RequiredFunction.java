package toyproject.studyscheduler.domain.function;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseInfoEntity;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RequiredFunction extends BaseInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int expectedTime;

    @Enumerated
    private FunctionType functionType;

    @ManyToOne(fetch = FetchType.LAZY)
    ToyProject toyProject;

    @Builder
    private RequiredFunction(String title, String description, int expectedTime, FunctionType functionType, ToyProject toyProject) {
        super(title, description);
        this.expectedTime = expectedTime;
        this.functionType = functionType;
        this.toyProject = toyProject;
        this.toyProject.addRequiredFunction(this);
    }
}
