package org.pwd.web.api;

public class ApiResponse {

    private final double score;

    public ApiResponse(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }
}
