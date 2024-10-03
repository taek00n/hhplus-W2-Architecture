package com.hhplus.architecture.domain.service;

import com.hhplus.architecture.domain.Lecture;
import com.hhplus.architecture.domain.Member;
import com.hhplus.architecture.domain.repostiory.LectureRepository;
import com.hhplus.architecture.domain.repostiory.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LectureServiceTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        lectureRepository.deleteAll();
    }

    @Test
    @DisplayName("해당_일자에_존재하는_강의_리스트_조회")
    void lectureListByDate() {
        //given
        Lecture lecture = new Lecture("Tdd", "20241010");
        Lecture lecture1 = new Lecture("Tdd", "20241011");
        Lecture lecture2 = new Lecture("Architecture", "20241010");
        lectureRepository.save(lecture);
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);
        //when
        List<Lecture> byLectureDate = lectureService.findByLectureDate("20241010");
        //then
        assertEquals(2, byLectureDate.size());
    }

    @Test
    @DisplayName("해당_일자에_강의가_존재하지_않을떄")
    void noneLectureListByDate() {
        //given
        String errorMsg = "";
        Lecture lecture = new Lecture("Tdd", "20241010");
        Lecture lecture1 = new Lecture("Tdd", "20241011");
        Lecture lecture2 = new Lecture("Architecture", "20241010");
        lectureRepository.save(lecture);
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);
        //when
        try {
            lectureService.findByLectureDate("20241016");
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        //then
        assertEquals("해당 일자에 강의가 없습니다.", errorMsg);
    }

    @Test
    @DisplayName("해당_번호의_강의가_존재할_떄")
    void lectureById() {
        //given
        Lecture lecture = new Lecture("Tdd", "20241010");
        Lecture saveLecture = lectureRepository.save(lecture);
        //when
        Lecture byLectureId = lectureService.findByLectureId(saveLecture.getLectureId());
        //then
        assertEquals(byLectureId.getLectureId(), saveLecture.getLectureId());
    }

    @Test
    @DisplayName("해당_번호의_강의가_존재하지_않을떄")
    void noneLectureById() {
        //given
        String errorMsg = "";
        Lecture lecture = new Lecture("Tdd", "20241010");
        lectureRepository.save(lecture);
        //when
        try {
            lectureService.findByLectureId(24L);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        //then
        assertEquals("해당 정보의 강의가 없습니다.", errorMsg);
    }

    @Test
    @DisplayName("강의_신청")
    void subLecture() {
        //given
        Lecture lecture = new Lecture("Tdd", "20241010");
        Lecture saveLecture = lectureRepository.save(lecture);
        Member member = new Member(1L, saveLecture);
        int beforeSave = memberRepository.countMembersByLectureId(lecture);
        //when
        if (beforeSave < lecture.getCapacity()) {
            memberRepository.save(member);
        }
        int afterSave = memberRepository.countMembersByLectureId(lecture);
        //then
        assertEquals(1, afterSave);
    }
}