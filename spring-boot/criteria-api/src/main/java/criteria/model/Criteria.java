package criteria.model;

public class Criteria {
   private String key;
   private Object value;
   private Operation operation;
   private boolean orPredicate;

   public Criteria( boolean orPredicate, String key, Operation operation, Object value) {
      this.key = key;
      this.value = value;
      this.operation = operation;
      this.orPredicate = orPredicate;
   }

   public String getKey() {
      return key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public Object getValue() {
      return value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public Operation getOperation() {
      return operation;
   }

   public void setOperation(Operation operation) {
      this.operation = operation;
   }

   public boolean isOrPredicate() {
      return orPredicate;
   }

   public void setOrPredicate(boolean orPredicate) {
      this.orPredicate = orPredicate;
   }
}
