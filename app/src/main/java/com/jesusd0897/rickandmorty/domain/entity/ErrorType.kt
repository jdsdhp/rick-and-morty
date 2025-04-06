package com.jesusd0897.rickandmorty.domain.entity

/**
 * The error type for the app.
 * @property NETWORK The error type for network errors.
 * @property LOAD The error type for load errors.
 * @property UNKNOWN The error type for unknown errors.
 */
enum class ErrorType {
    NETWORK,
    LOAD,
    UNKNOWN,
}