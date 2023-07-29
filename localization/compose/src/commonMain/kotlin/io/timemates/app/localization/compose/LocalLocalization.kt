package io.timemates.app.localization.compose

import androidx.compose.runtime.staticCompositionLocalOf
import io.timemates.app.localization.EnglishStrings
import io.timemates.app.localization.Strings

val LocalStrings = staticCompositionLocalOf<Strings> { EnglishStrings }