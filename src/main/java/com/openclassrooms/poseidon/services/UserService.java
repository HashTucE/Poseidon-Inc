package com.openclassrooms.poseidon.services;


import com.openclassrooms.poseidon.domain.User;
import com.openclassrooms.poseidon.exceptions.EmptyListException;
import com.openclassrooms.poseidon.exceptions.NotExistingException;
import com.openclassrooms.poseidon.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {



    private static final Logger log = LogManager.getLogger(UserService.class);


    @Autowired
    private UserRepository userRepository;


    /**
     * call findAll from repository
     * @return user list
     * @throws EmptyListException e
     */
    public List<User> findAll() {

        log.info("findAll from userService called");
        return userRepository.findAll();
    }


    /**
     * check if an object exist by id
     * @param id id
     * @return boolean
     * @throws NotExistingException e
     */
    public boolean existsById(int id) throws NotExistingException {

        boolean isUserExist = userRepository.existsById(id);
        if (!isUserExist) {
            throw new NotExistingException("user", id);
        }
        log.debug("existsById = " + id + " from User service called with success");
        return true;
    }


    /**
     * call findById from repository
     * @param id id
     * @return user object
     * @throws NotExistingException e
     */
    public User findById(int id) throws NotExistingException {

        log.info("findById = " + id + " from userService called");
        return userRepository.findById(id).orElseThrow(()-> new NotExistingException("user", id));
    }


    /**
     * call save method from repository
     * @param user user
     * @return user object
     */
    public User addUser(User user) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setFullname(user.getFullname());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setRole(user.getRole());
        userRepository.save(user);
        log.info("addUser with id " + user.getId() + " from service called");
        return user;
    }


    /**
     * update a user calling save from repository
     * @param id id
     * @param user user
     * @throws NotExistingException e
     */
    public void updateUser(int id, User user) throws NotExistingException {

        User userToModify = findById(id);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userToModify.setFullname(user.getFullname());
        userToModify.setPassword(encoder.encode(user.getPassword()));
        userToModify.setUsername(user.getUsername());
        userToModify.setRole(user.getRole());
//        userToModify.setId(id);
        userRepository.save(userToModify);
        log.debug("updateTrade with id " + user.getId() + " from service called with success");
    }


    /**
     * delete an object by id calling delete from repository
     * @param id id
     * @throws NotExistingException e
     */
    public void deleteById(int id) throws NotExistingException {

        User optionalUser = findById(id);

        userRepository.delete(optionalUser);
        log.debug("deleteById = " + id + " from Trade service called with success");
    }


    /**
     * call findByUsername from repository
     * @param username username
     * @return user object
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
