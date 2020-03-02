package io.reflectoring.argumentresolver;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class NotFoundException extends HttpStatusCodeException {

  protected NotFoundException() {
    super(HttpStatus.NOT_FOUND);
  }
}
