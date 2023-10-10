package ru.severstal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.severstal.entity.Note;
import ru.severstal.entity.User;
import ru.severstal.repository.NoteRepo;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Controller
public class NotesController {

    @Autowired
    private NoteRepo noteRepo;

    @GetMapping("/")
    public String getStartPage(Map<String, Object> model) {
        return "home";
    }

    @GetMapping("/main")
    public String getAllNotes(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String filterTag,
            Map<String, Object> model
    ){
        Iterable<Note> notes;

        if (filterTag != null && !filterTag.isEmpty()) {
            notes = noteRepo.findByTagAndUserId(filterTag, user.getId());
        } else {
            notes = noteRepo.findAllByUserId(user.getId());
        }

        model.put("notes", notes);
        model.put("filterTag", filterTag);

        return "main";
    }

    @PostMapping("/main")
    public String addNote(
            @AuthenticationPrincipal User user,
            @Valid Note note,
            BindingResult bindingResult,
            Model model
    ) throws IOException {

        note.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("note", note);
        }
        else {

            model.addAttribute("note", null);

            noteRepo.save(note);
        }

        Iterable<Note> notes = noteRepo.findAllByUserId(user.getId());

        model.addAttribute("notes", notes);

        return "main";
    }

    @GetMapping("/main/{user}")
    public String userNotes(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Note note
    ) {
        Set<Note> notes = user.getNotes();

        model.addAttribute("notes", notes);
        model.addAttribute("note", note);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userNotes";
    }

    @PostMapping("/main/{user}")
    public String updateNote(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Note note,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag
    ) throws IOException {
        if (note.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                note.setText(text);
            }

            if (!StringUtils.isEmpty(tag)) {
                note.setTag(tag);
            }

            noteRepo.save(note);
        }

        return "redirect:/main";
    }
}
