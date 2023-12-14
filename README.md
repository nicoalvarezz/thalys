![Alt text](.github/logo/logo.png)
---
<p align="center">
    <img src="https://github.com/nicoalvarezz/thalys/actions/workflows/merge_build.yml/badge.svg">
    <img src=".github/badges/jacoco.svg" />
    <img src=".github/badges/branches.svg" />
</p>

# Installation
__No maven dependency out just yet__

# Required configs
Beofore you start writing code, you will have to set two configs for your **thalys** application start up.
```
/src/resources/application.properties

thalys.app.port=8080
thalys.app.basePackage=com.example
```

These configurations will allow you to open any port you want. They will also set the base package of the project, this will allow thalys to find all your controllers, paths and configurations.

# Creating a Controller
To create a controller you can simply create a class contains the `@RestController` class tag.

```java
@RestController
public class ExampleClass {

}
```

This tag will allow the project to find your controller which will hold your endpoints. You can also give it a path, to set a "parent path" to all endpoints.

```java
@RestController("/api/v1")
```

Now you can start setting up you endpoints. Currently only the main HTTP methods are supported.
- Get
- Post
- Delete
- Put

To set the method above you have to write a method for a specific endpoint and assign it the tag for the HTTP method you want to be used for. Take the following example.

```java

@RestController
public class ExampleClass {
    
    @GetPath("/get-test")
    public ResponseEntity getTest(RequestEntity request) {
        // behaviour of endpoint goes here
    }

    @GetPath("/post-test")
    public ResponseEntity postTest(RequestEntity request) {
        // behaviour of endpoint goes here
    }

    @GetPath("/put-test")
    public ResponseEntity putTest(RequestEntity request) {
        // behaviour of endpoint goes here
    }

    @GetPath("/delete-test")
    public ResponseEntity deleteTest(RequestEntity request) {
        // behaviour of endpoint goes here
    }
}

```

All endpoint methods take a `RequestEntity` as the argument. With it you can use it to access a lot of the contents that are sent through the http request. Checkout the class to see what methods you can use from there. All methods have docs that especify their behaviour.

All endpoint methods return a `ResponseEntity` object. With the response entity you can craft the response as you need for your endpoint. Checkout the class to find how to build tis object and what options it has. This class also has up to date docs.

Take the following example on how to `RequestEntity` and `ResponseEntity`

```java
@RestController
public class ExampleClass {
    
    @GetPath("/get-test/{param}")
    public ResponseEntity getTest(RequestEntity request) {
        return ResponseEntity.jsonResponse(request.routeParams(), HttpStatus.OK);
    }
}
```

`reuquest.routeParams()` will construct a `Map<String, String>` of the params sent in the request.
`ResponseEntity.jsonResponse()` takes a map and the status. Checkout `ResponseEntity` class to find out all the possible ways to construct a response.

**Note**: Is importnat to note that **thalys** does not support url-encoded rests for now :(

# Middlewares

# Useful Enums
## HttpMethod
## Header
## ContentType
## HttpStatus

