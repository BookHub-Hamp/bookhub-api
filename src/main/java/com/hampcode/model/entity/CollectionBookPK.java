package com.hampcode.model.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class CollectionBookPK implements Serializable {
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id"
    , foreignKey = @ForeignKey(name = "fk_collectionbooks_books"))
    private Book book;

    @ManyToOne
    @JoinColumn(name = "collection_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_collectionbooks_collections"))
    private Collection collection;
}
