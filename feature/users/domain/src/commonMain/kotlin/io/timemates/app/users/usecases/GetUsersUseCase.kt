package io.timemates.app.users.usecases

import io.timemates.app.users.repositories.UsersRepository
import io.timemates.sdk.common.exceptions.NotFoundException
import io.timemates.sdk.users.profile.types.User
import io.timemates.sdk.users.profile.types.value.UserId

/**
 * Use case class for retrieving a user by ID.
 *
 * @param repository The repository for user operations.
 */
class GetUsersUseCase(
    private val repository: UsersRepository,
) {
    /**
     * Executes the use case to retrieve a user with the specified ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The result of the operation.
     */
    suspend fun execute(userIds: List<UserId>): Result {
        return repository.getUsers(userIds)
            .map { Result.Success(it) }
            .getOrElse { exception: Throwable ->
                when (exception) {
                    is NotFoundException -> Result.NotFound
                    else -> Result.Failure(exception)
                }
            }
    }

    /**
     * Sealed class representing the result of the use case.
     */
    sealed class Result {
        /**
         * Represents a successful result with the retrieved user.
         *
         * @param users The retrieved user.
         */
        data class Success(val users: List<User>) : Result()

        /**
         * Represents a result indicating that the user was not found.
         */
        object NotFound : Result()

        /**
         * Represents any other failure result from server / sdk.
         *
         * @param throwable The throwable indicating the failure.
         */
        data class Failure(val throwable: Throwable) : Result()
    }
}
