package team.combinatorics.shuwashuwa.service;

import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDto;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDto;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDto;

@Service
public interface UserService {

    LogInSuccessDto wechatLogin(LogInInfoDto logInInfoDto) throws Exception;

    int deleteOneUser(String openID) throws Exception;

    void deleteAllUsers();

    void updateUserInfo(String code, UpdateUserInfoDto updateUserInfoDto) throws Exception;

    UpdateUserInfoDto getUserInfo(String code) throws Exception;

    // boolean suicide();

    String test(LogInInfoDto logInInfoDto) throws Exception;
}
