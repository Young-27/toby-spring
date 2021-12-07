# 1.1 초난감 DAO

- DAO : DB를 사용해 데이터를 조회하거나 조작하는 기능을 전담하도록 만든 오브젝트
- 자바빈 : 두 가지 관례를 따라 만들어진 오브젝트
    - 디폴트 생성자 : 자바빈은 파라미터가 없는 디폴트 생성자를 가지고 있어야 함. 툴이나 프레임워크에서 리플렉션을 이용해 오브젝트를 생성하기 때문
        - 리플렉션 : 구체적인 클래스 타입을 알지 못해도, 그 클래스의 메소드, 타입, 변수들을 접근할 수 있도록 해주는 Java API
    - 프로퍼티 : 자바빈이 노출하는 이름을 가진 속성. 프로퍼티는 set으로 시작하는 수정자 메소드(setter)와 get으로 시작하는 접근자 메소드(getter)를 이용해 수정 또는 조회 가능

# 1.2 DAO의 분리

## 1.2.1 관심사의 분리

- 개발자가 객체를 설계할 때 가장 염두에 둬야 할 사항, **미래의 변화를 어떻게 대비할 것인가**
⇒ 객체지향 프로그래밍의 발전 (실세계를 최대한 가깝게 모델링)
    - 코드 변화의 폭을 최소한으로 줄여주기 (변경이 일어날 때 필요 작업 최소화, 문제를 일으키지 않도록 하기)
- 모든 변경과 발전은 **한 번에 한 가지 관심사항**에 집중되어 일어남 (but, 그에 따른 작업은 한 곳에 집중되지 않는다.)
    - 관심사의 분리
        - 관심이 같은 것끼리 하나의 객체 안으로, 관심이 다른 것은 서로 영향을 주지 않도록 분리하여 같은 관심에 효과적으로 집중하도록 만드는 것

## 1.2.2 커넥션 만들기의 추출

### **UserDao의 관심사항 세 가지**

1. DB와 연결을 위한 커넥션을 어떻게 가져올까
2. 사용자 등록을 위해 DB에 보낼 SQL 문장을 담을 Statement를 만들고 실행하는 것
3. 작업 후 Statement와 Connection 오브젝트 닫기

### **변경사항에 대한 검증: 리팩토링과 테스트**

- 리팩토링
    - 기존의 코드를 외부의 동작방식에는 변화 없이 내부 구조를 변경해서 재구성하는 작업 또는 기술
    - 생산성 up, 코드 품질 up, 유지보수 용이, 견고하고 유연한 제품 개발 가능 !!
    - 메소드 추출 기법 : 리팩토링에서 공통의 기능을 담당하는 메소드로 중복된 코드를 뽑아내는 것
    

## 1.2.3 DB 커넥션 만들기의 독립

**문제 가정)**

- 사용자가 다른 종류의 DB를 사용하고, 
DB커넥션을 가져오는 방법이 종종 변경될 가능성이 있으며, 
소스코드를 사용자에게 직접 공개하고 싶지는 않을 때 어떻게 해야될까?!

### **상속을 통한 확장**

- 관심사항을 분리해 상하위 클래스에 나눠 담기
- 데이터를 어떻게 등록하고 가져올 것인가
어떤 기능을 사용하는 지에 관심 ⇒ UserDao(슈퍼클래스)
- DB 연결 방법은 어떻게 할 것인가 
어떤 식으로 기능을 제공하는지에 관심 ⇒ NUserDao, DUserDao(서브클래스)
- **템플릿 메소드 패턴** : 슈퍼클래스에 기본적인 로직의 흐름을 만들고, 그 기능의 일부를 추상 메소드나 오버라이딩이 가능한 메소드로 만든 뒤 서브클래스에서 이런 메소드를 필요에 맞게 구현해서 사용하는 방법
    - 훅 메소드 : 슈퍼클래스에서 디폴트 기능을 정의해두거나 비워뒀다가 서브클래스에서 선택적으로 오버라이드 할 수 있도록 만들어둔 메소드
- **팩토리 메소드 패턴** : 서브클래스에서 구체적인 오브젝트 생성 방법을 결정하게 하는 것 / 오브젝트 생성 방법을 슈퍼클래스의 기본 코드에서 독립시키는 방법
    - 주로 인터페이스 타입으로 오브젝트를 리턴
    - 서브클래스는 다양한 방법으로 오브젝트를 생성하는 메소드를 재정의 가능
    - 팩토리 메소드 : 서브클래스에서 오브젝트 생성 방법과 클래스를 결정할 수 있도록 미리 정의해둔 메소드
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f7d38c3e-dec6-41db-922d-01d1858b9fcd/Untitled.png)
        

> **디자인 패턴**
> 
> - 소프트웨어 설계 시 특정 상황에서 자주 만나는 문제를 해결하기 위해 사용할 수 있는 재사용 가능한 솔루션
> - 객체지향적인 설계로 문제 해결에 적용할 수 있는 확장성 추구 방법은 **클래스 상속, 오브젝트 합성**
> - 패턴을 적용할 상황, 해결해야 할 문제, 솔루션의 구조와 각 요소의 역할과 핵심 의도가 무엇인지 기억해 두기

**상속의** **한계점**

- 이미 다른 목적을 위해 상속하고 있을 시 다중상속 불가
- 상속을 통한 상하위 클래스의 관계는 too much 밀접함
- 상속 코드가 사용된 클래스들이 계속 만들어진다면  상속을 통해 만들어진 코드가 매 클래스마다 중복돼서 나타나는 문제가 발생함

# 1.3 DAO의 확장

## 1.3.1 클래스의 분리

- 완전히 독립적인 클래스로 분리
- UserDao의 코드가 SimpleConnectionMaker라는 특정 클래스에 종속되어 있기 때문에 상속을 사용했을 때 처럼 UserDao 코드의 수정 없이 DB 커넥션 생성 기능을 변경할 방법이 없다.
- 문제점
    1. SimpleConnectionMaker의 메소드
        - 다른 클래스의 메소드명이 다르다면 커넥션을 가져오는 코드를 일일이 변경해야 하는 문제
    2. DB 커넥션을 제공하는 클래스가 어떤 것인지 UserDao가 구체적으로 알고 있어야 한다는 점
        - UserDao에 SimpleConnectionMaker라는 클래스 타입의 인스턴스 변수까지 정의해놓아서, 다른 클래스를 구현하면 UserDao 자체를 수정해야 함
        - ⇒ UserDao가 바뀔 수 있는 정보, 즉 DB 커넥션을 가져오는 클래스에 대해 너무 많이 알고 있기 때문 (그 클래스에서 커넥션을 가져오는 메소드는 이름이 뭔지까지 일일이 알고 있어야 한다.)
        - =⇒ 종속적!!!

## 1.3.2 인터페이스의 도입

- 추상화 : 공통적인 성격을 뽑아내어 따로 분리해내는 작업 ⇒ 인터페이스
- 자신을 구현한 클래스에 대한 구체적인 정보는 모두 감추기 때문에 접근하는 쪽에서 오브젝트를 만들 때 사용할 클래스를 몰라도 됨
- 기능만 정의, 구현방법은 나타나 있지 않음
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/96bdfa42-4aae-4568-b7fc-8675489e2c2d/Untitled.png)
    

## 1.3.3 관계설정 책임의 분리*

*1.3.2 인터페이스의 도입에서 클래스 이름(DconnectionMaker) 등장 때문에 문제가 발생했는데 1.3.3에서도 마찬가지로 클래스 이름이 등장했지만 가능한 이유..???

- UserDao와 / UserDao가 사용할 ConnectionMaker의 특정 구현 클래스 사이의 관계를 설정해주는 것에 관한 관심 (=DConnectionMaker, NConnectionMaker)
- 사용되는 오브젝트 ⇒ 서비스 / 사용하는 오브젝트 ⇒ 클라이언트
- 클라이언트 오브젝트에 기능 분리
    - 오브젝트와 오브젝트 사이의 관계를 설정해줘야 함 - ??????????
        - 직접 생성자를 호출해서 직접 오브젝트를 만드는 방법
        - 외부에서 만들어준 것을 가져오는 방법
            
            ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/50f3b4bc-c312-4661-ab5c-d6e5d79d2b2c/Untitled.png)
            

```java
package main.springbook.user.dao;

import java.sql.SQLException;

/**
 * 1.3.3 관계설정 책임의 분리
 * - 관계설정 책임이 추가된 메소드
 * - 클라이언트 코드
 * - UserDao의 변경 없이 다른 사용자가 자유로운 DB 접속 클래스를 만들어 사용 가능
 */
public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		// UserDao가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트 만들기
		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		// 1. UserDao 생성
		// 2. 사용할 ConnectionMaker 타입의 오브젝트 제공, 결국 두 오브젝트 사이의 의존관계 설정 효과
		// *UserDao 클래스에서 생성자 수정함 public UserDao(ConnectionMaker connectionMaker){ - }
		UserDao dao = new UserDao(connectionMaker);
	}

}
```

- 상속을 사용해 분리했을 때보다 훨씬 유연함
    - ConnectionMaker라는 인터페이스를 사용하기만 하면 다른 DAO클래스에도 ConnectionMaker의 구현 클래스들을 적용 가능
    - DAO가 많아져도 DB 접속 방법에 대한 관심은 오직 한 군데에 집중됨 !!!
    - 서로 영향을 주지 않고 필요에 따라 자유롭게 확장 가능
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c3b58338-ec11-4956-a20e-4d5b36a1156a/Untitled.png)
    

## 1.3.4 원칙과 패턴

- 초난감 DAO 코드의 개선 결과를 객체지향 기술의 이론을 통해 설명

### 개방 폐쇄 원칙

- '클래스나 모듈은 확장에는 열려 있어야 하고 변경에는 닫혀 있어야 한다'
- [그림 1-7] 인터페이스를 통해 제공되는 확장 포인트는 개방, 인터페이스를 이용하는 클래스는 불필요한 변화가 일어나지 않도록 폐쇄되어있음

<aside>
💡 디자인 패턴 ⇒ 특별한 상황에서 발생하는 문제에 대한 좀 더 구체적인 솔루션
객체지향 설계 원칙 ⇒ 좀 더 일반적인 상황에서 적용 가능한 설계 기준

</aside>

<aside>
💡 SOLID  5가지 객체지향 설계 원칙
- SRP(The Single Responsibility Principle) : 단일 책임 원칙
- OCP(The Open Closed Principle) : 개방 폐쇄 원칙
- LSP(The Liskov Substitution Principle) : 리스코프 치환 원칙
- ISP(The Interface Segregation Principle) : 인터페이스 분리 원칙
- DIP(The Dependency Inversion Principle) : 의존관계 역전 원칙

</aside>

### 높은 응집도와 낮은 결합도

- **높은 응집도** : 하나의 모듈, 클래스가 하나의 책임 또는 관심사에만 집중되어 있음
- **낮은 결합도** : 책임과 관심사가 다른 오브젝트 또는 모듈과 느슨하게 연결된 형태를 유지하는 것 / 하나의 오브젝트가 변경이 일어날 때에 관계를 맺고 있는 다른 오브젝트에게 변화를 요구하는 정도
    - 서로 독립적이며 알 필요도 없음
    - 변화에 대응하는 속도가 높아짐
    - 구성이 깔끔해짐
    - 확장이 편리함

### 전략패턴

- 자신의 기능 맥락에서, 필요에 따라 변경이 필요한 알고리즘을 **인터페이스를 통해 통째로 외부로 분리**시키고,
이를 구현한 구체적인 알고리즘 **클래스를 필요에 따라 바꿔서 사용**할 수 있게 하는 디자인 패턴
*알고리즘 : 독립적인 책임으로 분리가 가능한 기능

# 1.4 제어의 역전(IoC Inversuin of Control)

## 1.4.1 오브젝트 팩토리

- 클라이언트 UserDaoTest
    - 성격이 다른 책임이나 관심사는 분리 !!

### 팩토리

1. 분리시킬 기능을 담당할 클래스를 하나 만든다.
2. 이 클래스의 역할은 객체의 생성 방법을 결정하고 그렇게 만들어진 오브젝트를 돌려주는 것인데, 이런 일을 하는 오브젝트를 **팩토리**라고 부른다.

### 설계도로서의 팩토리

- UserDao, ConnectionMaker
    - 각각 애플리케이션의 핵심적인 데이터 로직과 기술 로직 담당
    - 실질적인 로직을 담당하는 **컴포넌트**
- DaoFactory
    - 이러한 애플리케이션의 오브젝트들을 구성하고 그 관계를 정의하는 책임을 맡고 있음
    - 애플리케이션을 구성하는 컴포넌트의 구조와 관계를 정의한 **설계도** 같은 역할(어떤 오브젝트 → 어떤 오브젝트를 사용?! 하는지를 정의해놓은 코드)
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0cfcaac6-69cb-4b55-a85f-053f9e1c944c/Untitled.png)
    

## 1.4.2 오브젝트 팩토리의 활용

- DaoFactory에 UserDao가 아닌 다른 DAO의 생성 기능을 넣으면 어떻게 될까?
⇒ ConnectionMaker 구현 클래스의 **오브젝트를 생성하는 코드가 메소드마다 반복**되는 문제
⇒ 분리

```java
public class DaoFactory{
	public UserDao userDao(){
		return new UserDao(new DConnectionMaker());
	}
	
	public AccountDao accountDao(){
		return new AccountDao(new DConnectionMaker());
	}

	public MessageDao messageDao(){
		return new MessageDao(new DConnectionMaker());
	}
	// new DConnectionMaker() => ConnectionMaker 구현 클래스를 선정하고 생성하는 코드의 중복 !!
}
```

```java
public class DaoFactory{
	public UserDao userDao(){
		return new UserDao(connectionMaker());
	}
	
	public AccountDao accountDao(){
		return new AccountDao(connectionMaker());
	}

	public MessageDao messageDao(){
		return new MessageDao(connectionMaker());
	}
	
	public ConnectionMaker connectionMaker(){
		return new DConnectionMaker();
		// 분리해서 중복을 제거한 ConnectionMaker 타입 오브젝트 생성 코드
		// 구현 클래스를 변경하고 싶을 때도 여기만 수정하면 됨
	}
}
```

## 1.4.3 제어권의 이전을 통한 제어관계 역전

- **제어의 역전**이란?
    - 프로그램의 제어 흐름 구조가 뒤바뀌는 것
        - 모든 제어 권한(결정, 생성)을 다른 대상에게 위임함
        - 예시
            1. 서블릿 : 서블릿에 대한 제어 권한을 가진 컨테이너가 적절한 시점에 클래스 오브젝트를 만들고 그 안의 메소드를 호출
            2. 템플릿 메소드 패턴 : 제어권을 상위 템플릿 메소드에 넘기고 자신은 필요할 때 호출되어 사용되도록 함
            3. 프레임워크 : 애플리케이션 코드는 프레임워크가 짜놓은 틀에서 수동적으로 동작
            4. 책에서 DaoFactory를 도입했던 과정 (ConnectionMaker의 구현 클래스를 결정하고 오브젝트를 만드는 제어권을 넘김)
            

# 1.5 스프링의 IoC

스프링의 핵심

- 빈 팩토리 / 애플리케이션 컨텍스트

## 1.5.1 오브젝트 팩토리를 이용한 스프링 IoC

### 애플리케이션 컨텍스트와 설정정보