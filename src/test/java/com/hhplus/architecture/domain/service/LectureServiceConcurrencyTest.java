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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
public class LectureServiceConcurrencyTest {

    @Autowired
    private LectureService lectureService;

    @MockBean
    private LectureRepository lectureRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("한_특강에_40명_신청시_30명만_성공")
    void subLectureOverCap() throws Exception {
        //given
        final Long lectureId = 1L;

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        int threads = 40;

        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        CountDownLatch countDownLatch = new CountDownLatch(threads);

        //when
        for (int i = 0; i < threads; i++) {
            Long memberId = (long) i;
            executorService.submit(() -> {
                try {
                    lectureService.subLecture(memberId, lectureId);
                    successCount.incrementAndGet();
                } catch (IllegalArgumentException e) {
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        //then
        Thread.sleep(1000);
        assertThat(successCount.get()).isEqualTo(30);
        assertThat(failCount.get()).isEqualTo(10);
        lectureRepository.deleteAll();
    }

    @Test
    @DisplayName("특강을_5번_신청했을때_한번만_성공")
    void subLectureSameMember() throws Exception {
        //given
        final Long lectureId = 1L;

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        int threads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        CountDownLatch countDownLatch = new CountDownLatch(threads);

        //when
        for (int i = 0; i < threads; i++) {
            Long memberId = (long) i;
            executorService.submit(() -> {
                try {
                    lectureService.subLecture(memberId, lectureId);
                    successCount.incrementAndGet();
                } catch (IllegalArgumentException e) {
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        //then
        Thread.sleep(1000);
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(4);
        lectureRepository.deleteAll();
    }
}
