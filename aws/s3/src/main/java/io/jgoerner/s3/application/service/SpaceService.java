package io.jgoerner.s3.application.service;

import io.jgoerner.s3.application.port.in.object.GetAllObjects;
import io.jgoerner.s3.application.port.in.object.RemoveObject;
import io.jgoerner.s3.application.port.in.space.*;
import io.jgoerner.s3.application.port.out.bucket.*;
import io.jgoerner.s3.application.port.out.object.DeleteObject;
import io.jgoerner.s3.application.port.out.space.*;
import io.jgoerner.s3.domain.Object;
import io.jgoerner.s3.domain.Space;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class SpaceService
    implements CreateSpace,
        GetAllSpaces,
        GetAllObjects,
        RemoveSpace,
        ForceRemoveSpace,
        SetTTL,
        RemoveTTL {

  private final CheckSpaceExistence spaceExistenceChecker;
  private final SaveSpace spaceSaver;
  private final RetrieveAllSpaces allSpaceRetriever;
  private final CreateBucket bucketCreator;
  private final ResolveSpaceName bucketNameResolver;
  private final DeleteBucket bucketDeleter;
  private final DeleteSpace spaceDeleter;
  private final ListObjects objectLister;
  private final DeleteObject objectDeleter;
  private final SetVisibilityInObjectLifecycle objectLifecycleVisibilitySetter;
  private final RemoveVisibilityInObjectLifecycle objectLifecycleVisibilityRemover;
  private final RetrieveSpaceByName spaceByNameRetriever;

  public SpaceService(
      CheckSpaceExistence spaceExistenceChecker,
      SaveSpace spaceSaver,
      RetrieveAllSpaces allSpaceRetriever,
      CreateBucket bucketCreator,
      ResolveSpaceName bucketNameResolver,
      DeleteBucket bucketDeleter,
      DeleteSpace spaceDeleter,
      ListObjects objectLister,
      RemoveObject objectRemover,
      DeleteObject objectDeleter,
      SetVisibilityInObjectLifecycle objectLifecycleVisibilitySetter,
      RemoveVisibilityInObjectLifecycle objectLifecycleVisibilityRemover,
      RetrieveSpaceByName spaceByNameRetriever) {
    this.spaceExistenceChecker = spaceExistenceChecker;
    this.spaceSaver = spaceSaver;
    this.allSpaceRetriever = allSpaceRetriever;
    this.bucketNameResolver = bucketNameResolver;
    this.bucketCreator = bucketCreator;
    this.bucketDeleter = bucketDeleter;
    this.spaceDeleter = spaceDeleter;
    this.objectLister = objectLister;
    this.objectDeleter = objectDeleter;
    this.objectLifecycleVisibilitySetter = objectLifecycleVisibilitySetter;
    this.objectLifecycleVisibilityRemover = objectLifecycleVisibilityRemover;
    this.spaceByNameRetriever = spaceByNameRetriever;
  }

  @Override
  public Space create(String name) {
    // check if bucket exists
    if (spaceExistenceChecker.doesExist(name)) {
      log.info("Space " + name + " does already exist");
      return null;
    }

    // create
    Space space = new Space(name, "spring-boot-s3-tutorial-" + UUID.randomUUID().toString(), null);
    log.info("Mapped space to bucket " + space);
    bucketCreator.create(space.getBucket());

    // create bucket meta
    return this.spaceSaver.save(space);
  }

  @Override
  public List<Space> getAll() {
    var buckets = allSpaceRetriever.findAll();
    log.info("Found " + buckets.size() + " buckets");
    return buckets;
  }

  @Override
  public List<Object> getAllObjects(String space) {
    // get bucket from H2
    var bucket = bucketNameResolver.resolve(space);

    // return all files in bucket
    return objectLister.listObjectsInBucket(bucket);
  }

  @Override
  public void remove(String space) {
    // get bucket from H2
    var bucket = bucketNameResolver.resolve(space);

    // delete from S3
    bucketDeleter.delete(bucket);

    // delete from H2
    spaceDeleter.delete(space);
  }

  @Override
  public void forceRemove(String space) {
    // get bucket from H2
    var bucket = bucketNameResolver.resolve(space);

    // empty bucket
    getAllObjects(space).stream()
        .peek(log::info)
        .forEach(object -> objectDeleter.delete(bucket, object.getKey()));

    // get rid of bucket
    remove(space);
  }

  @Override
  public void setTTL(String space, Integer ttlInDays) {
    var bucket = bucketNameResolver.resolve(space);
    log.info("Going to adjust the TTL for the bucket " + bucket + " to " + ttlInDays + " day(s)");

    // S3
    objectLifecycleVisibilitySetter.setVisibility(bucket, ttlInDays);

    // H2
    var spaceEntity = spaceByNameRetriever.retrieveByName(space);
    spaceEntity.setTtl(ttlInDays);
    spaceSaver.save(spaceEntity);
  }

  @Override
  public void removeTTL(String space) {
    var bucket = bucketNameResolver.resolve(space);
    log.info("Going to remove TTL policy for bucket " + bucket);

    // S3
    objectLifecycleVisibilityRemover.removeVisibility(bucket);

    // H2
    var spaceEntity = spaceByNameRetriever.retrieveByName(space);
    spaceEntity.setTtl(null);
    spaceSaver.save(spaceEntity);
  }
}
