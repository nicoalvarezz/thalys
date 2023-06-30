package com.java.koffy;

import com.java.koffy.database.SQLDao;

public class UserDao extends SQLDao<User> {

    public UserDao() {
        super(User.class);
    }
}
