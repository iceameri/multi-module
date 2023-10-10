package multi.module.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import multi.module.core.ex2.repo.Ex2Repository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Ex2Service {
    private final Ex2Repository ex2Repository;
    public Object create(Object request, Object ob1) {
        // Ex2 엔티티 생성
        // ex2Repository.save(Ex2);
        return null;
    }
}
