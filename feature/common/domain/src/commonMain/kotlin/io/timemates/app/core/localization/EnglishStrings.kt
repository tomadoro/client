package io.timemates.app.core.localization

object EnglishStrings : Strings {
    override val appName: String = "TimeMates"
    override val start: String = "Start"
    override val nextStep: String = "Next step"
    override val authorization: String = "Authorization"
    override val authorizationDescription: String = """
        Welcome to $appName! Please provide your email address to proceed with the 
        authorization process. We will send a verification code to your email to confirm your
        identity.
    """.trimIndent()
    override val confirmation: String = "Confirmation"
    override val email: String = "Email address"
    override val emailSizeIsInvalid: String = "Email address size should be in range of 5 and 200 symbols"
    override val emailIsInvalid: String = "Email address is invalid."
    override val unknownFailure: String = "Unknown failure happened"
    override val tooManyAttempts: String = "Too many attempts."
    override val dismiss: String = "Dismiss"
}