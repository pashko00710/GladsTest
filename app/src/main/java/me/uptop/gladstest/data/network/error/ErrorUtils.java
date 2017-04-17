package me.uptop.gladstest.data.network.error;

import retrofit2.Response;

public class ErrorUtils {
    public static ApiError parseError(Response<?> response) {
        return new ApiError(response.code());
    }
}
