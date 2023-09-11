package toyproject.studyscheduler.domain.study.toyproject.TechStack;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private ToyProject toyProject;

    @Builder
    private TechStack(String title, Category category, ToyProject toyProject) {
        this.title = title;
        this.category = category;
        this.toyProject = toyProject;
    }
}
