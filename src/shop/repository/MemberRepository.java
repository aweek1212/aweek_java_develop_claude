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

    public Member findById(long id) {
        for (int i = 0; i < size; i++) {
            if (store[i].getId() == id) {
                return store[i];
            }
        }
        return null;
    }

    public Member findByEmail(String email) {
        for (int i = 0; i < size; i++) {
            if (store[i].getEmail().equals(email)) {
                return store[i];
            }
        }
        return null;
    }

    public Member[] findAll() {
        Member[] result = new Member[size];

        for (int i = 0; i < size; i++) {
            result[i] = store[i];
        }
        return result;
    }

}
