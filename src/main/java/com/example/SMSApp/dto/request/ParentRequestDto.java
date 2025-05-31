package com.example.SMSApp.dto.request;


import com.example.SMSApp.model.enums.UserSex;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ParentRequestDto {

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must be valid"
    )
    private String email;

    @NotBlank(message = "Name is required.")
    @Size(max = 50, message = "Name must be at most 50 characters.")
    private String name;

    @NotBlank(message = "Surname is required.")
    @Size(max = 50, message = "Surname must be at most 50 characters.")
    private String surname;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits.")
    private String phone;

    @NotBlank(message = "Address is required.")
    @Size(max = 255, message = "Address must be at most 255 characters.")
    private String address;

    @NotBlank(message = "Blood type is required.")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Blood type must be A+, A-, B+, B-, AB+, AB-, O+, or O-.")
    private String bloodType;

    @NotNull(message = "Gender is required.")
    private UserSex sex;

    @NotNull(message = "Birthday is required.")
    @Past(message = "Birthday must be a past date.")
    private LocalDate birthday;

    @NotBlank(message = "Relation is required.")
    @Size(max = 20, message = "Relation must be at most 20 characters.")
    private String relation;

}
