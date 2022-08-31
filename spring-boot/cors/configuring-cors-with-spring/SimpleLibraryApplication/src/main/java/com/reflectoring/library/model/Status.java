package com.reflectoring.library.model;

public enum Status {
    SUCCESS(200) {
        @Override
        public String toString() {
            return "Success";
        }
    },
    ERROR(500) {
        @Override
        public String toString() {
            return "Error";
        }
    };

    private int code;

    Status(int code) {
        this.code = code;
    }

    public static int fetchCode(String statusCode) {
        for (Status s : Status.values()) {
            if (s.name().equals(statusCode.toUpperCase())) {
                return s.code;
            }
        }
        return 0;
    }
}
