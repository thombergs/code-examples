package io.reflectoring.pagination;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class PaginatedController {

  private final MovieCharacterRepository characterRepository;

  @GetMapping(path = "/characters/page")
  Page<MovieCharacter> loadCharactersPage(Pageable pageable) {
    return characterRepository.findAllPage(pageable);
  }

  @GetMapping(path = "/characters/sorted")
  List<MovieCharacter> loadCharactersSorted(Sort sort) {
    return characterRepository.findAllSorted(sort);
  }

  @GetMapping(path = "/characters/slice")
  Slice<MovieCharacter> loadCharactersSlice(Pageable pageable) {
    return characterRepository.findAllSlice(pageable);
  }
}
