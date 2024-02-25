package io.github.suitetecsa.sdk.nauta.exception

class LoadInfoException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, throwable: Throwable?) : super(message, throwable)
}
