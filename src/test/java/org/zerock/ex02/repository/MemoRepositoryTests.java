package org.zerock.ex02.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex02.entity.Memo;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        //  실제로 MemoRepository가 정상적으로 스프링에서 처리되고, 의존성 주입에 문제가 없는지를 먼저 확인
        System.out.println(memoRepository.getClass().getName());
    }

    // 등록작업 테스트
    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("memo sample.."+i).build();
            memoRepository.save(memo);
        });
    }


    // 조회작업 테스트 findById() 방식
    @Test
    public void testSelect() {
        Long mno = 100L;
        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("=========================");
        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }

        // findById() 를 실행한 순간에 SQL이 동작함
    }

    // 조회작업 테스트 getOne() 방식
    @Test
    @Transactional
    public void testSelect2() {
        Long mno = 100L;
        Memo memo = memoRepository.getOne(mno);
        System.out.println("===========================");
        System.out.println(memo);

        // 실제 객체를 사용하는 순간에 SQL이 동작한다.
    }

    // 수정작업 테스트
    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("update Text").build();
        System.out.println(memoRepository.save(memo));
    }

    // 삭제 작업 테스트
    @Test
    public void testDelete() {
        Long mno = 100L;
        memoRepository.deleteById(mno);
        // memoRepository.deleteAll();
    }

    // 페이징처리
    @Test
    public void testPageDefault() {

        // 1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
        System.out.println("===========================");
        System.out.println(("Total Pages: " + result.getTotalPages())); // 총 몇페이지
        System.out.println("Total Count: " + result.getTotalElements()); // 전체 개수
        System.out.println("Page Number: " + result.getNumber()); // 현재 페이지번호. 0부터시작
        System.out.println("has next page?: "+result.hasNext()); // 다음페이지 존재 여부
        System.out.println("first Page?: " + result.isFirst()); // 시작페이지(0) 여부

    }

    // 정렬조건 추가
    @Test
    public void testSort(){
        Sort sort1 = Sort.by("mno").descending(); // sort 조건1
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); // and를 이용해 sort조건 연결

        // Pageable pageable = PageRequest.of(0, 10, sort1);
        Pageable pageable = PageRequest.of(0, 20, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }









}
