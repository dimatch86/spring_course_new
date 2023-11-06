package com.example.service;

import com.example.model.Contact;

import java.util.List;

public interface ContactService {

    void save(Contact contact);
    List<Contact> listContacts();
    void editContact(Contact contact);
    void deleteById(Long id);

    Contact findById(Long id);
}
