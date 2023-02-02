package com.melitopolcherry.timester.core.extensions

@Suppress("NOTHING_TO_INLINE")
inline fun <T> lazyUnsynchronized(noinline initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)