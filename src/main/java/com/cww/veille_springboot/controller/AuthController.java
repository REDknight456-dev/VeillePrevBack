package com.cww.veille_springboot.controller;

import com.cww.veille_springboot.configuration.JwtUtils;
import com.cww.veille_springboot.entity.User;
import com.cww.veille_springboot.entity.VerificationCode;
import com.cww.veille_springboot.repository.UserRepository;
import com.cww.veille_springboot.repository.VerificationCodeRepository;
import com.cww.veille_springboot.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService; // Add this line
    private final VerificationCodeRepository verificationCodeRepository; // Add this line too

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Vérification si l'email existe déjà
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        // Création d'un nouvel utilisateur avec les données validées
        User userValid = new User();
        userValid.setEmail(user.getEmail());
        userValid.setPassword(passwordEncoder.encode(user.getPassword())); // Encodage du mot de passe
        userValid.setNom(user.getNom());
        userValid.setFonction_utilisateur(user.getFonction_utilisateur());

        if (user.getRole() != null) {
            userValid.setRole(user.getRole()); // Utiliser le rôle envoyé dans la requête
        } else {
            userValid.setRole(User.Role.NORMAL); // Définir le rôle par défaut si aucun rôle n'est envoyé
        }

        // Sauvegarde de l'utilisateur dans la base de données
        User savedUser = userRepository.save(userValid);

        // Retour d'une réponse avec l'utilisateur sauvegardé
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Authentification de l'utilisateur avec l'email et le mot de passe
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            // Vérification si l'authentification a réussi
            if (authentication.isAuthenticated()) {
                // Récupération de l'utilisateur depuis la base de données
                User authenticatedUser = userRepository.findByEmail(user.getEmail());
                if (authenticatedUser == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
                }

                // Génération du token JWT
                String token = jwtUtils.generateToken(authenticatedUser.getEmail());

                // Construction de la réponse avec les informations utilisateur
                Map<String, Object> authData = new HashMap<>();
                authData.put("token", token);
                authData.put("username", authenticatedUser.getEmail());
                authData.put("roles", authenticatedUser.getRole()); // Récupération du rôle depuis l'utilisateur authentifié
                authData.put("nom", authenticatedUser.getNom());
                authData.put("id_user", authenticatedUser.getId_utilisateur());

                return ResponseEntity.ok(authData); // Retourne les données de l'authentification
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
        } catch (AuthenticationException e) {
            // Gestion de l'exception en cas d'échec d'authentification
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));

        // On ne renvoie pas le mot de passe dans la réponse
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));

        // Mise à jour uniquement des champs autorisés
        if (userDetails.getNumero_tel() != null) {
            user.setNumero_tel(userDetails.getNumero_tel());
        }
        if (userDetails.getPhoto_profil() != null) {
            user.setPhoto_profil(userDetails.getPhoto_profil());
        }
        if (userDetails.getFonction_utilisateur() != null) {
            user.setFonction_utilisateur(userDetails.getFonction_utilisateur());
        }
        if (userDetails.getNom() != null) {
            user.setNom(userDetails.getNom());
        }

        // Sauvegarde des modifications
        User updatedUser = userRepository.save(user);
        updatedUser.setPassword(null); // Ne pas renvoyer le mot de passe dans la réponse

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<?> getProfilePhoto(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));

        if (user.getPhoto_profil() == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("photo_profil", Base64.getEncoder().encodeToString(user.getPhoto_profil()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable Integer id, @RequestBody Map<String, String> passwordData) {
        // Récupérer l'utilisateur
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
        String retypePassword = passwordData.get("retypePassword");

        // Vérifier si l'ancien mot de passe est correct
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("L'ancien mot de passe est incorrect");
        }

        // Vérifier si le nouveau mot de passe et sa confirmation correspondent
        if (!newPassword.equals(retypePassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le nouveau mot de passe et sa confirmation ne correspondent pas");
        }

        // Mettre à jour le mot de passe avec le nouveau mot de passe hashé
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok().body("Mot de passe modifié avec succès");
    }
    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FACode(
            @RequestParam String email,
            @RequestParam String code) {
        VerificationCode verificationCode = verificationCodeRepository.findByEmailAndCodeAndUsedFalse(email, code);
        if (verificationCode == null) {
            return ResponseEntity.badRequest().body("Invalid code");
        }

        if (verificationCode.isExpired()) {
            return ResponseEntity.badRequest().body("Code has expired. Please request a new code");
        }

        verificationCode.setUsed(true);
        verificationCodeRepository.save(verificationCode);

        User authenticatedUser = userRepository.findByEmail(email);
        String token = jwtUtils.generateToken(email);

        Map<String, Object> authData = new HashMap<>();
        authData.put("token", token);
        authData.put("username", authenticatedUser.getEmail());
        authData.put("roles", authenticatedUser.getRole());

        return ResponseEntity.ok(authData);
    }

    @PostMapping("/send-2fa-code")
    public ResponseEntity<?> send2faCode(@RequestParam String email) throws IOException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String code = generateAndSave2FACode(email);
        mailService.send2FACode(email, code); // Use instance method instead of static

        return ResponseEntity.ok("2FA code sent to " + email);
    }

    private String generateAndSave2FACode(String email) {
        String code = generateCode();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        verificationCode.setUsed(false);

        verificationCodeRepository.save(verificationCode); // Use the injected repository

        return code;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }


}
