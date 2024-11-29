package net.codejava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller

public class AppController {

    @Autowired
    private DAO_UserRepository userRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Home Page
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            var user = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user.getUsername(), user.getAuthorities().toString());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    // Registration Form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Entity_User()); // Form backing object
        return "signup_form"; // signup_form.html
    }

    // Process Registration
    @PostMapping("/process_register")
    public String processRegister(Entity_User entity_User) {
        String encodedPassword = passwordEncoder.encode(entity_User.getPassword());
        entity_User.setPassword(encodedPassword);
        
        userRepo.save(entity_User);
        
        return "redirect:/register_success"; // Redirect to register success page after successful registration
    }

    // Success Page
    @GetMapping("/register_success")
    public String showRegisterSuccessPage() {
        return "register_success"; // Display success message after registration
    }

    // List of Users
    @GetMapping("/users")
    public String listUsers(Model model, org.springframework.security.core.Authentication authentication) {
        String loggedInUserEmail = authentication.getName(); // Get logged-in user's email

        List<Entity_User> listUsers = userRepo.findAll(); // Fetch all users
        listUsers.removeIf(user -> user.getEmail().equals(loggedInUserEmail)); // Exclude logged-in user

        model.addAttribute("listUsers", listUsers); // Pass the user list to Thymeleaf
        return "users"; // Render the 'users.html' template
    }
    
    
}
    

