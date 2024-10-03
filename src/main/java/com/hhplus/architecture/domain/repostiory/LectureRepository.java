package com.hhplus.architecture.domain.repostiory;

import com.hhplus.architecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByLectureDate(String lectureDate);
}
