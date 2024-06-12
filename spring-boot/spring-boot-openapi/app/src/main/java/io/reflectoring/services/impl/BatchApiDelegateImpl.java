package io.reflectoring.services.impl;

import io.reflectoring.api.BatchApiDelegate;
import io.reflectoring.model.BatchMessage;
import io.reflectoring.model.MessageParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Slf4j
@Service
public class BatchApiDelegateImpl implements BatchApiDelegate {
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<String> sendBatchMessage(MessageParameters messageParams, BatchMessage body) {
        log.error(messageParams.toString());
        log.error(body.toString());

        return null;
    }
}
