package team.combinatorics.shuwashuwa.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.model.pojo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Before
    @Test
    // 这个测试方法中随便编openid就行，并不向tx服务器验证
    public void insertUserTest() {
        userDao.addUserOpenid("fake openid 1");
        userDao.addUserOpenid("fake openid 2");
        userDao.addUserOpenid("fake openid 3");
        userDao.addUserOpenid("fake openid 4");

    }

    @Test
    public void addUserOpenidTest() {
        userDao.updateUserInfo(new User(1, null, "misaki"
                , "粉红裸熊", null, null
                , null, null, null, null, null
                , false, false, false));
    }

}

