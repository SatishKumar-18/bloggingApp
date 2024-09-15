package com.project.BloggingApp.repository;

import com.project.BloggingApp.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepo;

    @Test
    void can_create_users(){
        var user = User.builder()
                .username("SatishKumar")
                .password("Satish")
                .build();

        userRepo.save(user);
    }
    @Test
     void get_all_user(){
        var user = userRepo.findAll();

        Assertions.assertNotNull(user);
     }

     @Test
     void delete_user(){
        userRepo.deleteAll();
     }

}
