package com.example.loaplan.domain.character.repository;

import com.example.loaplan.domain.character.entity.CharacterEntity;
import com.example.loaplan.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {
    Optional<CharacterEntity> findByUserAndNickname(UserEntity user, String nickname);
    List<CharacterEntity> findByUser(UserEntity user);
    List<CharacterEntity> findByUserOrderByOrderIndexAsc(UserEntity user);
    Optional<CharacterEntity> findByIdAndUser(Long id, UserEntity user);
    Optional<CharacterEntity> findTopByUserIdOrderByIdAsc(Long userId);



}
