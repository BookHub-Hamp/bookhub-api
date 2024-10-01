package com.hampcode.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "collection_books")
@IdClass(CollectionBookPK.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionBook {
    @Id
    private Integer book;
    @Id
    private Integer collection;

    @Column(name = "added_date", nullable = false)
    private LocalDateTime addedDate;
}
