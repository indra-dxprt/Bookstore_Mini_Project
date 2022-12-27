package com.books.api.models

import jakarta.persistence.*
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption

@Entity
class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(unique = true)
    lateinit var username: String
    lateinit var password: String

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true)
    lateinit var books: List<Book>
}