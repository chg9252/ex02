package org.zerock.ex02.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex02.entity.Memo;

import java.util.List;
import java.util.Objects;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    void deleteMemoByMnoLessThan(Long num);


    /* @Query 어노테이션 */
    // mno의 역순으로 정력
    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();

    // @Query의 파라미터 바인딩 :파라미터 사용
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText= :memoText where m.mno = :mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
    int updateMemoText(@Param("param") Memo memo);


    /* @Query와 페이징처리*/
    // @Query를 이용할 때는 별도의 countQuery라는 속성을 적용해 주고 Pageable 타입의 파라미터를 전달
    @Query(value = "select m from Memo m where m.mno>:mno",
            countQuery = "select count(m) from Memo m where m.mno> :mno")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);


    // Object[]리턴
    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno", countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Objects[]> getListWithQueryObject(Long mno, Pageable pageable);

    // Native SQL 처리
    @Query(value = "select * from memo where mno > 0", nativeQuery = true)
    List<Object[]> getNativeResult();
}
