package com.reflectoring.library.model.mapstruct;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {
    @JsonProperty("bookId")
    private long id;

    @JsonProperty("bookName")
    private String name;

    @JsonProperty("publisher")
    private String publisher;

    @JsonProperty("publicationYear")
    private String publicationYear;

    @JsonProperty("isCopyrighted")
    private boolean copyrightIssued;

    @JsonProperty("authors")
    private Set<AuthorDto> authors;

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publicationYear='" + publicationYear + '\'' +
                ", copyrightIssued=" + copyrightIssued +
                ", authors=" + authors +
                '}';
    }
}
