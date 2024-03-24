package org.timemates.app.authorization.ui.new_account_info.mvi

import io.mockk.mockk
import io.mockk.verify
import org.timemates.sdk.authorization.email.types.value.VerificationHash
import org.timemates.sdk.common.constructor.createOrThrow
import kotlinx.coroutines.test.TestScope
import org.timemates.app.authorization.ui.new_account_info.mvi.NewAccountInfoScreenComponent.Intent
import org.timemates.app.authorization.ui.new_account_info.mvi.NewAccountInfoScreenComponent.State
import org.timemates.app.foundation.mvi.reduce
import org.timemates.app.foundation.random.nextString
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class NewAccountInfoReducerTest {
    private val verificationHash: VerificationHash = VerificationHash.factory.createOrThrow(Random.nextString(VerificationHash.SIZE))
    private val sendEffect: (NewAccountInfoScreenComponent.Action) -> Unit = mockk(relaxed = true)
    private val reducer: NewAccountInfoReducer = NewAccountInfoReducer(verificationHash)
    private val coroutineScope = TestScope()

    @Test
    fun `reducing event NextClicked event should not update the state`() {
        // GIVEN
        val state = State
        val event: Intent.NextClicked = Intent.NextClicked

        // WHEN
        val resultState = reducer.reduce(state, event, coroutineScope, sendEffect)

        // THEN
        verify { sendEffect(NewAccountInfoScreenComponent.Action.NavigateToAccountConfiguring(verificationHash)) }
        assertEquals(state, resultState)
    }

    @Test
    fun `reducing event OnBackClicked event should not update the state`() {
        // GIVEN
        val state = State
        val event = Intent.OnBackClicked

        // WHEN
        val resultState = reducer.reduce(state, event, coroutineScope, sendEffect)

        // THEN
        verify { sendEffect(NewAccountInfoScreenComponent.Action.NavigateToStart) }
        assertEquals(state, resultState)
    }
}