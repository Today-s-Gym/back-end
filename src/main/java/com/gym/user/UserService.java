package com.gym.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void insertUser(User user)
    {
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String useremail) {
        //검색 결과가 없을 때 빈 User 객체 반환
        User findUser = userRepository.findByEmail(useremail).orElseGet(
                new Supplier<User>() {
                    @Override
                    public User get() {
                        return new User();
                    }
                });

        return findUser;
    }

}
