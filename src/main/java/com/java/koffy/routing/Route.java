package com.java.koffy.routing;

import com.java.koffy.http.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Route {

    private String uri;
    private Supplier<Response> action;
    private String regex;
    private List<String> parameters;

    public Route(String uri, Supplier<Response> action) {
        this.uri = uri;
        this.action = action;
        this.regex = Pattern.compile("\\{([^{}]+)\\}").matcher(uri).replaceAll("([a-zA-Z0-9]+)");
        this.parameters = Pattern.compile("\\{([^{}]+)\\}")
                        .matcher(uri).results()
                        .map(matchResult -> matchResult.group(1))
                        .toList();
    }

    public String getUri() {
        return uri;
    }

    public Supplier<Response> getAction() {
        return action;
    }

    public boolean matches(String uri) {
        return Pattern.compile(String.format("^%s/?$", regex)).matcher(uri).matches();
    }

    public boolean hasParameters() {
        return !this.parameters.isEmpty();
    }

    public Map<String, String> parseParameter(String uri) {
        List<String> arguments = extractMatchGroups(uri);

        // Converting List<String> parameters and List<String> arguments to a HashMap (Combining)
        return IntStream.range(0, parameters.size()).boxed().collect(Collectors.toMap(parameters::get, arguments::get));
    }

    private List<String> extractMatchGroups(String uri){
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
