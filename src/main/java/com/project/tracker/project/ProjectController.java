package com.project.tracker.project;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository repository;
    private final ProjectMapper mapper;

    public ProjectController(ProjectRepository repository, ProjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ProjectResponseRecord> getAllProjects() {
        return mapper.toList(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<ProjectResponseRecord> createProject(@RequestBody @Valid ProjectRequestRecord input) {
        Project project = mapper.toProject(input);

        Project projectSaved = repository.save(project);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(projectSaved.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toRecord(projectSaved));
    }

    @GetMapping("/{id}")
    public ProjectResponseRecord getProjectById(@PathVariable(value="id") Long id){
        return mapper.toRecord(repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}

