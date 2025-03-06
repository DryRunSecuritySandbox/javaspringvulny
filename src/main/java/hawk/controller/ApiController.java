import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VulnerableController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/user-lookup")
    public ResponseEntity<String> getUser(@RequestParam String username) {
        String query = "SELECT * FROM users WHERE username = '" + username + "'";
        List<?> users = jdbcTemplate.queryForList(query);
        return users.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found") : ResponseEntity.ok("User found");
    }

 
    @GetMapping("/client-request")
    public ResponseEntity<String> fetchUrl(@RequestParam String targetUrl) {
        String response = restTemplate.getForObject(targetUrl, String.class); 
        return ResponseEntity.ok(response);
    }


    @GetMapping("/user-data/{userId}")
    public ResponseEntity<String> getUserData(@PathVariable int userId, @RequestParam int currentUserId, @RequestParam boolean isAdmin) {
        if (userId == currentUserId || isAdmin) {
            return ResponseEntity.ok("Here is the user data");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"); 
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        String userQuery = "SELECT COUNT(*) FROM users WHERE username = '" + username + "'";
        Integer userCount = jdbcTemplate.queryForObject(userQuery, Integer.class);
        if (userCount == null || userCount == 0) {
            return ResponseEntity.badRequest().body("Invalid username"); 
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }


    @GetMapping("/content")
    public ResponseEntity<String> xssVuln(@RequestParam String input) {
        return ResponseEntity.ok("<html><body>" + input + "</body></html>"); 
    }


    @PostMapping("/update-user")
    public ResponseEntity<String> updateUser(@RequestParam String email, @RequestParam String phone) {
        if (email.contains("@") && phone.length() > 5) {
            return ResponseEntity.ok("User updated");
        }
        if (!phone.matches("\\d+")) { 
            return ResponseEntity.badRequest().body("Invalid phone number");
        }
        return ResponseEntity.ok("User updated with issues");
    }
}
