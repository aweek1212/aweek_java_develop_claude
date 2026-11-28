# IntelliJ IDEA 단축키·약어 참고표 (macOS 기본 키맵)

힌트에 붙일 팁은 이 표에서 고른다. 표에 없는 단축키는 확실할 때만 쓰고, 그때도 "안 되면 `Cmd+Shift+A`에서 '기능 이름' 검색"을 덧붙인다.

## 목차
0. 읽는 법과 주의할 점
1. 지금 바로 쓸 수 있는 것 (기본편 1단계부터)
   - 1-1 약어 (라이브 템플릿)
   - 1-2 포스트픽스 완성
   - 1-3 코드 생성 (`Cmd+N`)
   - 1-4 편집
   - 1-5 이동·탐색
   - 1-6 실행·디버그
   - 1-7 리팩토링
   - 1-8 커밋
2. 해당 개념을 배운 뒤에 소개할 것 (진도별)

---

## 0. 읽는 법과 주의할 점

- 키 이름: `Cmd`(⌘), `Option`(⌥), `Ctrl`(⌃), `Shift`(⇧), `Enter`(↩), `Delete`(⌫, 맥의 백스페이스)
- **약어와 포스트픽스는 영문 입력 상태에서만** 동작한다. 한글 상태로 `sout`를 치면 `ㄴㅐㅕㅅ`가 된다. 처음 알려 줄 때 한 번 언급한다.
- **맥북의 F키**: `Shift+F6`처럼 F키가 들어간 단축키는 `fn`을 같이 눌러야 할 수 있다 (`fn+Shift+F6`).
  시스템 설정 → 키보드 → 키보드 단축키 → 기능 키 → "F1, F2 등의 키를 표준 기능 키로 사용"을 켜면 `fn` 없이 된다. F키 단축키를 처음 알려 줄 때 한 번 언급한다.
- 키맵을 바꿨거나 단축키가 다른 앱과 겹치면 안 될 수 있다. 그럴 땐 `Cmd+Shift+A`(Find Action)에서 기능 이름을 검색하면 실행도 되고 지정된 단축키도 보인다.
- 약어 목록: `Settings(Cmd+,) → Editor → Live Templates → Java`
  포스트픽스 목록: `Settings → Editor → General → Postfix Completion`
  지금 위치에서 쓸 수 있는 약어 보기: `Cmd+J`

---

## 1. 지금 바로 쓸 수 있는 것

### 1-1. 약어 (라이브 템플릿): 입력 후 `Tab`

| 입력 | 펼쳐지는 코드 | 쓰는 곳 예시 |
|---|---|---|
| `psvm` 또는 `main` | `public static void main(String[] args) { }` | 확인 코드, `ShopMain` |
| `sout` | `System.out.println();` | 실패 메시지 출력 |
| `soutv` | `System.out.println("변수 = " + 변수);` (가까운 변수 자동 선택) | 값 확인용 디버깅 출력 |
| `soutm` | `System.out.println("클래스.메서드");` | 어떤 메서드가 불렸는지 확인 |
| `soutp` | 메서드 매개변수를 모두 출력 | 매개변수 값 확인 |
| `fori` | `for (int i = 0; i < ; i++) { }` (커서가 조건 자리로) | 저장소의 `i < size` 반복 |
| `itar` | `for (int i = 0; i < arr.length; i++) { T t = arr[i]; }` | 배열 전체를 인덱스로 돌 때 |
| `iter` | `for (T t : 배열) { }` | 향상된 for문 |
| `ifn` | `if (변수 == null) { }` | 조회 결과 `null` 검사 |
| `inn` | `if (변수 != null) { }` | 중복 검사 |

### 1-2. 포스트픽스 완성: 식 뒤에 `.이름` 입력 후 `Tab`

먼저 값을 쓰고 나중에 감싸는 방식이라 "생각나는 순서대로" 칠 수 있다.

| 입력 예 | 결과 |
|---|---|
| `new Item("볼펜", 1000, 3).var` | `Item item = new Item("볼펜", 1000, 3);` (변수 이름 추천) |
| `itemRepository.findById(itemId).var` | `Item byId = itemRepository.findById(itemId);` → 이름을 바로 고칠 수 있음 |
| `item.null` | `if (item == null) { }` |
| `member.nn` | `if (member != null) { }` |
| `item.isSoldOut().if` | `if (item.isSoldOut()) { }` |
| `item.addStock(quantity).not` | `!item.addStock(quantity)` |
| `size.fori` | `for (int i = 0; i < size; i++) { }` |
| `items.for` | `for (Item item : items) { }` |
| `result.return` | `return result;` |
| `"[실패] 메시지".sout` | `System.out.println("[실패] 메시지");` |

### 1-3. 코드 생성: 에디터에서 `Cmd+N` (Generate)

| 메뉴 | 하는 일 | 주의 |
|---|---|---|
| Constructor | 고른 필드를 받는 생성자 | `id`처럼 생성자에서 받지 않을 필드는 선택에서 빼기 |
| Getter | 고른 필드의 getter | 대화상자에서 `Cmd+A`로 전체 선택, `Cmd+클릭`으로 골라 선택 |
| Setter | 고른 필드의 setter | **필요한 필드만** 고르기 (예: 미션 2는 `id`만) |
| Getter and Setter | 둘 다 | 캡슐화 규칙을 깨기 쉬우니 미션 규칙 확인 후 권하기 |

- 프로젝트 창(왼쪽 파일 목록)에서 `Cmd+N`을 누르면 새 클래스·패키지 만들기 메뉴가 뜬다.
- `Ctrl+Enter`도 에디터에서 Generate를 연다.

### 1-4. 편집

| 단축키 | 기능 |
|---|---|
| `Option+Enter` | 빨간 줄·노란 줄에서 해결 방법 보기 (import 추가, 없는 메서드·필드 만들기 등) |
| `Cmd+Option+L` | 코드 정렬 (들여쓰기·띄어쓰기 맞추기) |
| `Ctrl+Option+O` | 안 쓰는 import 지우기 |
| `Ctrl+Space` | 자동 완성 목록 다시 띄우기 |
| `Cmd+P` | 지금 호출 중인 메서드의 매개변수 목록 보기 |
| `Cmd+Shift+Enter` | 문장 마무리 (괄호·세미콜론·중괄호 자동 완성) |
| `Cmd+D` | 현재 줄 복제 |
| `Cmd+Delete` | 현재 줄 삭제 |
| `Cmd+/` | 줄 주석 켜기·끄기 |
| `Cmd+Option+/` | 블록 주석 켜기·끄기 |
| `Shift+Cmd+Up` / `Down` | 문장(블록)을 위·아래로 이동 |
| `Option+Up` / `Down` | 선택 범위 넓히기·줄이기 |
| `Shift+Enter` | 커서가 줄 중간에 있어도 아래에 새 줄 |
| `Cmd+J` | 지금 쓸 수 있는 약어 목록 |

### 1-5. 이동·탐색

| 단축키 | 기능 |
|---|---|
| `Shift` 두 번 | 어디서나 검색 (클래스·파일·기능) |
| `Cmd+Shift+A` | 기능 이름으로 검색해서 실행 (단축키를 잊었을 때) |
| `Cmd+O` | 클래스 이름으로 열기 |
| `Cmd+Shift+O` | 파일 이름으로 열기 |
| `Cmd+E` | 최근 파일 |
| `Cmd+B` 또는 `Cmd+클릭` | 선언으로 이동 (메서드·클래스 정의 보기) |
| `Option+F7` | 이 메서드·필드를 쓰는 곳 찾기 |
| `Cmd+Shift+F7` | 현재 파일에서 사용 위치 강조 |
| `Cmd+F12` | 파일 구조 (필드·메서드 목록) |
| `F2` / `Shift+F2` | 다음·이전 에러로 이동 |
| `Cmd+F1` | 커서 위치의 에러 설명 보기 |
| `F1` | 빠른 문서 보기 (예: `String.equals` 설명) |
| `Cmd+L` | 줄 번호로 이동 |
| `Cmd+[` / `Cmd+]` | 이전·다음 위치로 돌아가기 |
| `Cmd+1` | 프로젝트 창 열고 닫기 |
| `Option+F12` | 터미널 창 |
| `Cmd+Shift+F12` | 모든 도구 창 숨기기 (코드만 크게) |

### 1-6. 실행·디버그

| 단축키 | 기능 |
|---|---|
| `Ctrl+Shift+R` | **지금 열린 파일의 `main` 실행** (확인 코드 실행에 제일 많이 씀) |
| `Ctrl+R` | 마지막으로 실행한 것 다시 실행 |
| `Ctrl+Shift+D` | 지금 열린 파일을 디버그 모드로 실행 |
| `Cmd+F8` | 중단점(브레이크포인트) 켜기·끄기 (줄 번호 옆 클릭도 같음) |
| `F8` | 디버그 중 다음 줄로 (메서드 안으로 안 들어감) |
| `F7` | 디버그 중 메서드 안으로 들어가기 |
| `Option+Cmd+R` | 다음 중단점까지 계속 실행 |
| `Option+F8` | 디버그 중 식 계산해 보기 |

디버거는 자바 문법이 아니라 도구라서 진도와 상관없이 소개해도 된다. 특히 참조형(섹션 3)을 이해할 때 좋다. 디버그 창에서 변수 옆 `{Item@123}` 같은 번호가 같으면 같은 객체다.

### 1-7. 리팩토링

| 단축키 | 기능 |
|---|---|
| `Shift+F6` | 이름 바꾸기 (쓰는 곳 전부 같이 바뀜) |
| `Cmd+Option+V` | 식을 변수로 뽑기 |
| `Cmd+Option+M` | 선택한 코드를 메서드로 뽑기 |
| `Cmd+Option+F` | 식을 필드로 뽑기 |
| `Cmd+Option+P` | 식을 매개변수로 뽑기 |
| `Cmd+Option+N` | 변수·메서드를 쓰는 곳에 펼쳐 넣기 (뽑기의 반대) |
| `Cmd+F6` | 메서드 시그니처(매개변수) 바꾸기 |
| `Ctrl+T` | 리팩토링 메뉴 전체 |

### 1-8. 커밋

| 단축키 | 기능 |
|---|---|
| `Cmd+K` | 커밋 창 |
| `Cmd+Shift+K` | 푸시 |

---

## 2. 해당 개념을 배운 뒤에 소개할 것

README "진행 현황"을 보고, 아래 진도에 도달하기 전에는 소개하지 않는다. 도구가 만들어 주는 코드에 아직 안 배운 문법이 들어 있기 때문이다.

| 필요한 진도 | 단축키·약어 | 이유 |
|---|---|---|
| 기본편 섹션 9 final | `psf`(`public static final`), `psfi`(`… int`), `psfs`(`… String`), `prsf`(`private static final`), `Cmd+Option+C`(상수로 뽑기) | `final` 키워드를 만들어 냄 |
| 기본편 섹션 10 상속 | `Ctrl+O`(메서드 오버라이드), `Cmd+U`(부모 메서드로 이동), `Ctrl+H`(상속 계층 보기), `Cmd+N → Override Methods` | 상속·`@Override` |
| 기본편 섹션 11~12 다형성 | `Ctrl+I`(인터페이스 메서드 구현), `Option+Cmd+B`(구현 클래스로 이동), 포스트픽스 `.cast`, `.inst`(`instanceof`) | 인터페이스·캐스팅 |
| 중급 1편 Object | `Cmd+N → toString()`, `Cmd+N → equals() and hashCode()` | `Object` 메서드 오버라이딩 |
| 중급 1편 예외 | `Cmd+Option+T → try/catch`(감싸기), 포스트픽스 `.try`, `.throw`, 약어 `thr` | 예외 처리 문법 |
| 중급 2편 컬렉션 | 포스트픽스 `.for`·`iter`를 `List`에 사용 | 제네릭 타입이 나옴 |
| 고급 3편 람다·스트림 | `Option+Enter → Replace with lambda / method reference` 같은 제안 | 람다 문법 |
| 9단계 JUnit | `Cmd+Shift+T`(테스트 만들기·테스트로 이동), `Ctrl+Shift+R`로 테스트 실행 | JUnit |
