package com.geekstudio.composetest.data.dto

import androidx.compose.runtime.Immutable

@Immutable
data class ContactVal(val name: String, val number: String)
data class ContactVar(var name: String, var number: String)