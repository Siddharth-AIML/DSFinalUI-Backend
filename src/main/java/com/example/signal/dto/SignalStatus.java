// Create a DTO class
package com.example.signal.dto;

public class SignalStatus {
    private String vertical;
    private String horizontal;

    public SignalStatus(String vertical, String horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
    }

    public String getVertical() {
        return vertical;
    }

    public String getHorizontal() {
        return horizontal;
    }
}

