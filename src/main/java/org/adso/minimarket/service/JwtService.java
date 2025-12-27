package org.adso.minimarket.service;

public interface JwtService {
    String generateToken(String email);
}
