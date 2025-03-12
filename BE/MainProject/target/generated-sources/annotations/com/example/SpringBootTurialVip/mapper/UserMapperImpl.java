package com.example.SpringBootTurialVip.mapper;

import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-12T08:15:52+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.parentid( request.getParentid() );
        user.username( request.getUsername() );
        user.fullname( request.getFullname() );
        user.password( request.getPassword() );
        user.email( request.getEmail() );
        user.phone( request.getPhone() );
        user.bod( request.getBod() );
        user.gender( request.getGender() );

        return user.build();
    }

    @Override
    public User toUser(ChildCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.parentid( request.getParentid() );
        user.fullname( request.getFullname() );
        user.bod( request.getBod() );
        user.gender( request.getGender() );
        user.height( request.getHeight() );
        user.weight( request.getWeight() );

        return user.build();
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        user.setFullname( request.getFullname() );
        user.setPassword( request.getPassword() );
        user.setEmail( request.getEmail() );
        user.setPhone( request.getPhone() );
        user.setBod( request.getBod() );
        user.setGender( request.getGender() );
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.parentid( user.getParentid() );
        userResponse.username( user.getUsername() );
        userResponse.fullname( user.getFullname() );
        userResponse.email( user.getEmail() );
        userResponse.phone( user.getPhone() );
        userResponse.bod( user.getBod() );
        userResponse.gender( user.getGender() );
        userResponse.avatarUrl( user.getAvatarUrl() );

        return userResponse.build();
    }

    @Override
    public ChildResponse toChildResponse(User user) {
        if ( user == null ) {
            return null;
        }

        ChildResponse.ChildResponseBuilder childResponse = ChildResponse.builder();

        childResponse.fullname( user.getFullname() );
        childResponse.gender( user.getGender() );
        childResponse.height( user.getHeight() );
        childResponse.weight( user.getWeight() );
        childResponse.avatarUrl( user.getAvatarUrl() );

        return childResponse.build();
    }
}
