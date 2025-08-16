package com.identity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    String token;
    boolean authenticated;
}
