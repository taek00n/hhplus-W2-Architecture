package com.hhplus.architecture.api.controller;

import com.hhplus.architecture.domain.Lecture;
import com.hhplus.architecture.domain.Member;
import com.hhplus.architecture.domain.repostiory.LectureRepository;
import com.hhplus.architecture.domain.repostiory.MemberRepository;
import com.hhplus.architecture.domain.service.LectureService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(LectureSubController.class)
class LectureSubControllerTest {

    @MockBean
    private LectureService lectureService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private MemberRepository memberRepository;


}