package com.anotap.whatagreatmovie.model;

import java.util.List;

public class ErrorUser {
    private List<String> username;

    public String getErrorMessage() {
        return username.size() > 0 ? username.get(0) : null;
    }
}
