package com.nhnacademy.front.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "아이디를 입력해주세요.")
        @Pattern(regexp = ".*[A-Za-z].*", message = "아이디에 영문자가 포함되어야 합니다.")
        @Pattern(regexp = ".*\\d.*", message = "아이디에 숫자가 포함되어야 합니다.")
        @Size(max = 50, min = 8, message = "아이디는 8자 이상 50자 이하이어야 합니다.")
        String id,

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        @Size(max = 100, message = "이메일은 100자 이하이어야 합니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = ".*[A-Za-z].*", message = "비밀번호에 영문자가 포함되어야 합니다.")
        @Pattern(regexp = ".*\\d.*", message = "비밀번호에 숫자가 포함되어야 합니다.")
        @Size(max = 255, min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
        String password
) {}
