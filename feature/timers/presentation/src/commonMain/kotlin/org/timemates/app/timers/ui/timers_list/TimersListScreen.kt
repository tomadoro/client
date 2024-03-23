package org.timemates.app.timers.ui.timers_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.timemates.app.feature.common.MVI
import org.timemates.app.feature.common.failures.getDefaultDisplayMessage
import org.timemates.app.localization.compose.LocalStrings
import org.timemates.app.style.system.appbar.AppBar
import org.timemates.app.style.system.button.FloatingActionButton
import org.timemates.app.style.system.theme.AppTheme
import org.timemates.app.timers.ui.PlaceholderTimerItem
import org.timemates.app.timers.ui.TimerItem
import org.timemates.app.timers.ui.timers_list.mvi.TimersListScreenComponent.*
import pro.respawn.flowmvi.essenty.compose.subscribe

@Composable
fun TimersListScreen(
    mvi: MVI<State, Intent, Action>,
    navigateToSetting: () -> Unit,
    navigateToTimerCreationScreen: () -> Unit,
    navigateToTimer: (Long) -> Unit,
) {
    val snackbarData = remember { SnackbarHostState() }
    val strings = LocalStrings.current

    val state by mvi.subscribe { action ->
        when (action) {
            is Action.Failure ->
                snackbarData.showSnackbar(message = action.throwable.getDefaultDisplayMessage(strings))
        }
    }
    val timersListState = rememberLazyListState()

    if (state.hasMoreItems) {
        LaunchedEffect(timersListState.layoutInfo.visibleItemsInfo.lastOrNull()) {
            if (timersListState.isScrolledToTheEnd()) {
                mvi.store.intent(Intent.Load)
            }
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                title = LocalStrings.current.appName,
                action = {
                    IconButton(
                        onClick = { navigateToSetting() },
                        modifier = Modifier,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = null,
                        )
                    }
                },
                modifier = Modifier,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToTimerCreationScreen() },
                modifier = Modifier,
                containerColor = AppTheme.colors.primary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Navigate to Create Timer Screen Button",
                    modifier = Modifier,
                )
            }
        }
    ) { rootPaddings ->
        if (!state.isLoading && state.timersList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(rootPaddings)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Text(
                        text = LocalStrings.current.noTimers,
                        color = AppTheme.colors.secondaryVariant,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    state = timersListState,
                    contentPadding = rootPaddings,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.timersList) { timer ->
                        TimerItem(
                            timer = timer,
                            onClick = { navigateToTimer(timer.timerId.long) }
                        )
                    }

                    if (state.isLoading) {
                        items(5) {
                            PlaceholderTimerItem()
                        }
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

fun LazyListState.isScrolledToTheEnd(): Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
}
