package com.example.practice.Models

class userModel {
    var image: String? = null
    var name: String? = null
    var email: String? = null
    var password: String? = null

    // Default constructor (No-argument)
    constructor()

    // Parameterized constructor
    constructor(image: String?, name: String?, email: String?, password: String?) {
        this.image = image
        this.name = name
        this.email = email
        this.password = password
    }
}
