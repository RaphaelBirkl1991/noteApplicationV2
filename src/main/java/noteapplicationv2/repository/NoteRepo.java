package noteapplicationv2.repository;

import noteapplicationv2.domain.Note;
import noteapplicationv2.enumeration.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> findByLevel(Level level);

    void deleteNoteById(Long id);
}
