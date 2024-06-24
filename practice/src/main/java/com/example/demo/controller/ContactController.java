package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Contact;
import com.example.demo.entity.User;
import com.example.demo.form.ContactForm;
import com.example.demo.form.DetailForm;
import com.example.demo.form.EditForm;
import com.example.demo.service.ContactServiceImpl;
import com.example.demo.service.UserServiceImpl;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin") // クラスレベルで "/admin" をプレフィックスとして追加
public class ContactController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ContactServiceImpl contactServiceImpl;

	@GetMapping("/contacts")
	public String showContactPage(Model model) {
		ContactForm contactForm = new ContactForm();
		// 現在のログインユーザを取得
		User currentUser = userServiceImpl.getLoggedInUser();
		// ユーザ情報を ContactForm に設定
		if (currentUser != null) {
			contactForm.setLastName(currentUser.getLastName());
			contactForm.setFirstName(currentUser.getFirstName());
		}
		model.addAttribute("contactForm", contactForm);
		return "contact";
	}

	@PostMapping("/contacts")
	public String processContactForm(Model model) {
		// 現在のログインユーザ情報を取得
		User loggedInUser = userServiceImpl.getLoggedInUser();
		// ログインユーザーのIDを使用してリダイレクト
		return "redirect:/admin/contacts/" + loggedInUser.getId();
	}

	@GetMapping("/contacts/{id}")
	public String showContactDetail(@PathVariable Long id, Model model) {
		// Contact情報を取得
		Contact contact = contactServiceImpl.findContactById(id);
		DetailForm detailForm = new DetailForm();

		if (contact == null) {
			// 初回アクセス時は User テーブルからデータを取得
			User user = userServiceImpl.findUserById(id);

			if (user == null) {
				// ユーザーが存在しない場合の処理
				return "redirect:/contacts";
			}

			// 初回
			detailForm.setLastName(user.getLastName());
			detailForm.setFirstName(user.getFirstName());
			detailForm.setEmail(user.getEmail());
		} else {
			// 2回目以降
			detailForm.setId(contact.getId());
			detailForm.setLastName(contact.getLastName());
			detailForm.setFirstName(contact.getFirstName());
			detailForm.setEmail(contact.getEmail());
			detailForm.setPhone(contact.getPhone());
			detailForm.setZipCode(contact.getZipCode());
			detailForm.setAddress(contact.getAddress());
			detailForm.setBuildingName(contact.getBuildingName());
			detailForm.setContactType(contact.getContactType());
			detailForm.setBody(contact.getBody());
			detailForm.setCreatedAt(contact.getCreatedAt());
			detailForm.setUpdatedAt(contact.getUpdatedAt());
		}

		model.addAttribute("detailForm", detailForm);
		model.addAttribute("id", id);

		return "detail";
	}

	@PostMapping("/contacts/{id}")
	public String complete(@PathVariable Long id,
			@ModelAttribute DetailForm detailForm,
			Model model) {
		return "redirect:/admin/contacts/" + id + "/edit";
	}

	@GetMapping("/contacts/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model model) {
		// Contact 情報を取得
		Contact contact = contactServiceImpl.findContactById(id);
		EditForm editForm = new EditForm();

		if (contact == null) {
			User user = userServiceImpl.findUserById(id);

			if (user == null) {
				return "redirect:/error";
			}
			editForm.setId(id);
			editForm.setLastName(user.getLastName());
			editForm.setFirstName(user.getFirstName());
			editForm.setEmail(user.getEmail());
		} else {		// 2回目以降は Contact テーブルからデータを取得
			editForm.setId(contact.getId());
			editForm.setLastName(contact.getLastName());
			editForm.setFirstName(contact.getFirstName());
			editForm.setEmail(contact.getEmail());
			editForm.setPhone(contact.getPhone());
			editForm.setZipCode(contact.getZipCode());
			editForm.setAddress(contact.getAddress());
			editForm.setBuildingName(contact.getBuildingName());
			editForm.setContactType(contact.getContactType());
			editForm.setBody(contact.getBody());
		}
		model.addAttribute("editForm", editForm);
		model.addAttribute("id", id);

		return "edit";
	}

	@PostMapping("/contacts/{id}/edit")
	public String processEditForm(@PathVariable Long id,
			@RequestParam("action") String action,
			@ModelAttribute @Valid EditForm editForm,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "edit";
		}
		if ("delete".equals(action)) {
			// 削除ボタンが押された場合の処理
			contactServiceImpl.deleteContact(id);
			return "redirect:/admin/contacts/delete"; // 削除が完了したらリダイレクト
		} else if ("send".equals(action)) {
			// 送信ボタンが押された場合の処理
			contactServiceImpl.updateContact(id, editForm);
			return "redirect:/admin/contacts/complete"; // 編集が完了したらリダイレクト
		}
		return "edit";
	}

	@GetMapping("/contacts/complete")
	public String complete(Model model) {
		return "completion";
	}

	@GetMapping("/contacts/delete")
	public String delete(Model model) {
		return "delete";
	}
}
