package io.reflectoring.hibernatesearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Indexed(index = "idx_post")
@NormalizerDef(name = "lower",
    filters = @TokenFilterDef(factory = LowerCaseFilterFactory.class))
public class Post {
  @Id
  private String id;

  @Field(name = "body")
  @Field(name = "bodyFiltered", analyzer = @Analyzer(definition = "stop"))
  private String body;

  @ManyToOne
  @IndexedEmbedded
  private User user;

  @Field(normalizer = @Normalizer(definition = "lower"))
  @Enumerated(EnumType.STRING)
  private Tag tag;

  private String imageUrl;

  private String imageDescription;

  @Field
  private String hashTags;

  @Field
  @SortableField
  private long likeCount;

  @Field(analyze = Analyze.NO)
  @SortableField
  private LocalDateTime createdAt;
}
