package multi.module.core.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiUtils {

    public static <T> ApiResult<T> success(T result) {
        return new ApiResult<>(true, result);
    }

    public static <T> ApiResult<T> fail(T result) {
        return new ApiResult<>(false, result);
    }
}
