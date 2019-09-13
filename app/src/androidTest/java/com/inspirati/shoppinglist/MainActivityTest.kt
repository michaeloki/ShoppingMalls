package com.inspirati.shoppinglist


import junit.framework.Assert.assertEquals
import org.junit.Test


internal class MainActivityTest {

    @Test
    fun checkTitle() {
        assertEquals("ShopsList", MainActivity().title)
    }

    @Test
    fun isOnline(): Boolean {
        assert(isOnline())
        return false
    }

}