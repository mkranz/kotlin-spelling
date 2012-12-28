package extensions

public inline fun <T> Iterable<T>.any() : Boolean {
    return this.iterator().hasNext()
}

public inline fun <T> Iterable<T>.or(other: Iterable<T>): Iterable<T> {
    if (this.any())
        return this
    return other
}
