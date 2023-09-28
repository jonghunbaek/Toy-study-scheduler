package toyproject.studyscheduler.domain.techstack;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseInfoEntity;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TechStack extends BaseInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TechCategory techCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private ToyProject toyProject;

    @Builder
    private TechStack(String title, String description, TechCategory techCategory, ToyProject toyProject) {
        super(title, description);
        this.techCategory = techCategory;
        this.toyProject = toyProject;
        this.toyProject.addTechStack(this);
    }
}
