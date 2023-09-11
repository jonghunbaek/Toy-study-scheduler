package toyproject.studyscheduler.domain.study.toyproject.requiredfunction;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Enumerated
    private FunctionType functionType;

    @ManyToOne(fetch = FetchType.LAZY)
    ToyProject toyProject;


}
