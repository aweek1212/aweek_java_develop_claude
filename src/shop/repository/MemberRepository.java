package shop.repository;

import shop.domain.Member;

public class MemberRepository {
    private static long sequence = 0;
    private Member[] store;
    private int size;

    public MemberRepository(int capacity) {
        store = new Member[capacity];
    }

    public MemberRepository() {
        this(10);
    }

    public Member save(Member member) {
        if (store.length == size) {
            return null;
        }

        sequence++;
        member.setId(sequence);

        store[size] = member;
        size++;

        return member;
    }
}
