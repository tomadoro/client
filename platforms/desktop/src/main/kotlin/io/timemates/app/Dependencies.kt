package io.timemates.app

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.timemates.api.rsocket.RSocketTimeMatesRequestsEngine
import io.timemates.app.authorization.dependencies.AuthorizationDataModule
import io.timemates.app.authorization.dependencies.screens.AfterStartModule
import io.timemates.app.authorization.dependencies.screens.ConfigureAccountModule
import io.timemates.app.authorization.dependencies.screens.ConfirmAuthorizationModule
import io.timemates.app.authorization.dependencies.screens.InitialAuthorizationModule
import io.timemates.app.authorization.dependencies.screens.NewAccountInfoModule
import io.timemates.app.authorization.dependencies.screens.StartAuthorizationModule
import io.timemates.app.core.handler.OnAuthorizationFailedHandler
import io.timemates.app.foundation.time.SystemUTCTimeProvider
import io.timemates.app.foundation.time.TimeProvider
import io.timemates.app.timers.dependencies.TimersDataModule
import io.timemates.app.timers.dependencies.screens.TimerCreationModule
import io.timemates.app.timers.dependencies.screens.TimerSettingsModule
import io.timemates.app.timers.dependencies.screens.TimersListModule
import io.timemates.sdk.common.engine.TimeMatesRequestsEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import org.koin.ksp.generated.module

/**
 * Initializes the application dependencies using Koin.
 *
 * @param authorizationFailedChannel A [Channel] that will be used to handle authorization failure events.
 */
fun initializeDependencies(
    authorizationFailedChannel: Channel<Unit>
) {
    startKoin {
        val platformModule = module {
            val jdbcDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

            single<SqlDriver>(qualifier = qualifier("authorization")) {
                jdbcDriver
            }

            single<SqlDriver>(qualifier = qualifier("users")) {
                jdbcDriver
            }

            single<TimeMatesRequestsEngine> {
                RSocketTimeMatesRequestsEngine(
                    endpoint = "wss://api.timemates.io/rsocket",
                    coroutineScope = CoroutineScope(Dispatchers.IO),
                )
            }

            single<OnAuthorizationFailedHandler> {
                OnAuthorizationFailedHandler { exception ->
                    exception.printStackTrace()
                    authorizationFailedChannel.trySend(Unit)
                }
            }

            single<TimeProvider> {
                SystemUTCTimeProvider()
            }
        }
        modules(
            platformModule,
            InitialAuthorizationModule().module,
            AuthorizationDataModule().module,
            ConfirmAuthorizationModule().module,
            StartAuthorizationModule().module,
            AfterStartModule().module,
            NewAccountInfoModule().module,
            ConfigureAccountModule().module,
            TimersListModule().module,
            TimerCreationModule().module,
            TimerSettingsModule().module,
        )
    }
}
