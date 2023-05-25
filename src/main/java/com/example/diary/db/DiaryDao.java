package com.example.diary.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DiaryDao extends JpaRepository<Diary, Long> {
    // updateDb(String name,Long id)の定義
    @Transactional
    @Modifying
    @Query(value = "UPDATE diary SET title = :ptitle,content = :pcontent WHERE id = :pid", nativeQuery = true)
    void updateDb(@Param("ptitle") String ptitle, @Param("pcontent") String pcontent, @Param("pid") Long pid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE diary SET title = :ptitle WHERE id = :pid", nativeQuery = true)
    void updateDb(@Param("ptitle") String ptitle, @Param("pid") Long pid);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO diary (title,content) VALUES (:ptitle,:pcontent)", nativeQuery = true)
    void insertDb(@Param("ptitle") String ptitle, @Param("pcontent") String pcontent);
}
