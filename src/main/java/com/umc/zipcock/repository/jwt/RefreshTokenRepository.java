package com.umc.zipcock.repository.jwt;

import com.umc.zipcock.model.entity.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    List<RefreshToken> findByKey(Long key);
}
