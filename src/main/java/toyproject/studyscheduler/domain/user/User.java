package toyproject.studyscheduler.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Enumerated
    private AccountType accountType;

    private String originProfileImage;
    private String storedProfileImage;

    @Builder
    private User(String name, String email, String password, AccountType accountType, String originProfileImage, String storedProfileImage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.originProfileImage = originProfileImage;
        this.storedProfileImage = storedProfileImage;
    }
}
