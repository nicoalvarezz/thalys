package com.java.koffy;

import com.java.koffy.database.Database;
import com.java.koffy.http.HttpStatus;
import com.java.koffy.http.ResponseEntity;
import com.mysql.cj.log.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) throws Exception {
        App app = App.bootstrap();
        app.router().get("/test/{param}", (request) -> ResponseEntity.jsonResponse(request.routeParams())
                .status(HttpStatus.OK)
                .build());

        app.router().post("/test-post", (request) ->
                ResponseEntity.jsonResponse(request.getPostData()).status(HttpStatus.OK).build());

        app.router().get("/test", (request) ->
            ResponseEntity.jsonResponse(request.getQueryData()).status(HttpStatus.OK).build());

        app.router().get("/redirect", (request) -> ResponseEntity.redirectResponse("/test/5"));

//        app.router().post("/test-email", (request) -> {
//            QuickTest quickTest = request.getSerialized(QuickTest.class);
//            return ResponseEntity.textResponse(quickTest.getEmail()).build();
//        }, QuickTest.class);

//        app.router().get("/middlewares", (request -> ResponseEntity.jsonResponse(new HashMap<>() {{
//            put("message", "OK");
//        }}).build())).setMiddlewares(new ArrayList<>() {{
//            add(AuthMiddleware.class);
//        }});

        app.router().get("/session", (request) -> {
            app.session().flash("alert", "success");
            return ResponseEntity.jsonResponse(app.session().getAllAttributesStringFormat()).build();
        });

        app.router().get("/another-session", (request) ->
                ResponseEntity.jsonResponse(app.session().getAllAttributesStringFormat()).build());

        app.router().get("/select", (request) -> {
            try {
                return ResponseEntity.jsonResponses(
                        new HashMap<>() {{
                            put("users", Database.selectStatement("SELECT * FROM user"));
                        }}).build();
            } catch (RuntimeException e) {
                return ResponseEntity.textResponse("id not found").build();
            }
        });

        app.router().post("/update", (request) -> {
            return ResponseEntity.textResponse(
                    String.valueOf(Database.updateStatement("UPDATE user SET name = ? WHERE id = ?",
                            request.getPostData("name").get(), request.getPostData("id").get()))
            ).build();
        });

        app.router().post("/delete", (request) -> {
            return ResponseEntity.textResponse(
                    String.valueOf(
                            Database.deleteStatement("DELETE FROM user WHERE id = ? ", request.getPostData("id").get())
                    )
            ).build();
        });

        app.router().post("/insert", (request) -> {
           return ResponseEntity.textResponse(String.valueOf(
                   Database.insertStatement("INSERT INTO user(name, id) VALUES(?, ?)", request.getPostData("name").get(), request.getPostData("id").get())
           )).build();
        });

        app.router().post("/save", (request) -> {
            UserDao userDao = new UserDao();
            User user = new User(request.getPostData("name").get(), Long.valueOf(request.getPostData("id").get()));
            try {
                userDao.save(user);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.textResponse("object saved").build();
        });

        app.router().get("/getById", (request) -> {
            UserDao userDao = new UserDao();
            User user;
            try {
                user = userDao.getById(Integer.valueOf(request.getQueryData("id").get()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.textResponse(user.getName()).build();
        });

        app.router().get("/getAll", (request) -> {
            List<User> users = new ArrayList<>();
            UserDao userDao = new UserDao();
            List<Map<String, Object>> list;
             try {
                 users = userDao.getAll();
                 Map<String,Object> map = new HashMap<>();
                 list = new ArrayList<>();
                 for (User user : users) {
                    map.put("name", user.getName());
                    map.put("id", user.getId());
                    list.add(map);
                 }
             } catch (SQLException e) {
                 throw new RuntimeException(e);
             }
             return ResponseEntity.jsonResponses(new HashMap<>() {{
                 put("users", list);
             }}).build();
        });

        app.router().get("/delete", (request) -> {
            UserDao  userDao = new UserDao();
            User user = new User(request.getQueryData("name").get(), Long.valueOf(request.getQueryData("id").get()));
            try {
                userDao.delete(user);
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
            return ResponseEntity.textResponse("something was deleted").build();
        });

        app.run(8000);
    }
}

