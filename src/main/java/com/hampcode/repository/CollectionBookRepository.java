package com.hampcode.repository;

import com.hampcode.model.entity.Book;
import com.hampcode.model.entity.Collection;
import com.hampcode.model.entity.CollectionBook;
import com.hampcode.model.entity.CollectionBookPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
public interface CollectionBookRepository extends JpaRepository<CollectionBook, CollectionBookPK> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO collection_books (book_id, collection_id, added_date) VALUES (:bookId, :collectionId, :addedDate)", nativeQuery = true)
    void addBookToCollection(@Param("bookId") Integer bookId, @Param("collectionId") Integer collectionId, @Param("addedDate") LocalDateTime addedDate);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM collection_books WHERE book_id = :bookId AND collection_id = :collectionId", nativeQuery = true)
    void deleteByBookAndCollection(@Param("bookId") Integer bookId, @Param("collectionId") Integer collectionId);

    /*@Query(value = "SELECT b.* FROM collection_books cb JOIN books b ON cb.book_id = b.id WHERE cb.collection_id = :collectionId", nativeQuery = true)
    List<Book> findBooksByCollection(@Param("collectionId") Integer collectionId);*/


    @Query("SELECT cb.book FROM CollectionBook cb WHERE cb.collection = :collection")
    List<Book> findBooksByCollection(@Param("collection") Collection collection);

    @Query(value = "SELECT COUNT(*) > 0 FROM collection_books WHERE book_id = :bookId AND collection_id = :collectionId", nativeQuery = true)
    boolean existsByBookAndCollection(@Param("bookId") Integer bookId, @Param("collectionId") Integer collectionId);

}
