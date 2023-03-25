package com.example.springsecurityauthtwo.security.services;

import java.util.List;
import java.util.Set;

import com.example.springsecurityauthtwo.security.model.dtos.SignupRequest;
import com.example.springsecurityauthtwo.security.model.entities.AppRole;
import com.example.springsecurityauthtwo.security.model.entities.AppUser;
import com.example.springsecurityauthtwo.security.model.enumeration.ERole;

/**
 * Interface for users management methods
 *
 * @author Alexandre Lourencinho
 * @version 1.0
 */
public interface UserServices {

    /**
     * The method used to retrieve an AppUser from his username.
     *
     * @param username the username of the user
     * @return the user
     */
    AppUser findUserByUsername(String username);

    /**
     * The method used to check if a username is already taken
     *
     * @param username the username
     * @return true if the username is already taken, false otherwise
     */
    Boolean usernameAlreadyExists(String username);

    /**
     * The method used to check if an email is already taken
     *
     * @param email the email
     * @return true if the email is already taken, false otherwise
     */
    Boolean emailAlreadyExists(String email);

    /**
     * The method used to retrieve an AppRole from his role name
     *
     * @param roleName the role name of the role
     * @return the role
     */
    AppRole findRoleByRoleName(ERole roleName);

    /**
     * The method used to register a new user
     *
     * @param user the user to register
     * @return the registered user
     */
    AppUser saveNewUser(AppUser user);

    /**
     * The method used to manage the roles of a new user
     *
     * @param strRoles the roles in string format
     * @param roles    the roles in AppRole format
     */
    void manageRoles(Set<String> strRoles, Set<AppRole> roles);

    /**
     * The method used to retrieve an AppUser from his id.
     *
     * @param id the id of the user
     * @return the user
     */
    AppUser findById(Long id);

    /**
     * find all the users
     *
     * @return a list of all users
     */
    List<AppUser> findAll();

    /**
     * The method used to update the information of a user
     *
     * @param user     the new information of the user
     * @param username the username of the user to update
     * @return the updated user
     */
    AppUser updateUserInfo(SignupRequest user, String username);

    /**
     * used to delete a user
     *
     * @param user the user to be deleted
     */
    void deleteUser(AppUser user);
}
