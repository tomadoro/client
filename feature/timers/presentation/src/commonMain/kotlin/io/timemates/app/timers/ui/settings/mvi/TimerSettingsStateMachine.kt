package io.timemates.app.timers.ui.settings.mvi

import androidx.compose.runtime.Immutable
import io.timemates.app.foundation.mvi.AbstractStateMachine
import io.timemates.app.foundation.mvi.UiEffect
import io.timemates.app.foundation.mvi.UiEvent
import io.timemates.app.foundation.mvi.UiState
import io.timemates.app.timers.ui.settings.mvi.TimerSettingsStateMachine.Effect
import io.timemates.app.timers.ui.settings.mvi.TimerSettingsStateMachine.Event
import io.timemates.app.timers.ui.settings.mvi.TimerSettingsStateMachine.State
import io.timemates.sdk.common.constructor.createOrThrow
import io.timemates.sdk.common.types.value.Count
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class TimerSettingsStateMachine(
    reducer: TimerSettingsReducer,
    middleware: TimerSettingsMiddleware,
) : AbstractStateMachine<State, Event, Effect>(
    reducer = reducer,
    middlewares = listOf(middleware),
) {
    override fun initDefaultState(): State {
        return State()
    }

    @Immutable
    data class State(
        val name: String = "",
        val description: String = "",
        val workTime: Duration = 25.minutes,
        val restTime: Duration = 5.minutes,
        val bigRestEnabled: Boolean = true,
        val bigRestPer: Count = Count.createOrThrow(4),
        val bigRestTime: Duration = 10.minutes,
        val isEveryoneCanPause: Boolean = false,
        val isConfirmationRequired: Boolean = true,
        val isNameSizeInvalid: Boolean = false,
        val isDescriptionSizeInvalid: Boolean = false,
        val isLoading: Boolean = false,
    ) : UiState

    sealed class Event : UiEvent {
        data class NameIsChanged(val name: String) : Event()

        data class DescriptionIsChanged(val description: String) : Event()

        data class WorkTimeIsChanged(val workTime: Duration) : Event()

        data class RestTimeIsChanged(val restTime: Duration) : Event()

        data class BigRestModeIsChanged(val bigRestEnabled: Boolean) : Event()

        data class BigRestPerIsChanged(val bigRestPer: Count) : Event()

        data class BigRestTimeIsChanged(val bigRestTime: Duration) : Event()

        data class TimerPauseControlAccessIsChanged(val isEveryoneCanPause: Boolean) : Event()

        data class ConfirmationRequirementChanged(val isConfirmationRequired: Boolean) : Event()

        object OnDoneClicked : Event()
    }

    sealed class Effect : UiEffect {
        data class Failure(val throwable: Throwable) : Effect()

        object Success : Effect()

        object NavigateToTimersScreen : Effect()
    }
}
