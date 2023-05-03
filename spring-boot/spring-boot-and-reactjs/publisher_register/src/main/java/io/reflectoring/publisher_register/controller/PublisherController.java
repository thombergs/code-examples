package io.reflectoring.publisher_register.controller;

import io.reflectoring.publisher_register.model.Publisher;
import io.reflectoring.publisher_register.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/publisher")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping("/create")
    public Publisher create(@RequestBody Publisher publisher){
        return publisherService.create(publisher);
    }

    @GetMapping("/all")
    public List<Publisher> getAllAuthors() {
        return publisherService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Publisher> findOneById(@PathVariable String id) {
        return publisherService.findOneById(id);
    }

    @PutMapping("/update")
    public Publisher update(@RequestBody Publisher publisher){
        return publisherService.update(publisher);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable String id){
        publisherService.delete(id);
    }
}
