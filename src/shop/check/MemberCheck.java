package shop.check;

import shop.domain.Member;
import shop.repository.MemberRepository;
import shop.service.MemberService;

public class MemberCheck {

    public static void main(String[] args) {
        MemberRepository memberRepository = new MemberRepository();
        MemberService memberService = new MemberService(memberRepository);

        System.out.println("== 1. 회원 가입 ==");
        Member memberA = memberService.join("김자바", "java@shop.com");
        Member memberB = memberService.join("이스프링", "spring@shop.com");
        System.out.println("memberA id=" + memberA.getId() + ", name=" + memberA.getName());
        System.out.println("memberB id=" + memberB.getId() + ", name=" + memberB.getName());

        System.out.println("== 2. 중복 이메일 가입 ==");
        Member duplicate = memberService.join("박중복", "java@shop.com");
        System.out.println("duplicate = " + duplicate);

        System.out.println("== 3. 잘못된 입력 ==");
        System.out.println("이름 빈칸 = " + memberService.join("", "blank@shop.com"));
        System.out.println("이메일 null = " + memberService.join("최널", null));

        System.out.println("== 4. 회원 조회 ==");
        Member findMember = memberService.findMember(memberA.getId());
        System.out.println("findMember name=" + findMember.getName());
        System.out.println("memberA와 같은 객체인가? " + (findMember == memberA));
        System.out.println("없는 회원 = " + memberService.findMember(999));

        System.out.println("== 5. 전체 조회 ==");
        Member[] members = memberService.findMembers();
        System.out.println("회원 수 = " + members.length);
        for (Member member : members) {
            System.out.println("id=" + member.getId() + ", name=" + member.getName() + ", email=" + member.getEmail());
        }

        System.out.println("== 6. 내부 배열 보호 ==");
        members[0] = null;
        System.out.println("밖에서 배열을 바꾼 뒤 첫 회원 = " + memberService.findMembers()[0].getName());

        System.out.println("== 7. 저장 공간이 가득 찼을 때 ==");
        MemberService smallService = new MemberService(new MemberRepository(2));
        Member first = smallService.join("회원1", "m1@shop.com");
        smallService.join("회원2", "m2@shop.com");
        System.out.println("세 번째 가입 = " + smallService.join("회원3", "m3@shop.com"));
        System.out.println("다른 저장소의 첫 회원 id = " + first.getId());
    }
}