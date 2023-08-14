package toyproject.studyscheduler.domain.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StudyRepositoryTest {

    @Autowired
    StudyRepository studyRepository;



    @DisplayName("주어진 기간에 속한 학습내용을 모두 조회 한다.")
    @Test
    void test() {
        // given
        
        // when 
        
        // then
        
     }
}