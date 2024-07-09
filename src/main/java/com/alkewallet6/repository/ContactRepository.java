package com.alkewallet6.repository;

import com.alkewallet6.model.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity,Long> {

    List<ContactEntity> findByUserEntityId(Long userId);
    List<ContactEntity> findByNameAndEmail(String name,String email);
}
