package com.oopclass.oopexamplejava.repositories;

import com.oopclass.oopexamplejava.models.Journal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JournalRepository extends CrudRepository<Journal, Long> {
    @Query("select j from Journal j where j.idGroup = ?1")
    List<Journal> findByIdGroup(Long idGroup);

}
