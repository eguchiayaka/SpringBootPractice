package com.example.demo.service;

import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.form.DetailForm;
import com.example.demo.form.UserForm;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(UserForm userForm) {
        // TODO 自動生成されたメソッド・スタブ
        User user = new User();
        user.setLastName(userForm.getLastName());
        user.setFirstName(userForm.getFirstName());
        user.setEmail(userForm.getEmail());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));

        return userRepository.save(user);

    }
 
    // 現在ログインしているユーザーを取得するメソッド
    public User getLoggedInUser() {
        // SecurityContextHolder から認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 認証情報が存在しない場合や、認証が無効な場合は null を返す
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // 認証情報が UserDetails から取得可能か確認
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            // ユーザーリポジトリからメールアドレスでユーザーを検索して返す
            return userRepository.findByEmail(email);
        }

        // UserDetails ではない場合も null を返す
        return null;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
 
    // IDに基づいてユーザを検索するメソッド
    public User findUserById(Long id) {
        // Optional を使ってユーザーを検索し、見つからなければ null を返す
        return userRepository.findById(id).orElse(null);
    }
    
    // DetailFormにユーザの詳細情報を設定するメソッド
    public void populateDetailForm(Long id, DetailForm detailForm) {
        User user = findUserById(id);
        if (user != null) {
            detailForm.setLastName(user.getLastName());
            detailForm.setFirstName(user.getFirstName());
            detailForm.setEmail(user.getEmail());
        }
    }
}
