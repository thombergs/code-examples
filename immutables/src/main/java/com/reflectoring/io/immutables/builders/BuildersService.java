package com.reflectoring.io.immutables.builders;

public class BuildersService {

    public static void main(String[] args) {
//        StrictBuilderArticle strictBuilderArticle =
//                createStrictArticle();
        StagedBuilderArticle stagedBuilderArticle = createStagedArticle();
    }

    // Show how strict builder works
    // Show error that is provided
    public static StrictBuilderArticle createStrictArticle(){
        return ImmutableStrictBuilderArticle.builder()
                .id(0)
                .id(1)
                .build();
    }

    // Staged article is strict implicitly
    // Staged article does now allow calling build method if not all mandatory fields are set
    // Downside of using staged builders

    public static StagedBuilderArticle createStagedArticle(){
        return ImmutableStagedBuilderArticle.builder()
                .id(0)
                .title("Lorem ipsum article!")
                .content("Lore ipsum...")
                .build();
    }


}
