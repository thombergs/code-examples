package io.jgoerner.s3.adapter.in;

import io.jgoerner.s3.application.port.in.object.*;
import io.jgoerner.s3.application.port.in.space.*;
import io.jgoerner.s3.domain.Object;
import io.jgoerner.s3.domain.ObjectPartial;
import io.jgoerner.s3.domain.Space;
import io.jgoerner.s3.domain.SpacePartial;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
@Log4j2
public class RestApi {

  private final CreateSpace spaceCreator;
  private final GetAllSpaces allSpaceGetter;
  private final RemoveSpace spaceRemover;
  private final GetAllObjects allObjectsInSpaceGetter;
  private final UploadObject objectUploader;
  private final UpdateObject objectUpdater;
  private final RemoveObject objectDeleter;
  private final ForceRemoveSpace forceSpaceRemover;
  private final CreateLink linkCreator;
  private final SetTTL ttlUpdater;
  private final RemoveTTL ttlRemover;

  public RestApi(
      CreateSpace spaceCreator,
      GetAllSpaces allSpaceGetter,
      RemoveSpace spaceRemover,
      GetAllObjects allObjectsInSpaceGetter,
      UploadObject objectUploader,
      UpdateObject objectUpdater,
      RemoveObject objectDeleter,
      ForceRemoveSpace forceSpaceRemover,
      CreateLink linkCreator,
      SetTTL ttlUpdater,
      RemoveTTL ttlRemover) {
    this.spaceCreator = spaceCreator;
    this.allSpaceGetter = allSpaceGetter;
    this.spaceRemover = spaceRemover;
    this.allObjectsInSpaceGetter = allObjectsInSpaceGetter;
    this.objectUploader = objectUploader;
    this.objectUpdater = objectUpdater;
    this.objectDeleter = objectDeleter;
    this.forceSpaceRemover = forceSpaceRemover;
    this.linkCreator = linkCreator;
    this.ttlUpdater = ttlUpdater;
    this.ttlRemover = ttlRemover;
  }

  @GetMapping("/space")
  List<Space> getSpaces() {
    return allSpaceGetter.getAll();
  }

  @PostMapping("/space/{space}")
  Space postSpace(@PathVariable String space) {
    return spaceCreator.create(space);
  }

  @DeleteMapping("/space/{space}")
  void deleteSpace(@PathVariable String space, @RequestParam Optional<Boolean> force) {
    log.info("Got the value " + force);
    force.ifPresentOrElse(
        value -> {
          if (value) {
            forceSpaceRemover.forceRemove(space);
          } else {
            spaceRemover.remove(space);
          }
        },
        () -> spaceRemover.remove(space));
  }

  @GetMapping("/space/{space}/object")
  List<Object> getObjectsInSpace(@PathVariable String space) {
    return allObjectsInSpaceGetter.getAllObjects(space);
  }

  @SneakyThrows
  @PostMapping("/space/{space}/object")
  Object postObject(
      @PathVariable String space,
      @RequestParam("file") MultipartFile file,
      @RequestParam(required = false, name = "name") String name) {
    var key = name != null ? name : file.getOriginalFilename();
    return objectUploader.upload(space, key, file.getInputStream());
  }

  @PatchMapping("/space/{space}/object/{key}")
  Object patchObject(
      @PathVariable String space, @PathVariable String key, @RequestBody ObjectPartial body) {
    log.info("Got the partial " + body);
    return objectUpdater.update(space, key, body);
  }

  @DeleteMapping("/space/{space}/object/{key}")
  void deleteObject(@PathVariable String space, @PathVariable String key) {
    objectDeleter.delete(space, key);
  }

  @PostMapping("/space/{space}/object/{key}/url")
  URL createLink(
      @PathVariable String space,
      @PathVariable String key,
      @RequestParam(required = false, name = "duration", defaultValue = "300") Long duration) {
    return linkCreator.createLink(space, key, duration);
  }

  @PatchMapping("/space/{space}")
  void patchSpace(@PathVariable String space, @RequestBody(required = false) SpacePartial body) {
    log.info("got " + body);
    if (body.getTtlInDays() > 1) {
      ttlUpdater.setTTL(space, body.getTtlInDays());
    } else {
      ttlRemover.removeTTL(space);
    }
  }
}
