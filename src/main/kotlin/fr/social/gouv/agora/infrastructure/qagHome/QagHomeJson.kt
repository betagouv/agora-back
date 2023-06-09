package fr.social.gouv.agora.infrastructure.qagHome

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.qag.SupportQagJson
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

@JsonInclude(JsonInclude.Include.NON_NULL)
data class QagHomeJson(
    @JsonProperty("incomingResponses")
    val incomingResponses: List<IncomingResponseQagPreviewJson>,
    @JsonProperty("responses")
    val responsesList: List<ResponseQagPreviewJson>,
    @JsonProperty("qags")
    val qagList: QagListJson,
    @JsonProperty("askQagErrorText")
    val askQagErrorText: String?,
)

data class IncomingResponseQagPreviewJson(
    @JsonProperty("qagId")
    val qagId: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("support")
    val support: SupportQagJson,
)

data class ResponseQagPreviewJson(
    @JsonProperty("qagId")
    val qagId: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("author")
    val author: String,
    @JsonProperty("authorPortraitUrl")
    val authorPortraitUrl: String,
    @JsonProperty("responseDate")
    val responseDate: String,
)

data class QagListJson(
    @JsonProperty("popular")
    val popular: List<QagPreviewJson>,
    @JsonProperty("latest")
    val latest: List<QagPreviewJson>,
    @JsonProperty("supporting")
    val supporting: List<QagPreviewJson>,
)

data class QagPreviewJson(
    @JsonProperty("qagId")
    val qagId: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("date")
    val date: String,
    @JsonProperty("support")
    val support: SupportQagJson,
)

