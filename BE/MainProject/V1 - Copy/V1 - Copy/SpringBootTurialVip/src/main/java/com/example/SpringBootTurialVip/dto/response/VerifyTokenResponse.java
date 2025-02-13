package com.example.SpringBootTurialVip.dto.response;

public class VerifyTokenResponse {
    private boolean valid;

    public VerifyTokenResponse() {
    }

    public VerifyTokenResponse(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    // Static Builder class
    public static class Builder {
        private boolean valid;

        public Builder valid(boolean valid) {
            this.valid = valid;
            return this;
        }

        public VerifyTokenResponse build() {
            return new VerifyTokenResponse(valid);
        }
    }

    // Phương thức để tạo Builder
    public static Builder builder() {
        return new Builder();
    }

}
