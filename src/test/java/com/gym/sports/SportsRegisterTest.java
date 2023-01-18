package com.gym.sports;

import com.gym.login.SportsController;
import com.gym.user.User;
import com.gym.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SportsRegisterTest {
    @Autowired
    private UserRepository userRepository;

    User user = new User();

    @Autowired
    private SportsController sportsController;


    @Test
    public void test() throws Exception{
        sportsController.uploadSports(user, 2);

    }
}
