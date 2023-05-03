package io.reflectoring.publisher_register.service;

import io.reflectoring.publisher_register.model.Publisher;
import io.reflectoring.publisher_register.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService{

    @Autowired
    private PublisherRepository publisherRepository;

    public Publisher create(Publisher author) {
        return publisherRepository.save(author);
    }

    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    public Optional<Publisher> findOneById(String id) {
        return publisherRepository.findById(id);
    }

    public void delete(String id) {
        publisherRepository.deleteById(id);
    }

    public Publisher update(Publisher author) {
        return publisherRepository.save(author);
    }
}
