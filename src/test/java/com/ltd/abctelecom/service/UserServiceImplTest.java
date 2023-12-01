package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.exception.CustomException;
import com.ltd.abctelecom.model.Role;
import com.ltd.abctelecom.model.UserModel;
import com.ltd.abctelecom.repository.ServiceRepository;
import com.ltd.abctelecom.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ServiceRepository serviceRepository;

    @InjectMocks
    UserService userService = new UserServiceImpl();

    @DisplayName("Login - Success Scenario")
    @Test
    void loginTest(){
        //mocking
        Users user = getMockUser();
        when(userRepository.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(user);

        //Actual
        UserModel mUser = userService.getUser("shoaib@abc.com", "123");

//        verification
        verify(userRepository, times(1))
                .findByEmailAndPassword(anyString(),anyString());
        //Assert
        assertNotNull(mUser);
        assertEquals(user.getUserName(), mUser.getUserName());
    }

    @DisplayName("Login - Failure Scenario")
    @Test
    public void loginFailureTest(){
        // Mocking
        when(userRepository.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(null);
        // Actual
        CustomException exception = assertThrows(
                CustomException.class,
                () -> userService.getUser("abc@xyz.com", "1234dfcA"));
        // Verify
        verify(userRepository, times(1))
                .findByEmailAndPassword(anyString(), anyString());
        // Assertion
        assertEquals("USER_NOT_FOUND", exception.getErrorCode());
    }


    private Users getMockUser() {
        return Users.builder()
                .id(1L)
                .userName("Shoaib")
                .email("shoaib@abc.com")
                .role("MANAGER")
                .pinCode("8888")
                .password("123")
                .build();
    }

    @DisplayName("Get Users List")
    @Test
    public void getAllUsersTest(){
//        Mocking
        List<Users> users = new ArrayList<>();
        users.add(getMockUser());
        when(userRepository.findAll())
                .thenReturn(users);
//        Actual
        List<UserModel> userList = userService.getAllUsers();
//        Verify
        verify(userRepository,times(1))
                .findAll();
//        Assertion
        assertEquals(users.get(0).getId(), userList.get(0).getId());
    }

    @DisplayName("Create User")
    @Disabled
    @Test
    public void createUserTest(){
        // Mocking
        UserModel userModel = getMockUserModel();
        Users user = getMockUser();
        when(userRepository.save(any(Users.class)))
                .thenReturn(user);
        // Actual
        String userId = userService.createUser(userModel);

        //verify
        verify(userRepository, times(1))
                .save(any());

        //Assertion
        assertEquals(user.getId().toString(), userId);

    }

    private UserModel getMockUserModel() {
        return UserModel.builder()
                .userName("Shoaib")
                .email("shoaib@xyz.com")
                .password("123")
                .role(Role.MANAGER)
                .pinCode("8888")
                .build();
    }

}