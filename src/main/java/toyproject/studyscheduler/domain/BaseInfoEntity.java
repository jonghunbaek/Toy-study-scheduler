package toyproject.studyscheduler.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor
public class BaseInfoEntity extends BaseEntity {

    private String title;

    private String description;

    public BaseInfoEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
