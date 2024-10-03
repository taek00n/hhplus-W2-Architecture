package com.hhplus.architecture.domain.repostiory;

import com.hhplus.architecture.domain.Lecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Test
    @DisplayName("강의_저장")
    void saveLecture() {
        // given
        Lecture lecture = new Lecture("TDD", "20241001");
        //when
        Lecture saveLecture = lectureRepository.save(lecture);
        Optional<Lecture> findLecture = lectureRepository.findById(saveLecture.getLectureId());
        //then
        assertEquals(findLecture.get().getLectureId(), saveLecture.getLectureId());
    }

    @Test
    @DisplayName("특정날짜의특강조회")
    void findByLectureDate() {
        Lecture lecture1 = new Lecture("TDD", "20241010");
        Lecture lecture2 = new Lecture("Arch", "20241010");

        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        List<Lecture> byLectureDate = lectureRepository.findByLectureDate("20241010");

        assertEquals(2, byLectureDate.size());
    }
}