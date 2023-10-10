package multi.module.controller;

import lombok.RequiredArgsConstructor;
import multi.module.core.common.util.ApiResult;
import multi.module.core.common.util.ApiUtils;
import multi.module.facade.Ex1Facade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ex1")
@RequiredArgsConstructor
public class Ex1Controller {
    private final Ex1Facade ex1Facade;

    @GetMapping()
    public ApiResult<Object> getList() {
        return ApiUtils.success(ex1Facade.get());
    }

}
