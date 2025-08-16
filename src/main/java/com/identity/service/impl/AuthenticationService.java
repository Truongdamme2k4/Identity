package com.identity.service.impl;

import com.identity.dto.request.AuthenticationRequest;
import com.identity.dto.request.IntrospectRequest;
import com.identity.dto.response.AuthenticationResponse;
import com.identity.dto.response.IntrospectResponse;
import com.identity.entity.UserEntity;
import com.identity.exception.AppException;
import com.identity.exception.ErrorCode;
import com.identity.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && expirationTime.after(new Date()))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthenticationResponse.builder()
                .token(generateToken(user.getUsername()))
                .authenticated(true)
                .build();
    }

    private String generateToken(String username){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("identity-service")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("userId", "Custom")
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        }catch (JOSEException e){
            throw new RuntimeException(e);
        }
    }

}
