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

import java.util.List;
import java.util.Random;

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
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("해당_일자에_존재하는_특강_리스트_조회")
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
    @DisplayName("해당_일자에_특강이_존재하지_않을떄")
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
        assertEquals("해당 일자에 특강이 없습니다.", errorMsg);
    }

    @Test
    @DisplayName("해당_일자의_특강중_정원이_꽉찬_특강은_조회_안됨")
    void overCapLectureByDate() {
        //given
        Lecture lecture = new Lecture("Tdd", "20241010");
        Lecture lecture1 = new Lecture("QNA", "20241010");
        Lecture lecture2 = new Lecture("Architecture", "20241010");
        lectureRepository.save(lecture);
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);
        for (int i = 0; i < 30; i++) {
            Long memberId = (long) i;
            lectureService.subLecture(memberId, lecture.getLectureId());
        }
        //when
        List<Lecture> byLectureDate = lectureService.findByLectureDate("20241010");
        //then
        assertEquals(2, byLectureDate.size());
    }

    @Test
    @DisplayName("해당_번호의_특강이_존재할_떄")
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
    @DisplayName("해당_번호의_특강이_존재하지_않을떄")
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
        assertEquals("해당 정보의 특강이 없습니다.", errorMsg);
    }

    @Test
    @DisplayName("해당_번호의_특강이_정원이_다_찼을떄")
    void overCapLectureById() {
        //given
        String errorMsg = "";
        Lecture lecture = new Lecture("Tdd", "20241010");
        Lecture saveLecture = lectureRepository.save(lecture);
        for (int i = 0; i < 30; i++) {
            Long memberId = (long) i;
            lectureService.subLecture(memberId, lecture.getLectureId());
        }
        //when
        try {
            lectureService.findByLectureId(saveLecture.getLectureId());
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        //then
        assertEquals(errorMsg, "해당 특강은 인원이 다 찼습니다.");
    }

    @Test
    @DisplayName("신청자의_해당특강_신청여부_확인")
    void checkDuplication() {
        //given
        Lecture lecture = lectureRepository.save(new Lecture("Tdd", "20241010"));
        Member member = memberRepository.save(new Member(1L, lecture));
        //when
        boolean chk = lectureService.checkDuplication(member.getMemberId(), lecture);
        //given
        assertEquals(true, chk);
    }

    @Test
    @DisplayName("특강_신청")
    void subLecture() {
        //given
        Lecture lecture = lectureRepository.save(new Lecture("Tdd", "20241010"));
        Member member = new Member(1L, lecture);
        //when
        lectureService.subLecture(member.getMemberId(), lecture.getLectureId());
        int countMembersByLectureId = memberRepository.countMembersByLectureId(lecture);
        //then
        assertEquals(1, countMembersByLectureId);
    }

    @Test
    @DisplayName("같은회원의_중복_신청")
    void dupSubLecture() {
        //given
        String errorMsg = "";
        Lecture lecture = lectureRepository.save(new Lecture("Tdd", "20241010"));
        lectureService.subLecture(1L, lecture.getLectureId());
        //when
        try {
            lectureService.subLecture(1L, lecture.getLectureId());
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        //then
        assertEquals("이미 신청한 특강입니다.", errorMsg);
    }

    @Test
    @DisplayName("30명_이후_특강_신청")
    void subLectureOverCapacity() {
        //given
        String errorMsg = "";
        Lecture lecture = lectureRepository.save(new Lecture("Tdd", "20241010"));
        for (int i = 0; i < 30; i++) {
            Long memberId = (long) i;
            lectureService.subLecture(memberId, lecture.getLectureId());
        }
        //when
        try {
            lectureService.subLecture(50L, lecture.getLectureId());
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        //then
        assertEquals("해당 특강은 인원이 다 찼습니다.", errorMsg);
    }

    @Test
    @DisplayName("신청자가_신청_성공한_특강이_없을떄_목록_조회")
    void noneSubLectureByMemberId() {
        //given
        String errorMsg = "";
        Member member = new Member(1L, null);
        Member saveMember = memberRepository.save(member);
        System.out.println("saveMember : " + saveMember.toString());
        //when
        try {
            lectureService.getSubLecturesByMemberId(saveMember.getMemberId());
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        //then
        assertEquals("신청 성공한 특강이 없습니다.", errorMsg);
    }

    @Test
    @DisplayName("신청자가_신청_성공한_특강의_목록_조회")
    void getSubLectureByMemberId() {
        //given
        Lecture lecture = lectureRepository.save(new Lecture("Tdd", "20241010"));
        Lecture lecture1 = lectureRepository.save(new Lecture("Tdd", "20241011"));
        Member member = new Member(1L, lecture);
        Member member1 = new Member(1L, lecture1);
        Member saveMember = memberRepository.save(member);
        Member saveMember1 = memberRepository.save(member1);
        //when
        List<Lecture> subLecturesByMemberId = lectureService.getSubLecturesByMemberId(member.getMemberId());
        //then
        assertEquals(2, subLecturesByMemberId.size());
        assertEquals(saveMember.getMemberId(), saveMember1.getMemberId());
    }
}
