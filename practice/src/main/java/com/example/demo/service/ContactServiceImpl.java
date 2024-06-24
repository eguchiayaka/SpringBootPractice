package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Contact;
import com.example.demo.form.EditForm;
import com.example.demo.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactRepository contactRepository;

	@Override
	public void updateContact(Long userId, EditForm editForm) {
		Contact contact = contactRepository.findById(userId).orElse(new Contact());

		contact.setId(userId);
		contact.setLastName(editForm.getLastName());
		contact.setFirstName(editForm.getFirstName());
		contact.setEmail(editForm.getEmail());
		contact.setPhone(editForm.getPhone());
		contact.setZipCode(editForm.getZipCode());
		contact.setAddress(editForm.getAddress());
		contact.setBuildingName(editForm.getBuildingName());
		contact.setContactType(editForm.getContactType());
		contact.setBody(editForm.getBody());
		contact.setUpdatedAt(LocalDateTime.now());

		if (contact.getCreatedAt() == null) {
			contact.setCreatedAt(LocalDateTime.now());
		}
		contact.setUpdatedAt(LocalDateTime.now());

		contactRepository.save(contact);
	}

	public Contact findContactById(Long id) {
		return contactRepository.findById(id).orElse(null);
	}

	public void deleteContact(Long id) {
		// TODO 自動生成されたメソッド・スタブ
		contactRepository.deleteById(id);
	}

}
