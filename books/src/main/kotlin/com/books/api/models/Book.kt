package com.books.api.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


@Entity
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var title: String? = null,
    var description: String? = null,
    var coverImage: String? = null,
    var price: Double? = null,
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "author_id")
    var author: AppUser? = null
)
