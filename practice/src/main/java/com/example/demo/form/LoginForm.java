package com.example.demo.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm  implements Serializable{
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;

}