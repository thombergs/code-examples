package io.refactoring.http5.client.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/** Represents data object. */
@JsonAutoDetect(
    fieldVisibility = Visibility.NONE,
    getterVisibility = Visibility.PUBLIC_ONLY,
    setterVisibility = Visibility.ANY,
    isGetterVisibility = Visibility.PUBLIC_ONLY)
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DataObject {
}
