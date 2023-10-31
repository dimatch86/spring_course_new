package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.MessageFormat;
import java.util.Scanner;
import java.util.Set;

@Component
public class PhoneBookService {

    @Value("${app.path.to.write}")
    private String pathToWrite;
    private final Set<Contact> contactList;

    public PhoneBookService(Set<Contact> contactList) {
        this.contactList = contactList;
    }

    @PostConstruct
    public void workWithPhoneBook() {

        while (true) {
            System.out.println("\nEnter the command: LIST, ADD, DELETE, SAVE or EXIT");

            String command = new Scanner(System.in).nextLine();

            switch (command) {
                case "LIST" -> printContacts();
                case "ADD" -> addContact();
                case "DELETE" -> deleteByEmail();
                case "SAVE" -> saveToFile();
                case "HELP" -> printHelp();
                case "EXIT" -> {
                    return;
                }
                default -> System.out.println("UNKNOWN COMMAND, enter \"HELP\"");
            }
        }
    }

    private void printContacts() {
        contactList.forEach(contact ->
                System.out.println(MessageFormat.format("{0}|{1}|{2}", contact.getFullName(), contact.getPhoneNumber(), contact.getEmail())));
    }

    private void addContact() {
        System.out.println("Введите контакт в формате - \"Иванов Иван Иванович;+890999999;someEmail@example.example\"");
        String contact = new Scanner(System.in).nextLine();
        String[] contactDetails = contact.split(";");
        if (contactDetails.length != 3 || !isValid(contactDetails)) {
            System.out.println("Incorrect input data");
            return;
        }
        contactList.add(new Contact(contactDetails[0], contactDetails[1], contactDetails[2]));
        System.out.println("New contact saved to list");
    }

    private void deleteByEmail() {
        System.out.println("Введите email для удаления");
        String email = new Scanner(System.in).nextLine();
        contactList.stream()
                .filter(contact -> contact.getEmail().equals(email))
                .findFirst()
                .ifPresentOrElse(contact -> {
                    contactList.remove(contact);
                    System.out.println("Contact removed successfully");
                    },
                        () -> System.out.println("Contact not found"));
    }

    private void saveToFile() {
        System.out.println("Are you sure to save contacts to file? (Y/N)");
        String input = new Scanner(System.in).nextLine();
        if (!input.equals("Y")) {
            return;
        }
        try(FileWriter writer = new FileWriter(pathToWrite, true))
        {
            StringBuilder stringBuilder = new StringBuilder();
            if (!isFileEmpty()) {
                writer.write("\n");
            }
            contactList.forEach(contact -> {
                String contactToWrite = MessageFormat.format("{0};{1};{2}", contact.getFullName(), contact.getPhoneNumber(), contact.getEmail());
                stringBuilder.append(contactToWrite).append(System.lineSeparator());
            });
            writer.write(stringBuilder.toString().trim());
            writer.flush();
            System.out.println("Contacts successfully saved to file");
        }
        catch(IOException ex){
            System.out.println("Unexpected failure: " + ex.getMessage());
        }

    }

    private void printHelp() {

        String message = """
                List of commands:
                LIST - list of contacts
                ADD - add contact to list
                DELETE - delete contact
                SAVE - write contacts to file
                EXIT - exit the program
                """;
        System.out.println(message);
    }

    private boolean isValid(String[] contactDetails) {
        return contactDetails[0].matches(ValidationUtils.FULL_NAME_PATTERN)
                && contactDetails[1].matches(ValidationUtils.PHONE_NUMBER_PATTERN)
                && contactDetails[2].matches(ValidationUtils.EMAIL_PATTERN);
    }

    private boolean isFileEmpty() {
        try (BufferedReader br = new BufferedReader(new FileReader(pathToWrite))) {

            return br.readLine() == null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
