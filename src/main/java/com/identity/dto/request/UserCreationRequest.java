package com.identity.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    LocalDate dob;
}

