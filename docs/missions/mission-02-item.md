# 미션 2. 상품 도메인 만들기

> **필요한 진도**: 실전 자바 기본편 섹션 2 ~ 섹션 8 `56. static 메서드1`
> **이전 미션**: [미션 1. 회원 도메인](mission-01-member.md) · **다음 미션**: 미션 3. 주문

## 목표

쇼핑몰에서 팔 **상품을 등록하고, 재고를 늘리고 줄이는** 기능을 만듭니다.
미션 1과 뼈대(도메인 → 저장소 → 서비스)는 같지만, 이번에는 **도메인 객체가 스스로 규칙을 지키게** 만드는 것이 핵심이에요.

> 쇼핑몰의 규칙: **재고는 절대 음수가 될 수 없다.**

## 이번 미션에서 쓰는 강의 개념

| 개념 | 강의 | 어디에 쓰나 |
|---|---|---|
| 클래스와 객체 | 섹션 2 | `Item`: 상품 하나의 데이터(이름, 가격, 재고)를 묶는 클래스 |
| 참조형 | 섹션 3 | 서비스에 상품 **번호**만 넘겨도, 밖에서 들고 있던 상품 객체의 재고가 바뀜 |
| 객체 지향 프로그래밍 | 섹션 4 | 재고를 늘리고 줄이는 **기능을 `Item` 안에** 넣기 |
| 생성자, `this()` | 섹션 5 | `Item` 생성자, `ItemRepository` 생성자 오버로딩 |
| 패키지 | 섹션 6 | 미션 1에서 만든 계층 패키지에 상품 클래스 추가 |
| 접근 제어자, 캡슐화 | 섹션 7 | 재고를 직접 바꾸는 `setStockQuantity()`를 **만들지 않기** |
| static 변수 | 섹션 8 | 상품 번호 `sequence`: 회원 번호와는 **따로** 매겨짐 |
| static 메서드 | 섹션 8 | `StringUtil.isBlank()` 재사용, 확인 코드의 `static` 출력 메서드 |

## 이번 미션의 핵심: 데이터를 꺼내서 고치지 말고, 객체에게 시키기

섹션 4에서 절차 지향과 객체 지향을 비교했던 것을 떠올려 보세요. 재고를 3개 빼는 코드를 두 가지로 쓸 수 있어요.

```java
// 1) 밖에서 데이터를 꺼내 계산하고 다시 넣는 방식
if (item.getStockQuantity() >= 3) {
    item.setStockQuantity(item.getStockQuantity() - 3);
}

// 2) 규칙을 아는 객체에게 시키는 방식
item.removeStock(3);
```

1번 방식은 재고를 빼는 **모든 곳**(서비스, 주문, 관리자 화면…)에서 검사를 잊지 말아야 해요. 한 곳만 깜빡해도 재고가 음수가 됩니다.
2번 방식은 검사가 `Item` 안에 **딱 한 번** 있어요. 게다가 `setStockQuantity()`가 아예 없으니 밖에서 규칙을 깰 방법이 없죠. 이것이 캡슐화(섹션 7)의 진짜 목적입니다.

이번 미션은 2번 방식으로 만듭니다.

```
ItemCheck (확인용 main)
   │
   ▼
ItemService     (서비스)  ← 입력 검사, 상품 찾기, 실패 메시지         → 스프링의 @Service
   │
   ▼
ItemRepository  (저장소)  ← 저장·조회만. 미션 1의 MemberRepository와 거의 같음 → @Repository
   │
   ▼
Item            (도메인)  ← 데이터 + "재고는 음수가 될 수 없다"는 규칙 → JPA의 @Entity
```

> 실제로 스프링 + JPA 실무에서도 `item.removeStock(quantity)`처럼 **도메인 객체에 비즈니스 메서드를 두는** 방식을 많이 씁니다.
> 스프링 강의에서 같은 이름의 메서드를 다시 만나게 될 거예요.

## 만들 파일

```
src/shop/
├── domain/Item.java               ← 새로 만들기
├── repository/ItemRepository.java ← 새로 만들기
├── service/ItemService.java       ← 새로 만들기
├── util/StringUtil.java           (미션 1에서 만든 것을 그대로 재사용)
└── check/ItemCheck.java           ← 마지막에 아래 코드를 붙여넣기
```

## 요구사항

### 1. `shop.domain.Item`

- 필드: 상품 번호 `id` (`long`), 이름 `name`, 가격 `price` (`int`), 재고 수량 `stockQuantity` (`int`). **모두 `private`**
- 생성자 `Item(String name, int price, int stockQuantity)`: `id`는 미션 1처럼 저장소가 정해 준다.
- 조회 메서드: `getId()`, `getName()`, `getPrice()`, `getStockQuantity()`, 그리고 저장소가 쓸 `setId(long id)`
- **`setStockQuantity()`, `setPrice()`는 만들지 않는다.** 재고는 아래 메서드로만 바꿀 수 있다.

| 메서드 | 동작 |
|---|---|
| `boolean addStock(int quantity)` | `quantity`가 0 이하면 아무것도 바꾸지 않고 `false`. 아니면 재고를 늘리고 `true` |
| `boolean removeStock(int quantity)` | `quantity`가 0 이하이거나 **재고보다 많으면** 아무것도 바꾸지 않고 `false`. 아니면 재고를 줄이고 `true` |
| `boolean isSoldOut()` | 재고가 0이면 `true` (품절) |

> 지금은 실패를 `false`로 알리지만, 중급 1편에서 **예외**를 배우면 "재고 부족 예외"를 던지도록 바꿀 거예요.

### 2. `shop.repository.ItemRepository`

미션 1의 `MemberRepository`와 **같은 구조**입니다. 복사해서 `Member`를 `Item`으로 바꾸는 것부터 시작해도 괜찮아요.

- 필드: 상품 번호를 만드는 **static** `sequence` (0부터), `Item[]` 배열, 저장한 개수 `size`
- 생성자: `ItemRepository(int capacity)`, 그리고 기본 크기 10으로 `this(...)`를 호출하는 `ItemRepository()`
- 메서드: `Item save(Item item)`, `Item findById(long id)`, `Item[] findAll()`, `int count()` (동작은 미션 1과 같음)

> 두 저장소가 타입만 다르고 코드가 거의 똑같다는 점을 기억해 두세요.
> 중급 2편에서 **제네릭**을 배우면 이런 중복을 없애는 방법을 알게 됩니다.

### 3. `shop.service.ItemService`

- 필드: `ItemRepository itemRepository` (`private`), 생성자로 **밖에서 받는다** (미션 1과 같은 생성자 주입)
- 메서드

| 메서드 | 동작 |
|---|---|
| `Item registerItem(String name, int price, int stockQuantity)` | 아래 규칙을 순서대로 검사. 실패하면 메시지를 출력하고 `null`, 성공하면 저장된 상품 반환 |
| `Item findItem(long itemId)` | 상품 한 개 조회. 없으면 `null` |
| `Item[] findItems()` | 전체 상품 조회 |
| `Item[] findItemsOnSale()` | **품절이 아닌 상품만** 담은 배열 반환 (`isSoldOut()` 사용) |
| `boolean addStock(long itemId, int quantity)` | 상품을 찾아 재고를 늘린다(입고). 성공하면 `true` |
| `boolean removeStock(long itemId, int quantity)` | 상품을 찾아 재고를 줄인다(출고). 성공하면 `true` |

`registerItem` 검사 규칙과 실패 메시지:

1. 이름이 비어 있으면 (`StringUtil.isBlank` 사용) → `[등록 실패] 상품 이름은 필수입니다.`
2. 가격이 0 이하면 → `[등록 실패] 가격은 0원보다 커야 합니다.`
3. 재고가 음수면 → `[등록 실패] 재고는 0개 이상이어야 합니다.` (재고 0개 등록은 가능: 입고 예정 상품)
4. 저장소가 가득 차서 저장에 실패하면 → `[등록 실패] 저장 공간이 부족합니다.`

`addStock` / `removeStock` 실패 메시지 (실패하면 출력하고 `false` 반환):

| 상황 | 메시지 |
|---|---|
| 입고할 상품이 없음 | `[입고 실패] 없는 상품입니다. id=99` |
| `item.addStock(...)`이 `false` | `[입고 실패] 수량은 1개 이상이어야 합니다.` |
| 출고할 상품이 없음 | `[출고 실패] 없는 상품입니다. id=99` |
| `item.removeStock(...)`이 `false` | `[출고 실패] 재고가 부족합니다. 현재 재고=3, 요청 수량=4` |

**재고를 바꾸는 계산은 서비스에서 하지 말고 `Item`의 메서드에 맡기세요.** 서비스는 "상품을 찾고, 시키고, 결과에 따라 메시지를 출력"만 합니다.

<details>
<summary>힌트: <code>removeStock</code>에서 검사할 것은 두 가지</summary>

1. `quantity`가 0 이하인가? → 음수를 빼면 오히려 재고가 **늘어나** 버려요. (`3 - (-2) = 5`)
2. 지금 재고보다 많이 빼려고 하는가?

둘 중 하나라도 해당하면 재고를 건드리지 말고 바로 `return false;` 하세요.
재고를 줄이는 코드는 두 검사를 모두 통과한 **뒤에** 와야 합니다.
</details>

<details>
<summary>힌트: 서비스의 <code>addStock</code>은 어떤 순서로?</summary>

1. `itemRepository.findById(itemId)`로 상품을 찾는다.
2. `null`이면 메시지 출력 후 `return false;`
3. 찾은 상품에게 `item.addStock(quantity)`를 **시킨다.** 결과가 `false`면 메시지 출력 후 `return false;`
4. 여기까지 왔으면 성공이니 `return true;`

boolean 결과를 뒤집으려면 `!`를 써요. `if (!item.addStock(quantity))`는 "재고 추가에 실패했다면"이라는 뜻입니다.
`addStock`은 조건문 안에서 호출되는 것만으로 실제로 재고를 바꾼다는 점도 기억하세요.
</details>

<details>
<summary>힌트: <code>findItemsOnSale()</code>의 배열 크기는 어떻게 정할까?</summary>

배열은 만들 때 크기를 정해야 하는데, 품절 아닌 상품이 몇 개인지는 세어 보기 전엔 몰라요. 그래서 **반복문을 두 번** 돕니다.

1. 첫 번째 반복: 품절이 아닌 상품 개수를 센다.
2. 그 개수로 `new Item[개수]`를 만든다.
3. 두 번째 반복: 품절이 아닌 상품을 담는다. 이때 **새 배열에 넣을 위치를 따로 세는 변수**가 필요해요. 원래 배열의 `i`를 그대로 쓰면 어떻게 될지 생각해 보세요.

> 중급 2편에서 `ArrayList`를 배우면 크기를 미리 몰라도 하나씩 추가할 수 있어서 반복문 한 번으로 끝나요.
</details>

## 완성 확인

`src/shop/check/ItemCheck.java`를 만들고 아래 코드를 그대로 붙여넣은 뒤 실행하세요.
7번에서 미션 1의 회원 클래스도 같이 사용합니다.

```java
package shop.check;

import shop.domain.Item;
import shop.domain.Member;
import shop.repository.ItemRepository;
import shop.repository.MemberRepository;
import shop.service.ItemService;
import shop.service.MemberService;

public class ItemCheck {

    public static void main(String[] args) {
        ItemService itemService = new ItemService(new ItemRepository());

        System.out.println("== 1. 상품 등록 ==");
        Item book = itemService.registerItem("자바 책", 30000, 10);
        Item album = itemService.registerItem("스프링 앨범", 15000, 0);
        Item movie = itemService.registerItem("JPA 영화", 12000, 3);
        printItem(book);
        printItem(album);
        printItem(movie);

        System.out.println("== 2. 잘못된 상품 등록 ==");
        System.out.println("이름 빈칸 = " + itemService.registerItem("", 1000, 1));
        System.out.println("가격 0원 = " + itemService.registerItem("공짜 상품", 0, 1));
        System.out.println("재고 음수 = " + itemService.registerItem("유령 상품", 1000, -5));

        System.out.println("== 3. Item이 스스로 재고 규칙 지키기 ==");
        Item pen = new Item("볼펜", 1000, 3);
        System.out.println("5개 빼기 = " + pen.removeStock(5) + ", 재고=" + pen.getStockQuantity());
        System.out.println("-2개 빼기 = " + pen.removeStock(-2) + ", 재고=" + pen.getStockQuantity());
        System.out.println("-2개 더하기 = " + pen.addStock(-2) + ", 재고=" + pen.getStockQuantity());
        System.out.println("3개 빼기 = " + pen.removeStock(3) + ", 재고=" + pen.getStockQuantity());
        System.out.println("품절인가? " + pen.isSoldOut());
        System.out.println("저장하지 않은 볼펜 id = " + pen.getId());

        System.out.println("== 4. 서비스로 입고·출고 ==");
        System.out.println("자바 책 5개 입고 = " + itemService.addStock(book.getId(), 5));
        System.out.println("자바 책 3개 출고 = " + itemService.removeStock(book.getId(), 3));
        System.out.println("book 변수로 본 재고 = " + book.getStockQuantity());
        System.out.println("JPA 영화 4개 출고 = " + itemService.removeStock(movie.getId(), 4));
        System.out.println("JPA 영화 재고 = " + movie.getStockQuantity());
        System.out.println("0개 입고 = " + itemService.addStock(book.getId(), 0));
        System.out.println("없는 상품 입고 = " + itemService.addStock(99, 1));
        System.out.println("없는 상품 출고 = " + itemService.removeStock(99, 1));

        System.out.println("== 5. 판매 중인 상품만 보기 ==");
        System.out.println("전체 상품 수 = " + itemService.findItems().length);
        Item[] onSale = itemService.findItemsOnSale();
        System.out.println("판매 중인 상품 수 = " + onSale.length);
        for (Item item : onSale) {
            printItem(item);
        }

        System.out.println("== 6. 저장 공간이 가득 찼을 때 ==");
        ItemService smallService = new ItemService(new ItemRepository(1));
        Item bag = smallService.registerItem("가방", 50000, 1);
        System.out.println("두 번째 등록 = " + smallService.registerItem("모자", 20000, 1));

        System.out.println("== 7. 회원 번호와 상품 번호 ==");
        MemberService memberService = new MemberService(new MemberRepository());
        Member member = memberService.join("김자바", "java@shop.com");
        System.out.println("첫 회원 id = " + member.getId() + ", 가방 id = " + bag.getId());
    }

    static void printItem(Item item) {
        System.out.println("id=" + item.getId() + ", name=" + item.getName()
                + ", price=" + item.getPrice() + ", stock=" + item.getStockQuantity());
    }
}
```

터미널에서는 저장소 루트에서 이렇게 실행해요.

```bash
java src/shop/check/ItemCheck.java
```

**예상 출력**

```
== 1. 상품 등록 ==
id=1, name=자바 책, price=30000, stock=10
id=2, name=스프링 앨범, price=15000, stock=0
id=3, name=JPA 영화, price=12000, stock=3
== 2. 잘못된 상품 등록 ==
[등록 실패] 상품 이름은 필수입니다.
이름 빈칸 = null
[등록 실패] 가격은 0원보다 커야 합니다.
가격 0원 = null
[등록 실패] 재고는 0개 이상이어야 합니다.
재고 음수 = null
== 3. Item이 스스로 재고 규칙 지키기 ==
5개 빼기 = false, 재고=3
-2개 빼기 = false, 재고=3
-2개 더하기 = false, 재고=3
3개 빼기 = true, 재고=0
품절인가? true
저장하지 않은 볼펜 id = 0
== 4. 서비스로 입고·출고 ==
자바 책 5개 입고 = true
자바 책 3개 출고 = true
book 변수로 본 재고 = 12
[출고 실패] 재고가 부족합니다. 현재 재고=3, 요청 수량=4
JPA 영화 4개 출고 = false
JPA 영화 재고 = 3
[입고 실패] 수량은 1개 이상이어야 합니다.
0개 입고 = false
[입고 실패] 없는 상품입니다. id=99
없는 상품 입고 = false
[출고 실패] 없는 상품입니다. id=99
없는 상품 출고 = false
== 5. 판매 중인 상품만 보기 ==
전체 상품 수 = 3
판매 중인 상품 수 = 2
id=1, name=자바 책, price=30000, stock=12
id=3, name=JPA 영화, price=12000, stock=3
== 6. 저장 공간이 가득 찼을 때 ==
[등록 실패] 저장 공간이 부족합니다.
두 번째 등록 = null
== 7. 회원 번호와 상품 번호 ==
첫 회원 id = 1, 가방 id = 4
```

출력이 다르게 나오면 어느 번호에서 달라졌는지가 곧 힌트예요.
특히 3번의 `-2개 빼기`에서 `재고=5`가 나온다면 아래 질문 2를 보세요.

## 생각해 볼 질문

리뷰할 때 같이 이야기할 거예요. 답을 짧게라도 생각해 오세요.

1. **`Item`에 `setStockQuantity()`가 있었다면** 누가, 어떻게 "재고는 음수가 될 수 없다"는 규칙을 깰 수 있을까요? 지금 구조에서는 왜 깰 수 없을까요?
2. `removeStock`에서 **"0 이하인가?" 검사를 지우고** 3번을 다시 실행해 보세요. `-2개 빼기`의 결과와 재고는 어떻게 바뀌나요? 그 뒤 줄들도 어떻게 달라지나요?
3. 4번에서 서비스에는 `book.getId()`, 즉 **번호만** 넘겼어요. 그런데 왜 `book` 변수로 본 재고도 12로 바뀌어 있을까요? (섹션 3 참조형)
4. 가격 검사는 서비스(`registerItem`)에서 하는데, `new Item("유령 상품", -1000, -5)`처럼 **직접 `new` 하면** 막을 수 있을까요? 생성자에서 막으려면 무엇이 필요할까요?
5. `itemService.removeStock(movie.getId(), -1)`을 호출하면 어떤 메시지가 나올까요? **그 메시지는 정확한가요?** `boolean` 하나로 실패 이유를 여러 가지로 알릴 수 있을까요?
6. 7번에서 회원 번호는 1, 가방 번호는 4가 나왔어요. 두 클래스 모두 `sequence`라는 **같은 이름의 static 변수**를 쓰는데 왜 섞이지 않을까요? 볼펜(`pen`)의 번호는 왜 0일까요?
7. 확인 코드의 `printItem` 앞에 붙은 `static`을 지우면 어떻게 될까요? 왜 그럴까요? (섹션 8 static 메서드)

## 도전 과제 (선택)

- 질문 5의 메시지 문제 고치기: 서비스의 `removeStock`에서 수량이 0 이하면 `[출고 실패] 수량은 1개 이상이어야 합니다.`를 따로 출력
- `Item`에 가격을 바꾸는 `boolean changePrice(int newPrice)` 추가. 0원 이하로는 바꿀 수 없게 막기
- `ItemService`에 `Item[] findItemsUnderPrice(int maxPrice)` 추가: 가격이 `maxPrice` 이하인 상품만 반환 (`findItemsOnSale()`과 같은 방식)

## 다 했으면

Claude에게 **"미션 2 리뷰해줘"** 라고 요청하세요. 리뷰를 반영한 뒤 커밋하고 미션 3(주문)으로 넘어갑니다.
미션 3에서는 지금 만든 `removeStock()`과 `addStock()`을 **주문과 주문 취소**에서 그대로 쓰게 돼요.
