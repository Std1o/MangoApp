package com.stdio.mangoapp.domain

interface Mapper<T, R> {

    fun map(input: T): R
}