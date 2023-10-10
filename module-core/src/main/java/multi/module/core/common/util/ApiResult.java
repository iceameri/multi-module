package multi.module.core.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ApiResult<T> {
    private final boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public ApiResult(boolean success, T result) {
        this.success = success;
        this.result = result;
    }
}
