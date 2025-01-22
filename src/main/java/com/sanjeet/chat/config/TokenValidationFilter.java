package com.sanjeet.chat.config;


import com.sanjeet.chat.model.entity.AdminRegistrationEntity;
import com.sanjeet.chat.model.entity.ClientRegistrationEntity;
import com.sanjeet.chat.model.entity.UserDetailsEntity;
import com.sanjeet.chat.repository.AdminRepository;
import com.sanjeet.chat.repository.ClientRepository;
import com.sanjeet.chat.repository.UserRepository;
import com.sanjeet.chat.service.JwtService;
import com.sanjeet.chat.utils.Constant;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public TokenValidationFilter(JwtService jwtService,AdminRepository adminRepository,UserRepository userRepository, ClientRepository clientRepository) {
        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("Request URI: " + requestURI);

        // Skip token validation for public URLs
        if (isPublicUrl(requestURI)) {
            System.out.println("Public URL accessed, skipping validation: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String apiKey = request.getHeader("API_KEY");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            // Validate token and extract claims
            var claims = jwtService.validateTokenAndGetClaims(token, apiKey);
            String role = claims.get(Constant.ROLE, String.class); // Extract role (CLIENT, USER, or ADMIN)

            // Role-specific validation logic
            if (Constant.ADMIN.equals(role)) {
                // For ADMIN, only validate the session token (skip API key validation)
                validateAdminToken(token, claims, response);
            } else if (Constant.CLIENT.equals(role) || Constant.USER.equals(role)) {
                // For CLIENT and USER, validate both API key and session token
                validateClientOrUserToken(token, apiKey, claims, response,role);
            } else {
                // Unknown role, unauthorized access
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or missing role");
                return;
            }

            // Set authentication in the security context
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            apiKey, // Use API Key or token as the principal
                            null,   // No credentials
                            jwtService.getAuthorities(role) // Authorities based on role
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token validation failed: " + e.getMessage());
            return;
        }

        // Proceed with the request if the token is valid
        filterChain.doFilter(request, response);
    }

    private void validateAdminToken(String token, Claims claims, HttpServletResponse response) throws IOException {
        // Extract the session token from the Authorization header (this is the `token` parameter itself)
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing Authorization token");
            throw new IllegalArgumentException("Invalid token in Authorization header");
        }

        // Extract session token from claims (optional, as a secondary check)
        String sessionTokenFromClaims = claims.get(Constant.CLAIM_SESSION_TOKEN, String.class);
        if (sessionTokenFromClaims == null || sessionTokenFromClaims.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing session token in claims");
            throw new IllegalArgumentException("Invalid session token in claims");
        }

        // Fetch the admin record from the database
        String email = claims.getSubject();
        AdminRegistrationEntity admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        // Compare the session token stored in the database with the actual token received
        String storedSessionToken = admin.getSessionToken();
        if (!token.equals(storedSessionToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization token mismatch for ADMIN");
            throw new IllegalArgumentException("Invalid token");
        }

        // Additional admin-specific validation logic can be added here
        System.out.println("Admin session token validated successfully");
    }

    private void validateClientOrUserToken(String token, String apiKey, Claims claims, HttpServletResponse response, String role) throws IOException {

        // Extract the session token from the Authorization header (this is the `token` parameter itself)
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing Authorization token");
            throw new IllegalArgumentException("Invalid token in Authorization header");
        }

        String tokenApiKey = claims.get(Constant.API_KEY, String.class); // Extract API Key from claims
        String sessionTokenFromClaims = claims.get(Constant.CLAIM_SESSION_TOKEN, String.class);

        if (sessionTokenFromClaims == null || sessionTokenFromClaims.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid session token for CLIENT/USER");
            throw new IllegalArgumentException("Invalid session token");
        }

        if (!apiKey.equals(tokenApiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key or mismatched token for CLIENT/USER");
            throw new IllegalArgumentException("API Key mismatch");
        }

        if (role.equals(Constant.CLIENT)){
            // Fetch the admin record from the database
            String id = claims.getSubject();
            ClientRegistrationEntity client = clientRepository.findById(Long.valueOf(id))
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));
            // Compare the session token stored in the database with the actual token received
            String storedSessionToken = client.getSessionToken();
            if (!token.equals(storedSessionToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authorization token mismatch for ADMIN");
                throw new IllegalArgumentException("Invalid token");
            }

        }else if (role.equals(Constant.USER)){
            // Fetch the admin record from the database
            String id = claims.getSubject();
            UserDetailsEntity user = userRepository.findById(Long.valueOf(id))
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
            // Compare the session token stored in the database with the actual token received
            String storedSessionToken = user.getUserSessionToken();
            if (!token.equals(storedSessionToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authorization token mismatch for ADMIN");
                throw new IllegalArgumentException("Invalid token");
            }

        }


        System.out.println("Client/USER API Key and session token validated successfully");
    }

    private boolean isPublicUrl(String requestURI) {
        return Arrays.stream(Constant.PUBLIC_URLS).anyMatch(requestURI::startsWith);
    }
}

