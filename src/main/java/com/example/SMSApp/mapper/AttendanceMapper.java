package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.AttendanceRequestDto;
import com.example.SMSApp.dto.response.AttendanceResponseDto;
import com.example.SMSApp.model.Attendance;

public class AttendanceMapper {


    public static AttendanceResponseDto toAttendanceDto(Attendance attendance) {
        if (attendance == null) return null;

        return AttendanceResponseDto.builder()
                .publicId(attendance.getPublicId())
                .present(attendance.getPresent())
                .studentId(attendance.getStudent() != null ? attendance.getStudent().getPublicId() : null)
                .studentName(attendance.getStudent() != null
                        ? attendance.getStudent().getPersonInfo().getFullName()
                        : null)
                .lessonId(attendance.getLesson() != null ? attendance.getLesson().getPublicId() : null)
                .lessonName(attendance.getLesson() != null ? attendance.getLesson().getName() : null)
                .build();
    }

    public static Attendance toAttendanceEntity(AttendanceRequestDto dto, Attendance existingAttendance) {
        if (dto == null) return null;

        Attendance attendance = existingAttendance != null ? existingAttendance : new Attendance();
        attendance.setPresent(dto.getPresent());

        // Note: Do not set student and lesson here.
        // These should be fetched and associated in the service layer.

        return attendance;
    }


}
