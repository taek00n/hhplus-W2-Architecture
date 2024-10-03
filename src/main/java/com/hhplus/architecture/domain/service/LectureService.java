package com.hhplus.architecture.domain.service;

import com.hhplus.architecture.api.dto.RequestDTO;
import com.hhplus.architecture.domain.Lecture;
import com.hhplus.architecture.domain.Member;
import com.hhplus.architecture.domain.repostiory.LectureRepository;
import com.hhplus.architecture.domain.repostiory.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    public LectureService(LectureRepository lectureRepository, MemberRepository memberRepository) {
        this.lectureRepository = lectureRepository;
        this.memberRepository = memberRepository;
    }

    // 일자로 강의 찾기
    public List<Lecture> findByLectureDate(String date) {

        List<Lecture> lectureList = lectureRepository.findByLectureDate(date);

        if (lectureList.isEmpty()) {
            throw new IllegalArgumentException("해당 일자에 강의가 없습니다.");
        }

        return lectureList;
    }

    // 강의 id로 강의 찾기
    public Lecture findByLectureId(Long lectureId) {
        Optional<Lecture> lectureFindById = lectureRepository.findById(lectureId);
        if (!lectureFindById.isPresent()) {
            throw new IllegalArgumentException("해당 정보의 강의가 없습니다.");
        }

        return lectureFindById.get();
    }

    // 강의 신청하기
    public Member subLecture(Long memberId, Long lectureId) {

        Lecture lecture = this.findByLectureId(lectureId);
        int currentLectureMemberCnt = getCurrentLectureMemberCnt(lecture);
        if (currentLectureMemberCnt == lecture.getCapacity()) {
            return null; // 정원초과
        }
        Member member = new Member(memberId, lecture);
        Member saveMember = memberRepository.save(member);

        return saveMember;
    }

    // 강의의 현재수강생 수 가져오기
    private int getCurrentLectureMemberCnt(Lecture lecture) {
        return memberRepository.countMembersByLectureId(lecture);
    }
}
