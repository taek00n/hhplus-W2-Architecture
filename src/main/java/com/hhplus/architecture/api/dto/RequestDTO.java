package com.hhplus.architecture.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDTO {

    Long memberId;
    Long lectureId;
    String lectureDate;
}
