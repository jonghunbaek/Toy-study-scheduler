package toyproject.studyscheduler.domain.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.user.AccountType;
import toyproject.studyscheduler.domain.user.User;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
class StudyRepositoryTest {

    @Autowired
    StudyRepository studyRepository;

    @DisplayName("주어진 기간에 수행한 학습들을 모두 조회 한다.")
    @Test
    void getStudiesByMonth() {
        // given
        User user = createUser();
        Study lecture = Lecture.builder().build();


        // when 
        
        // then
        
     }

    private static User createUser() {
        return User.builder()
                .email("hong@gmail.com")
                .password("zxcv1234")
                .name("hong")
                .accountType(AccountType.ACTIVE)
                .originProfileImage("1234")
                .storedProfileImage("4151")
                .build();
    }
}