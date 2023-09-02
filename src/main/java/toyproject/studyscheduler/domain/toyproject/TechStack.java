package toyproject.studyscheduler.domain.toyproject;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    private ToyProject toyProject;

    @Builder
    private TechStack(String title, String category, ToyProject toyProject) {
        this.title = title;
        this.category = category;
        this.toyProject = toyProject;
    }
}
