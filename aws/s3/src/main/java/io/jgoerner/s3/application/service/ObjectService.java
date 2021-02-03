package io.jgoerner.s3.application.service;

import io.jgoerner.s3.application.port.in.object.CreateLink;
import io.jgoerner.s3.application.port.in.object.RemoveObject;
import io.jgoerner.s3.application.port.in.object.UpdateObject;
import io.jgoerner.s3.application.port.in.object.UploadObject;
import io.jgoerner.s3.application.port.out.object.*;
import io.jgoerner.s3.application.port.out.space.ResolveSpaceName;
import io.jgoerner.s3.domain.Object;
import io.jgoerner.s3.domain.ObjectPartial;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
@Log4j2
public class ObjectService implements UploadObject, UpdateObject, RemoveObject, CreateLink {

  private final ResolveSpaceName bucketNameResolver;
  private final SaveObject objectSaver;
  private final MakeObjectPublic objectPublicMaker;
  private final MakeObjectPrivate objectPrivateMaker;
  private final DeleteObject objectDeleter;
  private final CreatePresignedUrl presignedUrlCreator;

  public ObjectService(
      ResolveSpaceName bucketNameResolver,
      SaveObject objectSaver,
      MakeObjectPublic objectPublicMaker,
      MakeObjectPrivate objectPrivateMaker,
      DeleteObject objectDeleter,
      CreatePresignedUrl presignedUrlCreator) {
    this.bucketNameResolver = bucketNameResolver;
    this.objectSaver = objectSaver;
    this.objectPublicMaker = objectPublicMaker;
    this.objectPrivateMaker = objectPrivateMaker;
    this.objectDeleter = objectDeleter;
    this.presignedUrlCreator = presignedUrlCreator;
  }

  @Override
  public Object upload(String space, String name, InputStream payload) {
    // check if bucket exists
    var bucket = bucketNameResolver.resolve(space);

    // generate a id & store in lookup table
    var key = UUID.randomUUID().toString();

    // save
    log.info(
        "Going to upload the file into to "
            + bucket
            + "/"
            + key
            + " with metadata name of "
            + name);

    return objectSaver.safe(bucket, key, name, payload);
  }

  @Override
  public Object update(String space, String key, ObjectPartial updates) {
    var bucket = bucketNameResolver.resolve(space);

    if (updates.getIsPublic() != null) {
      if (updates.getIsPublic()) {
        log.info("going to open up to public");
        objectPublicMaker.makePublic(bucket, key);
      } else {
        log.info("going to remove public access");
        objectPrivateMaker.makePrivate(bucket, key);
      }
    }

    return null;
  }

  @Override
  public void delete(String space, String key) {
    var bucket = bucketNameResolver.resolve(space);

    log.info("Going to delete the file with the key " + key + " in the bucket " + bucket);

    objectDeleter.delete(bucket, key);
  }

  @Override
  public URL createLink(String space, String key, Long duration) {
    var bucket = bucketNameResolver.resolve(space);

    log.info(
        "Going to generate a link for the file "
            + key
            + " in bucket "
            + bucket
            + " with visibility duration of "
            + duration
            + " seconds");

    return presignedUrlCreator.createURL(bucket, key, duration);
  }
}
