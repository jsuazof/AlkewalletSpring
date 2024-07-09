package com.alkewallet6.service.interfaces;

import com.alkewallet6.model.DTO.ContactDTO;
import com.alkewallet6.model.entity.ContactEntity;

import java.util.List;

public interface IContactService {

    List<ContactEntity> getAllContactList();
    ContactEntity save(ContactDTO contactDTO,Long userId);
    ContactEntity getById(Long id);
    List<ContactEntity> getByUserEntityId(Long userId);
    List<ContactEntity> getByNameAndEmail(String name,String email);
    void deleteContact(Long contactId);
}
