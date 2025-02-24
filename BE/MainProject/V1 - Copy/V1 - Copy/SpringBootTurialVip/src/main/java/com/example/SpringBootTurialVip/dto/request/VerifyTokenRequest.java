package com.example.SpringBootTurialVip.dto.request;

public class VerifyTokenRequest {
    private String token;

    public VerifyTokenRequest() {
    }

    public VerifyTokenRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Static Builder class
    public static class Builder {
        private String token;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public VerifyTokenRequest build() {
            return new VerifyTokenRequest(token);
        }
    }

    // Phương thức để tạo Builder
    public static Builder builder() {
        return new Builder();
    }
}
