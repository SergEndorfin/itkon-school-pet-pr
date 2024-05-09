package com.itkon.school.service;

import com.itkon.school.constants.ITKonContacts;
import com.itkon.school.model.Contact;
import com.itkon.school.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public boolean saveMessageDetails(Contact contact) {
        boolean isSave = false;
        contact.setStatus(ITKonContacts.OPEN);
        Contact savedContact = contactRepository.save(contact);
        log.info("message saved: {}", contact);
        if (savedContact != null && savedContact.getContactId() > 0) {
            isSave = true;
        }
        return isSave;
    }

    public List<Contact> findMsgsWithStatus() {
        return contactRepository.findByStatus(ITKonContacts.OPEN);
    }

    public boolean updateMsgStatus(int contactId) {
        AtomicBoolean isUpdated = new AtomicBoolean(false);
        contactRepository.findById(contactId).ifPresent(contact -> {
            contact.setStatus(ITKonContacts.CLOSE);
            Contact updatedContact = contactRepository.save(contact);
            if (updatedContact != null && updatedContact.getUpdatedBy() != null) {
                isUpdated.set(true);
            }
        });
        return isUpdated.get();
    }

    public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDirection) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageRequest = PageRequest.of(pageNum - 1, 5, sort);
        return contactRepository.findByStatus(ITKonContacts.OPEN, pageRequest);
    }

}
