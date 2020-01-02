package com.ecnu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @date 2019/12/21 -9:37 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableTimeRequest {
    @NotEmpty
    private String date;
    @NotEmpty
    private String gymId;
}
