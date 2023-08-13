package toyproject.studyscheduler.domain.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StudyRepositoryTest {

    @Autowired
    StudyRepository studyRepository;
    
    @DisplayName("수강중인 강의 저장 테스트")
    @Test
    void test() {
        // given
        
        // when 
        
        // then
        
     }
}