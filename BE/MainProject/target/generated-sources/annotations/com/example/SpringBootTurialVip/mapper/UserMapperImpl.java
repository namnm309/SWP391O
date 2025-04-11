package com.example.SpringBootTurialVip.mapper;

import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.request.CustomerCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-12T02:23:55+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.bod( request.getBod() );
        user.email( request.getEmail() );
        user.fullname( request.getFullname() );
        user.gender( request.getGender() );
        user.parentid( request.getParentid() );
        user.password( request.getPassword() );
        user.phone( request.getPhone() );
        user.username( request.getUsername() );

        return user.build();
    }

    @Override
    public User toUser(ChildCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.bod( request.getBod() );
        user.fullname( request.getFullname() );
        user.gender( request.getGender() );
        user.height( request.getHeight() );
        user.parentid( request.getParentid() );
        user.weight( request.getWeight() );

        return user.build();
    }

    @Override
    public User toUser(CustomerCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.bod( request.getBod() );
        user.email( request.getEmail() );
        user.fullname( request.getFullname() );
        user.gender( request.getGender() );
        user.phone( request.getPhone() );
        user.username( request.getUsername() );

        return user.build();
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getBod() != null ) {
            user.setBod( LocalDateTime.ofInstant( request.getBod().toInstant(), ZoneOffset.UTC ).toLocalDate() );
        }
        else {
            user.setBod( null );
        }
        user.setEmail( request.getEmail() );
        user.setFullname( request.getFullname() );
        user.setGender( request.getGender() );
        user.setPassword( request.getPassword() );
        user.setPhone( request.getPhone() );
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.avatarUrl( user.getAvatarUrl() );
        userResponse.bod( user.getBod() );
        userResponse.email( user.getEmail() );
        userResponse.fullname( user.getFullname() );
        userResponse.gender( user.getGender() );
        userResponse.id( user.getId() );
        userResponse.parentid( user.getParentid() );
        userResponse.phone( user.getPhone() );
        userResponse.username( user.getUsername() );

        return userResponse.build();
    }

    @Override
    public ChildResponse toChildResponse(User user) {
        if ( user == null ) {
            return null;
        }

        ChildResponse.ChildResponseBuilder childResponse = ChildResponse.builder();

        childResponse.childId( user.getId() );
        childResponse.avatarUrl( user.getAvatarUrl() );
        childResponse.fullname( user.getFullname() );
        childResponse.gender( user.getGender() );
        childResponse.height( user.getHeight() );
        childResponse.weight( user.getWeight() );

        return childResponse.build();
    }
}
