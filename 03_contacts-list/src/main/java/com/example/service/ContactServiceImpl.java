package com.example.service;

import com.example.model.Contact;
import com.example.repository.ContactsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactsRepository contactsRepository;

    @Override
    public void save(Contact contact) {
        contactsRepository.save(contact);
    }

    @Override
    public List<Contact> listContacts() {
        return contactsRepository.findAll();
    }

    @Override
    public void editContact(Contact contact) {
        contactsRepository.update(contact);
    }

    @Override
    public void deleteById(Long id) {
        contactsRepository.deleteById(id);
    }

    @Override
    public Contact findById(Long id) {
        return contactsRepository.findById(id).orElse(null);
    }
}
