package com.example.repository;

import com.example.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactsRepository {
    List<Contact> findAll();
    Optional<Contact> findById(Long id);
    Contact save(Contact contact);
    void update(Contact contact);
    void deleteById(Long id);
}
