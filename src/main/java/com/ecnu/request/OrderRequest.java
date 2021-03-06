package com.ecnu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;

/**
 * @date 2019/12/11 -11:18 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    @NotEmpty
    private String gymId;
    private String userEmail;
    @NotEmpty
    private String date;
    @Range(min = 16, max = 39)
    private Integer startTime;
    @Range(min = 17, max = 40)
    private Integer endTime;
}
