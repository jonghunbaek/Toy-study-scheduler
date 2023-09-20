package toyproject.studyscheduler.domain.techstack;

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
    private TechCategory techCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private ToyProject toyProject;

    @Builder
    private TechStack(String title, TechCategory techCategory, ToyProject toyProject) {
        this.title = title;
        this.techCategory = techCategory;
        this.toyProject = toyProject;
        this.toyProject.addTechStack(this);
    }
}
