package com.reflectoring.gymbuddy.dto.set;

public class SetUpdateRequest {
    private long weight;

    private long reps;

    public SetUpdateRequest(long weight, long reps){
      this.weight = weight;
      this.reps = reps;
    }

    public SetUpdateRequest(SetUpdateRequestBuilder builder){
      this.weight = builder.weight;
      this.reps = builder.reps;
    }

  public long getWeight() {
    return weight;
  }

  public long getReps() {
    return reps;
  }

  public static class SetUpdateRequestBuilder{
      private long weight;
      private long reps;

      public SetUpdateRequestBuilder(){}

      public SetUpdateRequestBuilder weight(long weight){
        this.weight = weight;
        return this;
      }

      public SetUpdateRequestBuilder reps(long reps){
        this.reps = reps;
        return this;
      }

      public SetUpdateRequest build(){
        return new SetUpdateRequest(this);
      }
    }
}
