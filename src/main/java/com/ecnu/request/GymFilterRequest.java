package com.ecnu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @date 2019/12/12 -2:14 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GymFilterRequest {
    private String type;
    private String address;
    private Boolean highToLow;
    private Boolean openOnly;
}
