package noteapplicationv2.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import noteapplicationv2.domain.HttpResponse;
import noteapplicationv2.enumeration.Level;
import noteapplicationv2.domain.Note;
import noteapplicationv2.exception.NoteNotFoundException;
import noteapplicationv2.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/note")
@RequiredArgsConstructor
public class NoteResource {
    private final NoteService noteService;

    @GetMapping("/all")
    public ResponseEntity<HttpResponse<Note>> getNotes() {
        return ResponseEntity.ok().body(noteService.getNotes());
    }

    @PostMapping("/add")
    public ResponseEntity<HttpResponse<Note>> saveNote(@RequestBody @Valid Note note) {
        return ResponseEntity.created(
                URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/note/all").toUriString())
        ).body(noteService.saveNote(note));
    }

    @GetMapping("/filter")
    public ResponseEntity<HttpResponse<Note>> filterNotes(@RequestParam(value = "level") Level level) {
        return ResponseEntity.ok().body(noteService.filterNotes(level));
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse<Note>> updateNote(@RequestBody @Valid Note note) throws NoteNotFoundException {
        return ResponseEntity.ok().body(noteService.updateNote(note));
    }

    @DeleteMapping("/delete/{noteId}")
    public ResponseEntity<HttpResponse<Note>> updateNote(@PathVariable(value = "noteId") Long id) throws NoteNotFoundException {
        return ResponseEntity.ok().body(noteService.deleteNote(id));
    }
}