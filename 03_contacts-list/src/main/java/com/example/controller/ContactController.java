package com.example.controller;

import com.example.model.Contact;
import com.example.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("contacts", contactService.listContacts());
        return "index";
    }

    @GetMapping("/contact/create")
    public String showCreateForm(Model model) {
        model
                .addAttribute("contact", new Contact())
                .addAttribute("creationMarker", true);
        return "create";

    }
    @PostMapping("/contact/create")
    public String createContact(@Valid @ModelAttribute Contact contact) {
        contactService.save(contact);
        return "redirect:/";

    }

    @GetMapping("/contact/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Contact contact = contactService.findById(id);
        if (contact != null) {
            model
                    .addAttribute("contact", contact)
                    .addAttribute("creationMarker", false);
            return "create";
        }
        return "redirect:/";
    }
    @PostMapping ("/contact/edit")
    public String editContact(@Valid @ModelAttribute Contact contact) {
       contactService.editContact(contact);
       return "redirect:/";
    }

    @GetMapping("/contact/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteById(id);
        return "redirect:/";
    }
}
