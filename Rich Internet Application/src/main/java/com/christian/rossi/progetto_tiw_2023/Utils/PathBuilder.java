package com.christian.rossi.progetto_tiw_2023.Utils;

public class PathBuilder {

    private final StringBuilder pathBuilder;
    private boolean noParams;

    public PathBuilder(String location) {
        pathBuilder = new StringBuilder();
        pathBuilder.append(location);
        noParams = true;
    }

    public PathBuilder addParam(String key, Object value) {
        if (noParams) {
            pathBuilder.append("?");
            noParams = false;
        } else pathBuilder.append("&");
        pathBuilder.append(key).append("=").append(value);
        return this;
    }

    @Override
    public String toString() {
        return pathBuilder.toString();
    }
}