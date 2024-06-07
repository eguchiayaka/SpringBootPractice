package com.example.demo.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.form.UserForm;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean authenticateUser(String email, String password) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			return passwordEncoder.matches(password, user.getPassword());
		} else {
			return false;
		}
	}

	public void saveUser(UserForm userForm) {
		// TODO 自動生成されたメソッド・スタブ
		User user = new User();
		user.setLastName(userForm.getLastName());
		user.setFirstName(userForm.getFirstName());
		user.setEmail(userForm.getEmail());
		user.setPassword(passwordEncoder.encode(userForm.getPassword()));

		userRepository.save(user);

	}

	@Override
	public void saveContact(UserForm userForm) {
		// TODO 自動生成されたメソッド・スタブ

	}
}