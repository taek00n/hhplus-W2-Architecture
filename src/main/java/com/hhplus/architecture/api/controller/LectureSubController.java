package com.hhplus.architecture.api.controller;

import com.hhplus.architecture.api.dto.RequestDTO;
import com.hhplus.architecture.api.dto.ResponseDTO;
import com.hhplus.architecture.domain.Lecture;
import com.hhplus.architecture.domain.Member;
import com.hhplus.architecture.domain.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lecture")
public class LectureSubController {

    private final LectureService lectureService;

    public LectureSubController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public List<ResponseDTO> getLecturesByDate(@RequestBody RequestDTO requestDTO) {

        List<Lecture> lectures = lectureService.findByLectureDate(requestDTO.getLectureDate());
        List<ResponseDTO> responseDtoList = new ArrayList<>();

        for (Lecture lecture : lectures) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .lectureName(lecture.getLectureName())
                    .lectureDate(lecture.getLectureDate())
                    .build();

            responseDtoList.add(responseDTO);
        }

        return responseDtoList;
    }

    @PostMapping("/subLecture")
    public ResponseDTO subLecture(@RequestBody RequestDTO requestDTO) {
        Member member = lectureService.subLecture(requestDTO.getMemberId(), requestDTO.getLectureId());
        ResponseDTO responseDTO = new ResponseDTO();

        if (member != null) {
            Lecture lecture = lectureService.findByLectureId(member.getLecture().getLectureId());
            responseDTO = ResponseDTO.builder()
                    .lectureName(lecture.getLectureName())
                    .memberId(member.getMemberId())
                    .build();
        }

        return responseDTO;
    }
}
