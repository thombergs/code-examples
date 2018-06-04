package io.reflectoring.dsl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslJsonRootValue;

public class PactDslJsonBodyLikeMapper {

  private static final Set<Class<?>> SIMPLE_TYPES = new HashSet<>(Arrays.asList(
          Boolean.class,
          boolean.class,
          Integer.class,
          int.class,
          Double.class,
          double.class,
          Float.class,
          float.class,
          BigDecimal.class,
          Number.class,
          String.class,
          Long.class,
          long.class
  ));

  public static PactDslJsonBody like(Object object) {
    return like(object, new PactDslJsonBody());
  }

  public static PactDslJsonBody like(Object object, PactDslJsonBody body) {
    try {
      return recursiveLike(object, body);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("could not create PactDslJsonBody due to exception!", e);
    }
  }

  private static PactDslJsonBody recursiveLike(Object object, PactDslJsonBody body) throws IllegalAccessException {
    Field[] fields = object.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);

      Object fieldValue = field.get(object);

      if (fieldValue == null) {
        // fields with null values will not be mapped
        continue;
      }

      if (isSimpleType(field.getType())) {
        mapSimpleFieldWithName(field.getName(), fieldValue, body);
      } else if (isCollectionType(field.getType())) {
        mapCollectionField(field.getName(), (Collection) fieldValue, body);
      } else {
        mapComplexField(field.getName(), fieldValue, body);
      }

    }

    return body;
  }

  private static void mapSimpleFieldWithName(String fieldName, Object fieldValue, PactDslJsonBody body) throws IllegalAccessException {
    Class<?> type = fieldValue.getClass();
    if (String.class == type) {
      body.stringType(fieldName, (String) fieldValue);
    } else if (Boolean.class == type || boolean.class == type) {
      body.booleanType(fieldName, (Boolean) fieldValue);
    } else if (Integer.class == type || int.class == type || Long.class == type || long.class == type) {
      body.integerType(fieldName, (Integer) fieldValue);
    } else if (Double.class == type || double.class == type) {
      body.decimalType(fieldName, (Double) fieldValue);
    } else if (Float.class == type || float.class == type) {
      body.decimalType(fieldName, ((Float) fieldValue).doubleValue());
    } else if (BigDecimal.class == type) {
      body.decimalType(fieldName, (BigDecimal) fieldValue);
    } else if (Number.class.isAssignableFrom(type)) {
      body.numberType(fieldName, (Number) fieldValue);
    } else {
      throw new IllegalStateException(String.format("field '%s' of type '%s' is not a simple field", fieldName, type));
    }
  }

  private static PactDslJsonRootValue getRootValueForType(Class<?> type) {
    if (String.class == type) {
      return PactDslJsonRootValue.stringType();
    } else if (Boolean.class == type || boolean.class == type) {
      return PactDslJsonRootValue.booleanType();
    } else if (Integer.class == type || int.class == type || Long.class == type || long.class == type) {
      return PactDslJsonRootValue.integerType();
    } else if (Double.class == type || double.class == type) {
      return PactDslJsonRootValue.decimalType();
    } else if (Float.class == type || float.class == type) {
      return PactDslJsonRootValue.decimalType();
    } else if (BigDecimal.class == type) {
      return PactDslJsonRootValue.decimalType();
    } else if (Number.class.isAssignableFrom(type)) {
      return PactDslJsonRootValue.numberType();
    } else {
      throw new IllegalStateException(String.format("unsupported type '%s'", type));
    }
  }

  private static void mapCollectionField(String fieldName, Collection<?> collection, PactDslJsonBody body) throws IllegalAccessException {
    if (collection.isEmpty()) {
      throw new IllegalArgumentException("matching empty lists is not supported!");
    }

    Class<?> listType = collection.iterator().next().getClass();

    if (isSimpleType(listType)) {
      PactDslJsonRootValue rootValue = getRootValueForType(listType);
      body.eachLike(fieldName, rootValue);
    } else if (isCollectionType(listType)) {
      throw new IllegalArgumentException("collections of collections are not supported");
    } else {
      PactDslJsonBody nestedBody = body.eachLike(fieldName);
      for (Object complexObject : collection) {
        mapComplexFieldWithoutOpeningObject(complexObject, nestedBody);
      }
      nestedBody.closeObject().closeArray();
    }

  }

  private static void mapComplexField(String fieldName, Object fieldValue, PactDslJsonBody body) throws IllegalAccessException {
    PactDslJsonBody nestedBody = body.object(fieldName);
    mapComplexFieldWithoutOpeningObject(fieldValue, nestedBody);
  }

  private static void mapComplexFieldWithoutOpeningObject(Object fieldValue, PactDslJsonBody nestedBody) throws IllegalAccessException {
    recursiveLike(fieldValue, nestedBody);
    nestedBody.closeObject();
  }

  private static boolean isSimpleType(Class<?> type) {
    return SIMPLE_TYPES.contains(type);
  }

  private static boolean isCollectionType(Class<?> type) {
    return Collection.class.isAssignableFrom(type);
  }

}
