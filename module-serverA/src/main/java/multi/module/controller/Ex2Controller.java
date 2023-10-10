package multi.module.controller;

import lombok.RequiredArgsConstructor;
import multi.module.core.common.util.ApiResult;
import multi.module.core.common.util.ApiUtils;
import multi.module.facade.Ex1Facade;
import multi.module.facade.Ex2Facade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ex1")
@RequiredArgsConstructor
public class Ex2Controller {
    private final Ex2Facade ex2Facade;

    @GetMapping()
    public ApiResult<Object> create(@RequestBody Object request) {
        return ApiUtils.success(ex2Facade.create(request));
    }
}
