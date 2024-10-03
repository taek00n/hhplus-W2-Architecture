package com.hhplus.architecture.domain.repostiory;

import com.hhplus.architecture.domain.Lecture;
import com.hhplus.architecture.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT COUNT(*) FROM Member M WHERE M.lecture  = :lecture")
    int countMembersByLectureId(@Param("lecture") Lecture lecture);
}
