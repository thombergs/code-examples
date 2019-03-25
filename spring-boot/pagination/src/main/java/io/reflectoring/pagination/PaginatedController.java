package io.reflectoring.pagination;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
class PaginatedController {

  private final CharacterRepository characterRepository;

  @GetMapping(path = "/characters")
  List<Character> loadCharacters(Pageable pageable) {
    return characterRepository.findAll(pageable);
  }
}
