package com.example.demo.service;



import com.example.demo.form.EditForm;


public interface ContactService {
	
	  
    void updateContact(Long userId, EditForm editForm);
   
}