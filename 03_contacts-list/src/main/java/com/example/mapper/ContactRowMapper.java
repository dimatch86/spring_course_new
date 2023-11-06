package com.example.mapper;

import com.example.model.Contact;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactRowMapper implements RowMapper<Contact> {
    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Contact.builder()
                .id(rs.getLong(Contact.Fields.id))
                .firstName(rs.getString(toSnakeCase(Contact.Fields.firstName)))
                .lastName(rs.getString(toSnakeCase(Contact.Fields.lastName)))
                .email(rs.getString(Contact.Fields.email))
                .phone(rs.getString(Contact.Fields.phone))
                .build();
    }

    private String toSnakeCase(String str) {
        StringBuilder result = new StringBuilder();
        char c = str.charAt(0);
        result.append(Character.toLowerCase(c));
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_');
                result.append(Character.toLowerCase(ch));
            }
            else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
