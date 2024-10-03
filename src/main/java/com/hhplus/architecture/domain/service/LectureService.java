package com.hhplus.architecture.domain.service;

import com.hhplus.architecture.domain.Lecture;
import com.hhplus.architecture.domain.Member;
import com.hhplus.architecture.domain.repostiory.LectureRepository;
import com.hhplus.architecture.domain.repostiory.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
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

    /**
     * 특정 일자로 특강 목록 조회
     * @param date
     * @return 해당 일자의 특강 목록
     *
     * 목록의 특강들중 정원이 다찬 특강은 조회되지 않게 한다.
     */
    public List<Lecture> findByLectureDate(String date) {

        List<Lecture> lectureList = lectureRepository.findByLectureDate(date);

        Iterator<Lecture> iterator = lectureList.iterator();
        while (iterator.hasNext()) {
            Lecture lecture = iterator.next();
            if (lecture.getCapacity() <= 0) {
                iterator.remove();
            }
        }

        if (lectureList.isEmpty()) {
            throw new IllegalArgumentException("해당 일자에 특강이 없습니다.");
        }

        return lectureList;
    }

    /**
     * 특강 id로 특강 정보 조회
     * @param lectureId
     * @return 조회한 특강
     *
     * 해당 특강의 정원이 다 찼으면 조회되지 않게 한다.
     */
    public Lecture findByLectureId(Long lectureId) {

        Lecture lecture = lectureRepository.findByLectureId(lectureId);
        if (lecture == null) {
            throw new IllegalArgumentException("해당 정보의 특강이 없습니다.");
        }

        if (lecture.getCapacity() <= 0) {
            throw new IllegalArgumentException("해당 특강은 인원이 다 찼습니다.");
        }

        return lecture;
    }

    /**
     * 특강 신청
     * @param memberId
     * @param lectureId
     * @return 성공한 Member 객체
     *
     * 동일한 신청자는 한 번의 수강 신청만 할 수 있음
     * 정원이 초과되면 마감
     */
    @Transactional
    public void subLecture(Long memberId, Long lectureId) {

        Lecture lecture = lectureRepository.findByLectureId(lectureId);
        System.out.println("service lecture >> " + lecture);

        if (lecture.getCapacity() <= 0) {
            throw new IllegalArgumentException("해당 특강은 인원이 다 찼습니다.");
        }

        lecture.reduceCapacity();
        Member member = new Member(memberId, lecture);
        System.out.println("service member >> " + member.toString());
        memberRepository.save(member);
    }

    /**
     * 사용자의 해당 특강 신청 여부
     * @param memberId
     * @param lecture
     * @return 신청했으면 ture / 안했으면 fasle
     */
    public boolean checkDuplication(Long memberId, Lecture lecture) {

        int cntMember = memberRepository.findMemberByLectureIdAndMemberId(lecture, memberId);
        if (cntMember > 0) {
            return true;
        }

        return false;
    }

    /**
     * 신청자의 신청 완료된 특강 목록을 가져온다
     * @param memberId
     * @return 신청 성공한 Lecture List
     */
    public List<Lecture> getSubLecturesByMemberId(Long memberId) {

        List<Lecture> lecturesByMemberId = memberRepository.findLecturesByMemberId(memberId);
        if (lecturesByMemberId.size() == 0) {
            throw new IllegalArgumentException("신청 성공한 특강이 없습니다.");
        }

        return lecturesByMemberId;
    }
}
