package io.refactoring.http5.client.example.classic.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * Handles response for data objects.
 *
 * @param <T> type of data object
 */
@Slf4j
public class DataObjectResponseHandler<T> extends AbstractHttpClientResponseHandler<T> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @NonNull private final Class<T> realType;

  /**
   * Construction.
   *
   * @param realType the type of data object
   */
  public DataObjectResponseHandler(@NonNull final Class<T> realType) {
    this.realType = realType;
  }

  /**
   * Represents ResponseHandler for converting the response entity into POJO instance.
   *
   * @param <T> type of data object
   */
  @Override
  public T handleEntity(HttpEntity httpEntity) throws IOException {

    try {
      return objectMapper.readValue(EntityUtils.toString(httpEntity), realType);
    } catch (ParseException e) {
      throw new ClientProtocolException(e);
    }
  }
}
