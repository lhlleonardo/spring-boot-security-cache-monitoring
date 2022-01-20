package com.br.leonardo.forum.security;

import com.br.leonardo.forum.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {

        Usuario usuario = (Usuario) authentication.getPrincipal();

        Date date = new Date();

        Date dataExperiracao = new Date(date.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("Leonardo")
                .setSubject(usuario.getId().toString())
                .setIssuedAt(date)
                .setExpiration(dataExperiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

}
