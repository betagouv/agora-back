package fr.social.gouv.agora.domain

data class FeatureFlags(
    val isAskQuestionEnabled: Boolean,
    val isSignUpEnabled: Boolean,
    val isLoginEnabled: Boolean,
    val isQagSelectEnabled: Boolean,
    val isQagArchiveEnabled: Boolean,
)
