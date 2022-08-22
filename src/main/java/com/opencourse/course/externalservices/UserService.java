package com.opencourse.course.externalservices;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.opencourse.course.dtos.UserInfoDto;


@Service
public class UserService {
    public  UserInfoDto getUserInfo(Long userId){
        UserInfoDto userInfo=new UserInfoDto();
        userInfo.setFirstname("Aohamed Amine");
        userInfo.setLastname("Ben Abbes");
        userInfo.setDateOfBirth(LocalDateTime.of(1999, 1, 24, 0, 0));
        return userInfo;
    }
}
