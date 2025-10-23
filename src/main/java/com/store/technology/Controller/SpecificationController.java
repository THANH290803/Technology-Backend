package com.store.technology.Controller;

import com.store.technology.Entity.Specification;
import com.store.technology.Service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/specifications")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping
    public List<Specification> getAll() {
        return specificationService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Specification> getById(@PathVariable Long id) {
        return specificationService.getById(id);
    }

    @PostMapping
    public Specification create(@RequestBody Specification spec) {
        return specificationService.create(spec);
    }

    @PutMapping("/{id}")
    public Specification update(@PathVariable Long id, @RequestBody Specification spec) {
        return specificationService.update(id, spec);
    }

    @DeleteMapping("/{id}")
    public boolean softDelete(@PathVariable Long id) {
        return specificationService.softDelete(id);
    }

    @PutMapping("/restore/{id}")
    public boolean restore(@PathVariable Long id) {
        return specificationService.restore(id);
    }
}
