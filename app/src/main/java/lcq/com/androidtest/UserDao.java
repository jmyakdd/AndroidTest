package lcq.com.androidtest;

import lcq.com.androidtest.bean.User;

/**
 * Created by CRTE-CD-13 on 2018/5/8.
 */

public class UserDao {
    public static boolean save(User user){
        return user.save();
    }
}
