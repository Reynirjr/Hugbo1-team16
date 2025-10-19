package org.example.oops;

import org.example.oops.User;
import org.example.oops.User.Role;
import org.example.oops.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPERUSER')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERUSER')")
    public String createStaff(@RequestBody User newUser) {
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return "User already exists!";
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(Role.STAFF);
        userRepository.save(newUser);
        return "Staff user created successfully.";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERUSER')")
    public String deleteUser(@PathVariable Integer id) {
        if (!userRepository.existsById(id)) {
            return "User not found.";
        }
        userRepository.deleteById(id);
        return "User deleted successfully.";
    }
}
