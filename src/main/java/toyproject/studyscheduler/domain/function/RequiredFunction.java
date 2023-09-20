package toyproject.studyscheduler.domain.function;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RequiredFunction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated
    private FunctionType functionType;

    @ManyToOne(fetch = FetchType.LAZY)
    ToyProject toyProject;

    @Builder
    private RequiredFunction(String title, String description, FunctionType functionType, ToyProject toyProject) {
        this.title = title;
        this.description = description;
        this.functionType = functionType;
        this.toyProject = toyProject;
        this.toyProject.addRequiredFunction(this);
    }
}
