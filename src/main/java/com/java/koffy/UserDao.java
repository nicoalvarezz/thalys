package com.java.koffy;

import com.java.koffy.database.Dao;

public class UserDao extends Dao<User> {

    public UserDao() {
        super(User.class);
    }
}
