package com.hhplus.architecture.domain.repostiory;

import com.hhplus.architecture.domain.Lecture;
import com.hhplus.architecture.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Lecture lecture;

    @BeforeEach
    void setUp() {
        lecture = lectureRepository.save(new Lecture("TDD", "20241010"));
    }

    @Test
    @DisplayName("강의의_신청_인원_가져오기")
    void countMemberByLectureId() {

        Member member = new Member(1L, lecture);
        Member member1 = new Member(2L, lecture);
        memberRepository.save(member);
        memberRepository.save(member1);

        int cnt = memberRepository.countMembersByLectureId(lecture);

        assertEquals(2, cnt);
    }

}