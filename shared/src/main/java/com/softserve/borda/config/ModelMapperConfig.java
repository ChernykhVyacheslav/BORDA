package com.softserve.borda.config;

import com.softserve.borda.dto.*;
import com.softserve.borda.entities.Invitation;
import com.softserve.borda.entities.Tag;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(tagToTagDTO);
        modelMapper.addConverter(inviteToInviteDTO);
        return modelMapper;
    }

    Converter<Tag, TagDTO> tagToTagDTO = new AbstractConverter<>() {
        protected TagDTO convert(Tag tag) {
            TagDTO tagDTO = new TagDTO();
            tagDTO.setId(tag.getId());
            tagDTO.setText(tag.getText());
            tagDTO.setColor(tag.getColor());
            tagDTO.setBoardId(tag.getBoardId());
            return tagDTO;
        }
    };

    Converter<Invitation, InvitationDTO> inviteToInviteDTO = new AbstractConverter<>() {
        protected InvitationDTO convert(Invitation invitation) {
            InvitationDTO invitationDTO = new InvitationDTO();

            invitationDTO.setId(invitation.getId());
            invitationDTO.setSender(new ModelMapper().map(invitation.getSender(), UserSimpleDTO.class));
            invitationDTO.setReceiverId(invitation.getReceiverId());
            invitationDTO.setBoard(new ModelMapper().map(invitation.getBoard(), BoardSimpleDTO.class));
            invitationDTO.setUserBoardRole(new ModelMapper().map(invitation.getUserBoardRole(), UserBoardRoleDTO.class));
            invitationDTO.setIsAccepted(invitation.getIsAccepted());
            return invitationDTO;
        }
    };
}
