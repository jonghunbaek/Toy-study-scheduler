package toyproject.studyscheduler.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.common.domain.BaseEntity;
import toyproject.studyscheduler.member.entity.domain.AccountType;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated
    private AccountType accountType;

    @Builder
    private Member(String name, String email, String password, AccountType accountType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public boolean isMatching(String password) {
        return this.password.equals(password);
    }
}
