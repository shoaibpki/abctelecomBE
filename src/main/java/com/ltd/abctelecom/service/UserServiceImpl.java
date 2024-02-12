package com.ltd.abctelecom.service;

import com.ltd.abctelecom.entity.Services;
import com.ltd.abctelecom.entity.Users;
import com.ltd.abctelecom.exception.CustomException;
import com.ltd.abctelecom.model.Role;
import com.ltd.abctelecom.model.UserModel;
import com.ltd.abctelecom.repository.ServiceRepository;
import com.ltd.abctelecom.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public UserServiceImpl() {
    }

    @Override
    public UserModel getUser(String email, String password) {
//        checking user admin
        if ( email.equalsIgnoreCase ("admin@abc.com") &&
                password.equalsIgnoreCase("123")){
//            set admin user default properties
            log.info("email: {}, password: {}",email, password);
            return UserModel.builder()
                    .userName("Admin")
                    .role(Role.ADMIN)
                    .email(email)
                    .password(password)
                    .build();
        }else { // checking other user for login
            try {
                Users user = userRepository.findByEmailAndPassword(email, password);
                return UserModel.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(Role.valueOf(user.getRole()))
                        .userName(user.getUserName())
                        .mobile((user.getMobile()))
                        .password(user.getPassword())
                        .services(user.getServices())
                        .complaints(user.getComplaints())
                        .pinCode(user.getPinCode())
                        .build();
            }catch (Exception e){
                throw new CustomException(
                        "User not found by given email or entered wrong password",
                        "USER_NOT_FOUND"
                        );
            }
        }
    }

    @Override
    public String createUser(UserModel user) {
        log.info("Adding User..: {}",user);
        Users mUser = Users.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().name())
                .pinCode(user.getPinCode())
                .mobile(user.getMobile())
                .services(user.getServices())
                .build();
        userRepository.save(mUser);
        log.info("::{}", mUser);
//        return "Successfully Created user with id : "+ mUser.getId();
        return mUser.getId().toString();
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream().map( user ->
                UserModel.builder()
                        .id(user.getId())
                        .services(user.getServices())
                        .pinCode(user.getPinCode())
                        .password(user.getPassword())
                        .mobile(user.getMobile())
                        .role(Role.valueOf(user.getRole()))
                        .userName(user.getUserName())
                        .email(user.getEmail())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public UserModel updateUser(Long id, UserModel userModel) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        "User not found by given id: {}"+ id,
                        "USER_NOT_FOUND"
                ));

        user.setUserName(userModel.getUserName());
        user.setEmail(userModel.getEmail());
        user.setRole(userModel.getRole().name());
        user.setMobile(userModel.getMobile());
        user.setPassword(userModel.getPassword());
        user.setPinCode(userModel.getPinCode());
        user.setServices(userModel.getServices());
        userRepository.save(user);
        copyProperties(user, userModel);
        return userModel;
    }
    @Override
    public UserModel assignServiceToCustomer(Long uid, Long sid) {
        Users users = userRepository.findById(uid).map(
                user -> {
                    if (user.getRole().equalsIgnoreCase("customer")){
                        Set<Services> services = user.getServices();
                        Services srv = serviceRepository.findById(sid)
                                .orElseThrow(() -> new CustomException(
                                        "Service Not Found by given id: "+ sid,
                                        "SERVICE_NOT_FOUND"
                                ));
                        services.add(srv);
                        user.setServices(services);
                        return user;
                    } else {
                        throw new CustomException(
                                "Services only Assign to Customers",
                                "SERVICE_WRONG_ASSIGN");
                    }
                }
        ).orElseThrow(() -> new CustomException(
                "User not found by given user id: "+ uid,
                "USER_NOT_FOUND"
        ));
        userRepository.save(users);
        UserModel userModel = new UserModel();
        copyProperties(users, userModel);
        userModel.setRole(Role.valueOf(users.getRole()));
        return userModel;
    }

    @Override
    public UserModel deleteServiceFromUser(Long uid, Long sid) {
        Users user = userRepository.findById(uid)
                .orElseThrow(() -> new CustomException(
                        "User not Found by given Id: "+uid,
                        "USER_NOT_FOUND"
                ));
        Services service = serviceRepository.findById(sid)
                .orElse(null);
        if (service != null){
            Set<Services> services = user.getServices();
            services.remove(service);
            user.setServices(services);
            userRepository.save(user);
        }
        UserModel userModel = new UserModel();
        copyProperties(user, userModel);
        userModel.setRole(Role.valueOf(user.getRole()));
        return userModel;
    }

    @Override
    public String deleteUser(long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        "User not found by given user id: "+ id,
                        "USER_NOT_FOUND"
                ));
        userRepository.delete(user);
        return "Delete User Successfully!";
    }
}
