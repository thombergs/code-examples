package io.jgoerner.s3.adapter.out.h2;

import io.jgoerner.s3.application.port.out.space.*;
import io.jgoerner.s3.domain.Space;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class SpacePersistenceHandler
    implements CheckSpaceExistence,
        SaveSpace,
        RetrieveAllSpaces,
        ResolveSpaceName,
        DeleteSpace,
        RetrieveSpaceByName {

  private final SpaceRepository spaceRepository;

  public SpacePersistenceHandler(SpaceRepository spaceRepository) {
    this.spaceRepository = spaceRepository;
  }

  @Override
  public boolean doesExist(String name) {
    return this.spaceRepository.findById(name).isPresent();
  }

  @Override
  public Space save(Space name) {
    this.spaceRepository.save(SpacePersistenceHandler.mapPojoToJpa(name));
    return name;
  }

  @Override
  public List<Space> findAll() {
    return spaceRepository.findAll().stream()
        .map(SpacePersistenceHandler::mapJpaToPojo)
        .collect(Collectors.toList());
  }

  private static SpaceEntity mapPojoToJpa(Space space) {
    return new SpaceEntity(space.getName(), space.getBucket(), space.getTtl());
  }

  private static Space mapJpaToPojo(SpaceEntity entity) {
    return new Space(entity.getName(), entity.getBucket(), entity.getTtl());
  }

  @Override
  public String resolve(String name) {
    var bucket = spaceRepository.findById(name).get().getBucket();
    log.info("Space " + name + " was resolved to " + bucket);
    return bucket;
  }

  @Override
  public void delete(String name) {
    spaceRepository.deleteById(name);
  }

  @Override
  public Space retrieveByName(String name) {
    return mapJpaToPojo(spaceRepository.findById(name).orElseThrow());
  }
}
