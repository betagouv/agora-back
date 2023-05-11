package fr.social.gouv.agora.infrastructure.qagHome

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.qag.SupportQagJson
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJson

data class QagHomeJson(
    @JsonProperty("responses")
    val responsesList: List<ResponseQagPreviewJson>,
    @JsonProperty("qags")
    val qagList: QagListJson,
)

data class ResponseQagPreviewJson(
    @JsonProperty("qagId")
    val qagId: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueJson,
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
    val thematique: ThematiqueJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("date")
    val date: String,
    @JsonProperty("support")
    val support: SupportQagJson,
)

