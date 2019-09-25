package com.kvinod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository repo;

	@GetMapping
	public Iterable<User> get() {
		log.info("Returning a list of all users.");
		return repo.findAll();
	}

	@Cacheable(key = "#id", value = "user")
	@GetMapping("/{id}")
	public User get(@PathVariable String id) {
		log.info("Returning a specific user for id: " + id);
		return repo.findById(id).get();
	}

	@PostMapping
	public User add(@RequestBody User user) {
		log.info("Added new user: " + user.toString());
		return repo.save(user);
	}

	@CachePut(value = "user", key = "#id")
	@PutMapping("/{id}")
	public User update(@PathVariable String id, @RequestBody User user) {
		user.setId(id);
		log.info("Updating user details for: " + user.toString());
		return repo.save(user);
	}

	@CacheEvict(value = "user", key = "#id")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		repo.deleteById(id);
		log.info("Deleted user for id: " + id);
		return ResponseEntity.ok("Deleted!");
	}

}
