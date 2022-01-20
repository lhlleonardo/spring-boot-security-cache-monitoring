package com.br.leonardo.forum.controller;

import com.br.leonardo.forum.dto.TokenDto;
import com.br.leonardo.forum.form.LoginForm;
import com.br.leonardo.forum.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm loginForm){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginForm.converter();

        try {
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            String token = tokenService.gerarToken(authentication);

            return ResponseEntity.ok(new TokenDto("Bearer " + token, "Bearer"));
        } catch (AuthenticationException e) {

            return ResponseEntity.badRequest().build();
        }

    }
}
