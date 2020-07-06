package com.futysh.fyfm.utils

inline fun <T : Any> multipleLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}
