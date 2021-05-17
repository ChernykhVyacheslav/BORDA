package com.softserve.borda.controllers;

import com.softserve.borda.dto.UserSimpleDTO;
import com.softserve.borda.dto.CreateUserDTO;
import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.Role;
import com.softserve.borda.entities.User;
import com.softserve.borda.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;


    @GetMapping
    public List<UserSimpleDTO> getAllUsers() {
        return userService.getAll().stream()
                .map((user) -> modelMapper.map(user, UserSimpleDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public UserSimpleDTO getUserById(@PathVariable Long id) {
        return modelMapper.map(
                userService.getUserById(id),
                UserSimpleDTO.class);
    }

    @PostMapping
    public UserSimpleDTO createUser(@RequestBody final CreateUserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.getRoles().add(new Role(Role.Roles.ROLE_USER.name()));
        return modelMapper.map(
                userService.createOrUpdate(user),
                UserSimpleDTO.class);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PutMapping(value = "{id}")
    public UserSimpleDTO update(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getUserById(id);
        BeanUtils.copyProperties(user, existingUser);
        return modelMapper.map(
                userService.createOrUpdate(existingUser),
                UserSimpleDTO.class);
    }

    @GetMapping("/{id}/boards")
    public List<Board> getBoardsByUser(@PathVariable Long id) {
        return userService.getBoardsByUserId(id);
    }

    @GetMapping("{id}/boardsByRole/{boardRoleId}")
    public List<Board> getBoardsByBoardRoleAndUserId(@PathVariable Long id,
                                               @PathVariable Long boardRoleId) {
        return userService.getBoardsByUserId(id); //TODO
    }

}