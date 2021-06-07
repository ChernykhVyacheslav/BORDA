package com.softserve.borda.repositories;

import com.softserve.borda.entities.User;
import com.softserve.borda.entities.UserBoardRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByUserBoardRelationsIn(List<UserBoardRelation> userBoardRelations);
}