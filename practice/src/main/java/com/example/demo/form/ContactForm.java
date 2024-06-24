package com.example.demo.form;


import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ContactForm implements Serializable {
    @NotBlank
    private String lastName;

    @NotBlank
    private String firstName;

    @NotEmpty
    private String contactType;

    @NotBlank
    private String body;
    
    private LocalDateTime createdAt;
   	private LocalDateTime updatedAt;

   	public void setCreatedAt(LocalDateTime now) {
   	    this.createdAt = now;
   	}

   	public void setUpdatedAt(LocalDateTime now) {
   	    this.updatedAt = now;
   	}
}