package com.alkewallet6.service;

import com.alkewallet6.model.DTO.ContactDTO;
import com.alkewallet6.model.entity.ContactEntity;
import com.alkewallet6.model.entity.UserEntity;
import com.alkewallet6.repository.ContactRepository;
import com.alkewallet6.repository.UserRepository;
import com.alkewallet6.service.interfaces.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService implements IContactService {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ContactEntity> getAllContactList() {
        return contactRepository.findAll();
    }

    @Override
    public ContactEntity save(ContactDTO contactDTO,Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<ContactEntity> contactExists = contactRepository.findByNameAndEmail(contactDTO.getName(),contactDTO.getEmail());

        for (ContactEntity contactDB : contactExists){
            if (contactDB.isActive())
            {
                throw new RuntimeException("El contacto ya esta registrado");
            }else{
                contactDB.setActive(true);
                return contactRepository.save(contactDB);
            }
        }

        ContactEntity contact = new ContactEntity();
        contact.setName(contactDTO.getName());
        contact.setEmail(contactDTO.getEmail());
        contact.setUserEntity(user);
        contact.setActive(true);

        UserEntity userDB = userRepository.findByNameAndEmail(contactDTO.getName(), contact.getEmail());
        contact.setUser(userDB != null);
        return contactRepository.save(contact);
    }

    @Override
    public ContactEntity getById(Long id) {
        Optional<ContactEntity> optional = contactRepository.findById(id);
        ContactEntity contact = null;

        if (optional.isPresent()){
            contact= optional.get();
        }else {
            throw new RuntimeException("contacto no es encontrado: " + id);
        }
        return contact;
    }

    @Override
    public List<ContactEntity> getByUserEntityId(Long userId){
        return contactRepository.findByUserEntityId(userId);
    }

    @Override
    public List<ContactEntity> getByNameAndEmail(String name, String email) {
        return contactRepository.findByNameAndEmail(name,email);
    }

    @Override
    public void deleteContact(Long contactId) {
        ContactEntity contact = contactRepository.findById(contactId)
                    .orElseThrow(() -> new IllegalArgumentException("ID de contacto no es valido"));
        contact.setActive(false);
        contactRepository.save(contact);
    }
}
