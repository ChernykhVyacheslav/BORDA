package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Tag;
import com.softserve.borda.entities.UserBoardRelation;
import com.softserve.borda.repositories.RoleRepository;
import com.softserve.borda.repositories.UserBoardRelationRepository;
import com.softserve.borda.services.UserBoardRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserBoardRelationServiceImpl implements UserBoardRelationService {
    
    private final UserBoardRelationRepository userBoardRelationRepository;
    private final RoleRepository roleRepository;

    public UserBoardRelationServiceImpl(UserBoardRelationRepository userBoardRelationRepository, RoleRepository roleRepository) {
        this.userBoardRelationRepository = userBoardRelationRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserBoardRelation> getAll() {
        return userBoardRelationRepository.findAll();
    }

    @Override
    public UserBoardRelation getUserBoardRelationById(Long id) {
        Optional<UserBoardRelation> userBoardRelation = userBoardRelationRepository.findById(id);
        if(userBoardRelation.isPresent()) {
            return userBoardRelation.get();
        }
        return null; // TODO: Throw custom exception
    }

    @Override
    public UserBoardRelation createOrUpdate(UserBoardRelation userBoardRelation) {
        if (userBoardRelation.getId() != null) {
            Optional<UserBoardRelation> userBoardRelationOptional = userBoardRelationRepository.findById(userBoardRelation.getId());

            if (userBoardRelationOptional.isPresent()) {
                UserBoardRelation newUserBoardRelation = userBoardRelationOptional.get();
                return userBoardRelationRepository.save(newUserBoardRelation);
            }
        }
        return userBoardRelationRepository.save(userBoardRelation);
    }

    @Override
    public void deleteUserBoardRelationById(Long id) {
        userBoardRelationRepository.deleteById(id);
    }

    @Override
    public List<Tag> getAllRolesByUserBoardRelationId(Long userBoardRelationId) {
        return roleRepository.getAllRolesByUserBoardRelationId(userBoardRelationId);
    }
}
