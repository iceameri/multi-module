[상세 설명 링크](https://velog.io/@jwoo5264/%EC%B5%9C%EC%8B%A0-%EB%A9%80%ED%8B%B0%EB%AA%A8%EB%93%88-%EC%95%84%ED%82%A4%ED%85%8D%EC%B3%90%ED%8D%BC%EC%82%AC%EB%93%9C%ED%8C%A8%ED%84%B4-%EC%A0%81%EC%9A%A9)

## 개요
멀티모듈 아키텍쳐는 Monolithic과 MSA를 상호 전환가능한 아키텍처의 기초가 되도록 설계를 했습니다. 이전의 설계한 아키텍쳐는 개발하는 과정에서 controller, service, repository의 의존성 증가되고 가독성이 떨어지는 등 아키텍쳐가 애매하게 설계가되고 상세한 기조가 부족했다고 생각했습니다.
그래서 Facade패턴을 도입하여 의존성을 낮추고 가독성을 높이는 작업도 추가했습니다. Facade패턴을 적용하는 과정에서 생각했던 부분과 상세 기조는 따로 기술할 계획입니다.

- serverB 구현X
- 상세 로직 구현X
- DB연결 구현X

## 개발환경
IntelliJ | JAVA17 | Spring Boot 2.7 | Gradle | JPA
RESTFul API | Facade Pattern

## 아키텍쳐 구조
- Server 모듈
  - 서버가 실행이 되는 모듈
  - Controller 와 Facade에 해당하는 Service가 구현이 된다
  - 각각의 모듈 하나가 전혀 다른 서버를 의미한다.
  - 해당 모듈에 있는 Service는 비즈니스 로직을 담당한다.
  - 코드 컨벤션은 {도메인명}Controller, {도메인명}Facade
 
- Service 모듈
  - 엔티티를 다루는 service 함수를 구현하는 부분
  - auth의 경우 Token 인증, Security를 설정하고 Filter를 선언하고 인증/인가에 대한 Service모듈이다.
  - 코드 컨벤션은 {엔티티명}Service

- Core 모듈
  - 엔티티가 선언되고 데이터베이스와 통신하는 모듈
  - Entity / DTO / Exception 등을 선언한다.
  - core 서버에 참조되는 데이터베이스를 사용한다. -> 서버모듈에 따른 core모듈을 1대1로 사용하게된거나 독립적인 core모듈을 사용하게되면 MSA가 된다.

```gradle
# build.gradle
plugins {
	id 'java'
	id 'org.springframework.boot' version '2.7.16'
	id 'io.spring.dependency-management' version '1.0.15.RELEASE'
}

repositories {
	mavenCentral()
}

ext {
	serverEnv = System.getProperty('server.env', 'dev')
	springBootVersion = '2.7.12'
}

allprojects {
	apply plugin: 'java'
	apply plugin: 'org.springframework.boot'
	apply plugin: 'io.spring.dependency-management'

	group = 'multi.module'

	tasks.withType(JavaCompile) {
		options.encoding = "UTF-8"
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	repositories {
		mavenCentral()
	}

	tasks.named('test') {
		useJUnitPlatform()
	}
}

subprojects {
	dependencies {
		// 공통 라이브러리 추가 부분
		implementation 'org.springframework.boot:spring-boot-starter'
		implementation 'org.springframework.boot:spring-boot-starter-web'
		implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

		compileOnly 'org.projectlombok:lombok:1.18.24'
		annotationProcessor 'org.projectlombok:lombok:1.18.24'
		annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'

		testImplementation 'org.projectlombok:lombok:1.18.20'
		testImplementation 'org.springframework.boot:spring-boot-starter-test'
	}
}

project(':module-serverA') {
	dependencies {
		implementation project(path: ':module-auth', configuration: 'default')
		implementation project(path: ':module-service', configuration: 'default')
		// 빌드관련 라이브러리, 설정 추가
	}
}

project(':module-serverB') {
	dependencies {
		implementation project(path: ':module-service', configuration: 'default')
		// 빌드관련 라이브러리, 설정 추가
	}
}

project(':module-auth') {
	dependencies {
		implementation project(path: ':module-core', configuration: 'default')

		implementation 'org.springframework.boot:spring-boot-starter-security:2.5.4'
	}
}

project(':module-service') {
	dependencies {
		implementation project(path: ':module-core', configuration: 'default')
	}
}

project(':module-core') {
	dependencies {
		implementation 'org.springframework.boot:spring-boot-starter-data-redis'
		implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
		implementation 'mysql:mysql-connector-java:8.0.29'
	}
}

bootJar {
	enabled = false
}
jar {
	enabled = true
}
```

## 구현 설명

두가지의 구현 부분을 설명할 것이다.
Ex1을 회원이라 생각하고 Ex2 게시판이라고 했을때
먼저 Ex1(회원)에 대한 조회이다

```java
// Controller
public class Ex1Controller {
    private final Ex1Facade ex1Facade;

    @GetMapping()
    public ApiResult<Object> getList() {
        return ApiUtils.success(ex1Facade.get());
    }

}
// Facade
public class Ex1Facade {
    private final Ex1Service ex1Service;
    public Object get() {
        return ex1Service.get();
    }
}
public class Ex1Service {
    public Object get() {
				//구현 생략
        return null;
    }
}
```
Ex2(게시판)에 대한 생성부분이다.

Ex2Service에서 구현을 했다면 Repository에서 조회하여 재사용하지 못하지만
Service를 통해 조회함으로써 재사용하는것이다.
현재는 한개의 서버 내에서 사용가능하지만 백오피스를 개발할 경우 다른서버에서도 함수를 재사용할 수 있게 된다.

```java
// Controller
public class Ex2Controller {
    private final Ex2Facade ex2Facade;

    @GetMapping()
    public ApiResult<Object> create(@RequestBody Object request) {
        return ApiUtils.success(ex2Facade.create(request));
    }
}
// Facade
public class Ex2Facade {
    private final Ex2Service ex2Service;
    private final Ex1Service ex1Service;

    public Object create(Object request) {
        Object ob1 = ex1Service.get();
        return ex2Service.create(request, ob1);
    }
}
// Service
public class Ex2Service {
    private final Ex2Repository ex2Repository;
    public Object create(Object request, Object ob1) {
        // Ex2 엔티티 생성
        // ex2Repository.save(Ex2);
        return null;
    }
}
```
## 추가 설명
관습적인 추상화 implement-service 설계 방식 삭제
코드 구조가 복잡해지고, 복잡해진 구조 만큼 코드를 분석하고 확인하는 과정에서 인터페이스를 거쳐 구현체들을 확인해야 하는 번거로움 발생한다.

OCP를 포기하는 대신 Facade 패턴
하위 시스템 클래스들을 캡슐화 하는 것이 아니지만 그냥 서브 시스템들을 사용할 간단한 인터페이스를 제공하여 가독성과 구현의 편리성 및 속도 증가를 한다.

## 마무리
비즈니스 로직 구현하는 부분에 있어서 단일 Service에 대한 많은 Repository의 의존성이 늘어나서 좀 더 객체지향적으로 개선해보았습니다. 현재 새로 진행중인 프로젝트에 리팩토링했고 기존 프로젝트와 비교해봤을때 가독성이 높이게 되었습니다. 해당 아키텍쳐를 참고하신다면 더 상세한 기조를 세워나가는 것을 추천드립니다.
