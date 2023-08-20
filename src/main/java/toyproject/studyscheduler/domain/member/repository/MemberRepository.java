package toyproject.studyscheduler.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.domain.member.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
