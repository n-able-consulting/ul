package com.spring.userslist.demo.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.userslist.demo.model.ApiHandshake;
import com.spring.userslist.demo.model.User;
import com.spring.userslist.demo.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {
   
	@Autowired
	private UserRepository UserRepository;
    private ApiHandshake handshake = new ApiHandshake();

    @GetMapping("")
	public String handshake() {
        return handshake.toJSON();
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUser(@RequestHeader Map<String, String> headers) {
		// System.out.println("api/users, allowed orgin(s) is: " + cors.getOrigin());
		// headers.forEach((key, value) -> {
		// 	System.out.println(String.format("api/users, Header '%s' = %s", key, value));
		// });
		return ResponseEntity.ok(UserRepository.findAll());
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable Long id) {
		try {
			User user = UserRepository.findById(id).get();
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			User user = new User();
			return ResponseEntity.ok(user);
		}
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User User) {
		return ResponseEntity.ok(UserRepository.save(User));
	}

}