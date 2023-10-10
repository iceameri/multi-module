package multi.module.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import multi.module.service.Ex1Service;
import multi.module.service.Ex2Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class Ex2Facade {
    private final Ex2Service ex2Service;
    private final Ex1Service ex1Service;

    public Object create(Object request) {
        Object ob1 = ex1Service.get();
        return ex2Service.create(request, ob1);
    }
}
