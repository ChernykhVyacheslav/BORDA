package com.softserve.borda.repositories;

import com.softserve.borda.entities.Role;
import com.softserve.borda.entities.UserBoardRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> getAllRolesByUserBoardRelationsContaining(UserBoardRelation userBoardRelation);
}
