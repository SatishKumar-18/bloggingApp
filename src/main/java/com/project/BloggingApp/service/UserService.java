package com.project.BloggingApp.service;

import com.project.BloggingApp.dto.user_dto.UpdateUserDTO;
import com.project.BloggingApp.dto.user_dto.UserDTO;
import com.project.BloggingApp.entity.Article;
import com.project.BloggingApp.entity.User;
import com.project.BloggingApp.repository.ArticleRepository;
import com.project.BloggingApp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ArticleRepository articleRepo;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ModelMapper modelMapper;

    public UserService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Autowired
    private MailService mailService;

    public User createUser(UserDTO userDTO){
        try{
            if(!userDTO.getUsername().isEmpty() && !userDTO.getPassword().isEmpty()){
                User user = modelMapper.map(userDTO, User.class);
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                User save = userRepo.save(user);
                mailService.sendEmail(user.getEmail(), "Blogging App account created", "Thank you for choosing Blogging App to post your blogs");
                return save;
            }
            return null;
        } catch (Exception e){
            return null;
        }
    }

    public User updateUser(UpdateUserDTO updateUserDTO, String username){
        try{
            User existingUser = getUser(username);

            existingUser.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
            existingUser.setBio(updateUserDTO.getBio());
            existingUser.setEmail(updateUserDTO.getEmail());

            return userRepo.save(existingUser);

        } catch (Exception e){
            return null;
        }
    }

    public void save(User user){
        userRepo.save(user);
    }

    public User getUser(String username){
        return userRepo.findByUsername(username);
    }

    public String getUserProfile(User user){
        StringBuilder articleSlug= new StringBuilder();
        for(Article article : user.getArticle()){
            articleSlug.append(article.getSlug()).append(": ").append(article.getContent()).append("\n");
        }

        return user.getUsername() + "\n " + "Bio- " + user.getBio() + "\n "
                + "Articles- " + articleSlug;
    }

    public void deleteUser(String username){
        User user = userRepo.findByUsername(username);
        userRepo.delete(user);
        articleRepo.deleteByAuthor(username);
    }

}
