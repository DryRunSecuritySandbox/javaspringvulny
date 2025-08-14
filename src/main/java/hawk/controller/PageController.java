package com.dryrun.demo;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/ui")
public class PageController {

    private final List<String> notes = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/greet", produces = MediaType.TEXT_HTML_VALUE)
    public String greet(@RequestParam(defaultValue = "Guest") String name) {
        // Basic page that renders the provided name
        return "<!doctype html><html><head><meta charset='utf-8'><title>Welcome</title></head><body>"
                + "<h1>Welcome, " + name + "</h1>"
                + "<form method='GET' action='/ui/greet'>"
                + "<input name='name' placeholder='Your name'/>"
                + "<button type='submit'>Go</button>"
                + "</form>"
                + "<p><a href='/ui/notes'>Notes</a></p>"
                + "</body></html>";
    }

    @GetMapping(value = "/notes", produces = MediaType.TEXT_HTML_VALUE)
    public String listNotes() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html><html><head><meta charset='utf-8'><title>Notes</title></head><body>");
        sb.append("<h2>Notes</h2><ul>");
        for (String n : notes) {
            sb.append("<li>").append(n).append("</li>");
        }
        sb.append("</ul>");
        sb.append("<form method='POST' action='/ui/notes'>"
                + "<input name='text' placeholder='Add note'/>"
                + "<button type='submit'>Save</button>"
                + "</form>");
        sb.append("<p><a href='/ui/greet'>Greet</a></p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    @PostMapping(value = "/notes", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addNote(@RequestParam String text) {
        notes.add(text);
        return ResponseEntity.status(302).header("Location", "/ui/notes").build();
    }
}
