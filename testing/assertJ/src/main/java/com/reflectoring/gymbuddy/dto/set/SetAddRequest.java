package com.reflectoring.gymbuddy.dto.set;

public class SetAddRequest {

    private long weight;

    private long reps;

    public SetAddRequest(long weight, long reps){
      this.weight = weight;
      this.reps = reps;
    }

    public SetAddRequest(SetAddRequestBuilder builder){
      this.weight = builder.weight;
      this.reps = builder.reps;
    }

  public long getWeight() {
    return weight;
  }

  public long getReps() {
    return reps;
  }

  public static class SetAddRequestBuilder{
      private long weight;
      private long reps;

      public SetAddRequestBuilder(){}

      public SetAddRequestBuilder weight(long weight){
        this.weight = weight;
        return this;
      }

      public SetAddRequestBuilder reps(long reps){
        this.reps = reps;
        return this;
      }

      public SetAddRequest build(){
        return new SetAddRequest(this);
      }
    }
}
