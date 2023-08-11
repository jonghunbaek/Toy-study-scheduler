package toyproject.studyscheduler.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.study.Study;

import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    // todo : jpa 상속관계 공부 후 추가하자.
    //private List<Study> studies;

    @Enumerated
    private AccountType accountType;

    private String originImage;
    private String storedImage;
}
