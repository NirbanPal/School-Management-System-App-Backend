package com.example.SMSApp.dto.response;


import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeResponseDto {

    private UUID publicId;
    private Integer level;
    private List<String> classNames;
}
