package com.hhplus.architecture.domain.repostiory;

import com.hhplus.architecture.domain.Lecture;
import com.hhplus.architecture.domain.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {


    @Query(value = "SELECT COUNT(*) FROM Member M WHERE M.lecture  = :lecture")
    int countMembersByLectureId(@Param("lecture") Lecture lecture);

    @Query(value = "SELECT COUNT(*) FROM Member M WHERE M.lecture = :lecture AND M.memberId = :memberId")
    int findMemberByLectureIdAndMemberId(@Param("lecture") Lecture lecture, @Param("memberId") Long memberId);

    @Query(value = "SELECT M.lecture FROM Member M, Lecture L WHERE M.lecture.lectureId = L.lectureId AND M.memberId = :memberId")
    List<Lecture> findLecturesByMemberId(@Param("memberId") Long memberId);

    Member save(Member member);
}
