package com.sanjeet.chat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjeet.chat.repository.AdminRepository;
import com.sanjeet.chat.repository.ClientRepository;
import com.sanjeet.chat.utils.Constant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
public class JwtService {

    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private final ClientService clientService;
    private String secretKey;

    private void initializeSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(Constant.H_MAC_ALGORITHM);
            SecretKey sk = keyGen.generateKey();
            this.secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
            System.out.println("Generated secret key: " + this.secretKey); // Log for debugging
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize secret key", e);
        }
    }

    public JwtService(ClientRepository clientRepository,ClientService clientService,AdminRepository adminRepository) {
        this.clientRepository = clientRepository;
        this.clientService = clientService;
        this.adminRepository = adminRepository;
        initializeSecretKey();
    }

    //Generate Client Session Token
    public String generateClientToken(String clientId, String apiKey,String email) {
        // Fetch the secret key for the client
        String secretKey = clientRepository.findSecretKeyByApiKey(apiKey);
        if (secretKey == null) {
            throw new IllegalArgumentException("Invalid API Key");
        }
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        String claimSessionToken = UUID.randomUUID().toString(); // Generate session token
        long expirationTime = 1000 * 60 * 60 * 24; // Token valid for 24 hours
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.ROLE,Constant.CLIENT);
        claims.put(Constant.USER_NAME,email);
        claims.put(Constant.API_KEY,apiKey);
        claims.put(Constant.CLIENT_ID,clientId);
        claims.put(Constant.CLAIM_SESSION_TOKEN,claimSessionToken);
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(clientId) // Use clientId as the
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(key)
                .compact();
    }

    // Generate User Token
    public String generateUserToken(String clientId, String userId, String apiKey,String phoneNo) {
        String secretKey = clientRepository.findSecretKeyByApiKey(apiKey);
        if (secretKey == null) {
            throw new IllegalArgumentException("Invalid API Key");
        }
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        String claimSessionToken = UUID.randomUUID().toString(); // Generate session token
        long expirationTime = 1000 * 60 * 60; // 1 hour for user tokens
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.ROLE,Constant.USER);
        claims.put(Constant.USER_NAME,phoneNo);
        claims.put(Constant.CLIENT_ID,clientId);
        claims.put(Constant.API_KEY,apiKey);
        claims.put(Constant.CLAIM_SESSION_TOKEN,claimSessionToken);
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(key)
                .compact();
    }

    //Generate Client Session Token
    public String generateAdminSessionToken(String email) {
        System.out.println("Generating JWT for email: " + email);
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        // Fetch the secret key for the client
        String claimSessionToken = UUID.randomUUID().toString(); // Generate session token
        long expirationTime = 1000 * 60 * 60 * 24; // Token valid for 24 hours
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.ROLE,Constant.ADMIN);
        claims.put(Constant.USER_NAME,email);
        claims.put(Constant.CLAIM_SESSION_TOKEN,claimSessionToken);
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(getKey())
                .compact();
    }

    // Validate token and extract claims
    public Claims validateTokenAndGetClaims(String token, String apiKey) {
        String secretKey = clientRepository.findSecretKeyByApiKey(apiKey);
        if (secretKey == null) {
            throw new IllegalArgumentException("Invalid API Key");
        }

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, String apiKey) {
        System.out.println("Validating Token: " + token);
        System.out.println("Using API Key: " + apiKey);
        String secretKey = clientRepository.findSecretKeyByApiKey(apiKey);
        System.out.println("Retrieved Secret Key for API Key: " + apiKey + " is: " + secretKey);
        if (secretKey == null) {
            System.out.println("Secret Key not found for API Key: " + apiKey);
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)))
                    .build()
                    .parseSignedClaims(token);
            System.out.println("Token validation successful");
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Expired JWT token: " + e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("Malformed JWT token: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
        }
        return false;
    }

    public boolean validateApiKeyOld(String apiKey) {
        System.out.println("Validating API Key: " + apiKey);
        boolean isValid = clientRepository.validateApiKey(apiKey);
        System.out.println("API Key exists and is active: " + isValid);
        return isValid;
    }

    // Validate API Key
    public boolean validateApiKey(String apiKey) {
        return clientRepository.existsByApiKey(apiKey);
    }

    // Get Authorities based on roles
    public Collection<GrantedAuthority> getAuthorities(String role) {
        return List.of(() -> role); // Assign role as GrantedAuthority
    }

    public String extractApiKeyFromToken(String token) {
        try {
            // Step 1: Extract the API Key (subject) without validating the signature
            String apiKey = extractApiKeyFromTokenWithoutValidation(token);
            System.out.println("Extracted api key is : " + apiKey);
            // Step 2: Retrieve the clientSecret for the extracted API Key
            String clientSecret = clientRepository.findSecretKeyByApiKey(apiKey);
            if (clientSecret == null) {
                throw new IllegalArgumentException("Invalid API Key: " + apiKey);
            }
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(clientSecret)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject(); // 'sub' contains the API Key
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to extract API Key from token: " + e.getMessage());
        }
    }

    private String extractApiKeyFromTokenWithoutValidation(String token) {
        try {
            // Split the JWT into its parts (Header, Payload, Signature)
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }

            // Decode the Payload (Base64URL format)
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

            // Parse the payload JSON to extract the 'apiKey'
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode payload = objectMapper.readTree(payloadJson);

            // Extract and return the 'apiKey' field from the payload
            return payload.get("sub").asText(); // 'sub' typically contains the API Key
        } catch (Exception e) {
            System.err.println("Error parsing token without validation: 11" + e.getMessage());
            return null;
        }
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}
