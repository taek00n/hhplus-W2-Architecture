package com.hhplus.architecture.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="LECTURE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Lecture {

    private final int DEFAULT_CAPACITY = 30;

    @Id @GeneratedValue
    @Column(name = "LECTURE_ID", nullable = false)
    private Long lectureId;

    @Column(name = "LECTURE_NAME")
    private String lectureName;

    @Column(name = "LECTURE_DATE")
    private String lectureDate;

    @Column(name = "CAPACITY")
    private int capacity;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    public Lecture(Long lectureId, String lectureName, String lectureDate) {
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.lectureDate = lectureDate;
        this.capacity = DEFAULT_CAPACITY;
    }

    public Lecture(String lectureName, String lectureDate) {
        this.lectureName = lectureName;
        this.lectureDate = lectureDate;
        this.capacity = DEFAULT_CAPACITY;
    }

    public void reduceCapacity() {
        this.capacity -= 1;
    }
}
