package multi.module.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import multi.module.service.Ex1Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class Ex1Facade {
    private final Ex1Service ex1Service;
    public Object get() {
        return ex1Service.get();
    }
}
