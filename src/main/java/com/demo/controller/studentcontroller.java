package com.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Student;
import com.demo.repository.StudentRepository;

@RestController
@RequestMapping("/api/students")
public class studentcontroller {
    @Autowired StudentRepository repo;

    @GetMapping    public List<Student> getAll() { return repo.findAll(); }
    @PostMapping   public Student create(@RequestBody Student s) { return repo.save(s); }
    @GetMapping("/{id}") public Student getById(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
