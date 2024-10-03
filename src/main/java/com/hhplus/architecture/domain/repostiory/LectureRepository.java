package com.hhplus.architecture.domain.repostiory;

import com.hhplus.architecture.domain.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT L FROM Lecture L WHERE L.lectureId = :lectureId")
    Lecture findByLectureId(@Param("lectureId") Long lectureId);

    List<Lecture> findByLectureDate(String lectureDate);
}
