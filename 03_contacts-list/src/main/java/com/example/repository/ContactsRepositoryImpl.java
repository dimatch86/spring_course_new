package com.example.repository;

import com.example.exception.ContactNotFoundException;
import com.example.mapper.ContactRowMapper;
import com.example.model.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContactsRepositoryImpl implements ContactsRepository {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<Contact> findAll() {

        String sql = "SELECT * FROM contact";
        return jdbcTemplate.query(sql, new ContactRowMapper());
    }

    @Override
    public Optional<Contact> findById(Long id) {
        String sql = "SELECT * FROM contact WHERE id = ?";
        Contact contact = DataAccessUtils.singleResult(
                jdbcTemplate.query(sql,
                        new ArgumentPreparedStatementSetter(new Object[] {id}),
                        new RowMapperResultSetExtractor<>(new ContactRowMapper(), 1))
        );
        return Optional.ofNullable(contact);
    }

    @Override
    public Contact save(Contact contact) {
        contact.setId(System.currentTimeMillis());
        String sql = "INSERT INTO contact (first_name, last_name, email, phone, id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone(), contact.getId());
        return contact;
    }

    @Override
    public void update(Contact contact) {
        findById(contact.getId()).ifPresentOrElse(existingContact -> {
            String sql = "UPDATE contact SET first_name = ?, last_name = ?, email = ?, phone = ? WHERE id = ?";
            jdbcTemplate.update(sql, contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone(), contact.getId());

        }, () -> {throw new ContactNotFoundException("Contact not found");});
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM contact WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
