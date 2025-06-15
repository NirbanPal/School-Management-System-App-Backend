package com.example.SMSApp.dto.response;


import lombok.*;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortDetailsListResponseDto {

    private UUID publicId;
    private String name;
    private String officialId;
}
