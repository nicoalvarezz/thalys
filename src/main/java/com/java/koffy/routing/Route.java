package com.java.koffy.routing;

import com.java.koffy.App;
import com.java.koffy.container.Container;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.http.ResponseEntity;
import com.java.koffy.http.Middleware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Route {

    /**
     * Request URI.
     */
    private String uri;

    /**
     * Action associated to this URI.
     */
    private Function<RequestEntity, ResponseEntity> action;

    /**
     * Regular expression used to match incoming request URIs.
     */
    private String regex;

    /**
     * Route parameters.
     */
    private List<String> parameters;

    /**
     * HTTP middlewares.
     */
    private List<Middleware> middlewares = new ArrayList<>();

    /**
     * Validatable class
     */
    private Class<?> validatable;

    public Route(String uri, Function<RequestEntity, ResponseEntity> action) {
        this.uri = uri;
        this.action = action;
        this.regex = Pattern.compile("\\{([^{}]+)\\}").matcher(uri).replaceAll("([a-zA-Z0-9]+)");
        this.parameters = Pattern.compile("\\{([^{}]+)\\}")
                        .matcher(uri).results()
                        .map(matchResult -> matchResult.group(1))
                        .toList();
    }

    /**
     * Set validatable class
     * @param validatable
     */
    public void setValidatable(Class<?> validatable) {
        this.validatable = validatable;
    }

    /**
     * Get validatable class
     * It returns Class that will be used to validate the post body
     * @return {@link Class}
     */
    public Class<?> getValidatable() {
        return validatable;
    }

    /**
     * Return route URI.
     * @return {@link String}
     */
    public String getUri() {
        return uri;
    }

    /**
     * Return action associate tot this URI.
     * @return {@link Function< RequestEntity ,  ResponseEntity >}
     */
    public Function<RequestEntity, ResponseEntity> getAction() {
        return action;
    }

    /**
     * Return all HTTP middlewares for this route.
     * @return {@link List<Middleware>}
     */
    public List<Middleware> getMiddlewares() {
        return middlewares;
    }

    /**
     * Takes the list of middleware classes. These classes must implement the {@link Middleware} interface.
     * This method creates a new instance of each element and saves it the middlewares list.
     * @param middlewares {@link ArrayList<Class<?>>} middleware classes for this route.
     */
    public void setMiddlewares(ArrayList<Class<?>> middlewares) {
        this.middlewares = middlewares
                .stream()
                .map(clazz -> {
                    try {
                        return clazz.asSubclass(Middleware.class).getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create instance for " + clazz.getName(), e);
                    }
                }).collect(Collectors.toList());
    }

    /**
     * Check if the middleware list is empty.
     * @return {@link Boolean}
     */
    public boolean hasMiddlewares() {
        return !middlewares.isEmpty();
    }

    /**
     * Check if the given URI matches the regex of this route.
     * @param uri {@link String}
     * @return {@link Boolean}
     */
    public boolean matches(String uri) {
        return Pattern.compile(String.format("^%s/?$", regex)).matcher(uri).matches();
    }

    /**
     * Check if the route has variable parameters in its definition.
     * @return {@link Boolean}
     */
    public boolean hasParameters() {
        return !this.parameters.isEmpty();
    }

    /**
     * Ge the key-value pairs from the URI as defined by this route.
     * This method combines the list of parameters and list of arguments in a {@link Map}.
     * @param uri {@link String}
     * @return {@link Map}
     */
    public Map<String, String> parseParameter(String uri) {
        List<String> arguments = extractMatchGroups(uri);

        // Converting List<String> parameters and List<String> arguments to a HashMap (Combining)
        return IntStream.range(0, parameters.size()).boxed().collect(Collectors.toMap(parameters::get, arguments::get));
    }

    public static Route get(String uri, Function<RequestEntity, ResponseEntity> action) {
        return Container.resolve(App.class).router().get(uri, action);
    }

    /**
     * Extract the matching URI groups. This method is specifically used to extract the {@link Route} parameters.
     * @param uri {@link String}
     * @return {@link List<String>}
     */
    private List<String> extractMatchGroups(String uri) {
        Matcher matcher = Pattern.compile(String.format("^%s$", regex)).matcher(uri);

        if (!matcher.find()) {
            return Collections.emptyList();
        }

        List<String> groups = new ArrayList<>();
        for (int i = 1; i <= matcher.groupCount(); i++) {
            groups.add(matcher.group(i));
        }
        return groups;
    }
}
