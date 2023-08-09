package toyproject.studyscheduler.domain.user;

public enum AccountType {

    PAUSE("계정 휴면"),
    ACTIVE("계정 활동"),
    BLOCK("계정 정지");

    private String description;

    AccountType(String description) {

    }
}
