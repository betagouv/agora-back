package fr.social.gouv.agora.infrastructure.reponseConsultation

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ConsultationResultJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("participantCount")
    val participantCount: Int,
    @JsonProperty("resultsUniqueChoice")
    val resultsUniqueChoice: List<QuestionResultJson>,
    @JsonProperty("resultsMultipleChoice")
    val resultsMultipleChoice: List<QuestionResultJson>,
    @JsonProperty("etEnsuite")
    val lastUpdate: ConsultationUpdatesJson,
)

data class QuestionResultJson(
    @JsonProperty("questionTitle")
    val questionTitle: String,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("responses")
    val responses: List<ChoiceResultJson>,
)

data class ChoiceResultJson(
    @JsonProperty("label")
    val label: String,
    @JsonProperty("ratio")
    val ratio: Int,
)

data class ConsultationUpdatesJson(
    @JsonProperty("step")
    val step: Int,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("explanationsTitle")
    val explanationsTitle: String?,
    @JsonProperty("explanations")
    val explanations: List<ExplanationJson>?,
    @JsonProperty("video")
    val video: VideoJson?,
    @JsonProperty("conclusion")
    val conclusion: ConclusionJson?,
)

data class ExplanationJson(
    @JsonProperty("isTogglable")
    val isTogglable: Boolean,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("intro")
    val intro: String,
    @JsonProperty("imageUrl")
    val imageUrl: String,
    @JsonProperty("description")
    val description: String,
)

data class VideoJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("intro")
    val intro: String,
    @JsonProperty("videoUrl")
    val videoUrl: String,
    @JsonProperty("videoWidth")
    val videoWidth: Int,
    @JsonProperty("videoHeight")
    val videoHeight: Int,
    @JsonProperty("transcription")
    val transcription: String,
)

data class ConclusionJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
)