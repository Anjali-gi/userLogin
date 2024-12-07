package com.userlogin.controller;

import com.userlogin.entity.User;
import com.userlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	// GET mapping for login page
	@GetMapping("/")
	public String loginPage() {
		return "login";
		// returns login.html
	}

	// POST mapping for login
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model model) {
		User user = userService.validateUser(username, password); // Validate user credentials
		if (user != null) {
			model.addAttribute("user", user); // If valid, add user to model
			return "redirect:/dashboard?id=" + user.getId(); // Redirect to dashboard page with ID
		} else {
			model.addAttribute("error", "Invalid credentials!"); // Error message for invalid login
			return "login"; // Return to login page
		}
	}

	// GET mapping for signup page
	@GetMapping("/signup")
	public String signupPage(Model model) {
		model.addAttribute("user", new User());
		return "signup"; // returns signup.html
	}

	// POST mapping for signup
	@PostMapping("/signup")
	public String signup(@ModelAttribute User user, Model model) {
		userService.saveUser(user); // Save the user in the database
		model.addAttribute("message", "User registered successfully!"); // Success message
		return "login"; // Redirect to login page
	}

	// GET mapping for dashboard page
	@GetMapping("/dashboard")
	public String dashboard(@RequestParam(required = false) Long id, Model model) {
		// Check if ID is missing or invalid
		if (id == null) {
			model.addAttribute("error", "Invalid user ID!");
			return "login"; // Redirect to login page if ID is invalid
		}

		User user = userService.findById(id).orElse(null); // Get user by ID
		if (user != null) {
			model.addAttribute("user", user);
			return "dashboard"; // Return to dashboard.html
		} else {
			model.addAttribute("error", "User not found!"); // Error if user not found
			return "login"; // Redirect to login page
		}
	}

	// POST mapping for updating user info (except username)
	@PostMapping("/update")
	public String updateUser(@ModelAttribute User user, Model model) {
		// Fetch the existing user using the ID to ensure username remains unchanged
		User existingUser = userService.findById(user.getId()).orElse(null);

		if (existingUser != null) {
			// Only update the editable fields: firstName, lastName, email, and password
			existingUser.setFirstName(user.getFirstName());
			existingUser.setLastName(user.getLastName());
			existingUser.setEmail(user.getEmail());
			existingUser.setPassword(user.getPassword()); // Consider password encoding if necessary

			// Save the updated user
			userService.saveUser(existingUser);
			model.addAttribute("message", "User updated successfully!");
			//model.addAttribute("user", existingUser); // Add the updated user to the model
		} else {
			model.addAttribute("error", "User not found!");
		}
		return "update"; // Return to dashboard.html
	}
}