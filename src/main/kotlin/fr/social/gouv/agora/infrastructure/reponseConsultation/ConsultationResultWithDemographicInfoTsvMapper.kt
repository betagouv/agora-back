package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class ConsultationResultWithDemographicInfoTsvMapper {

    fun buildTsvBody(consultationResult: ConsultationResultWithDemographicInfo) = """
${buildHeader(consultationResult)}
# Données socio-démographiques
${buildDemographicInfoBloc(consultationResult.demographicInfo)}           
# Réponses aux questions
${buildConsultationResponsesBloc(consultationResult)}
""".trimIndent()

    private fun buildHeader(consultationResult: ConsultationResultWithDemographicInfo): String {
        val bodyBuilder = StringBuilder()

        bodyBuilder.append(consultationResult.consultation.title)
        bodyBuilder.append("\n")
        bodyBuilder.append("${consultationResult.participantCount}\tparticipants")
        bodyBuilder.append("\n")

        return bodyBuilder.toString()
    }

    private fun buildConsultationResponsesBloc(consultationResult: ConsultationResultWithDemographicInfo): String {
        val bodyBuilder = StringBuilder()

        consultationResult.results.forEach { questionResult ->
            bodyBuilder.append("## ${questionResult.question.title}\t#\t%")
            bodyBuilder.append("\t${buildDemographicHeaders(consultationResult.demographicInfo)}")
            bodyBuilder.append("\n")
            questionResult.responses.forEach { choiceResult ->
                bodyBuilder.append("- ${choiceResult.choixPossible.label}\t${choiceResult.countAndRatio.count}\t${choiceResult.countAndRatio.ratio}")
                bodyBuilder.append(
                    "\t${
                        buildDemographicValues(
                            globalDemographicInfo = consultationResult.demographicInfo,
                            choiceDemographicInfo = choiceResult.demographicInfo,
                        )
                    }"
                )
                bodyBuilder.append("\n")
            }
        }

        return bodyBuilder.toString()
    }

    private fun buildDemographicInfoBloc(demographicInfo: DemographicInfo): String {
        val bodyBuilder = StringBuilder()

        bodyBuilder.append("### Genre\t#\t%")
        bodyBuilder.append("\n")
        demographicInfo.genderCount.forEach { (gender, countAndRatio) ->
            bodyBuilder.append("-- ${gender.toPretty()}\t${countAndRatio.count}\t${countAndRatio.ratio}")
            bodyBuilder.append("\n")
        }

        bodyBuilder.append("### Année de naissance\t#\t%")
        bodyBuilder.append("\n")
        demographicInfo.yearOfBirthCount.forEach { (yearOfBirth, countAndRatio) ->
            bodyBuilder.append("-- ${yearOfBirth.toPretty()}\t${countAndRatio.count}\t${countAndRatio.ratio}")
            bodyBuilder.append("\n")
        }

        bodyBuilder.append("### Département ou collectivité d'outre mer\t#\t%")
        bodyBuilder.append("\n")
        demographicInfo.departmentCount.forEach { (department, countAndRatio) ->
            bodyBuilder.append("-- ${department.toPretty()}\t${countAndRatio.count}\t${countAndRatio.ratio}")
            bodyBuilder.append("\n")
        }

        bodyBuilder.append("### Habite en milieu\t#\t%")
        bodyBuilder.append("\n")
        demographicInfo.cityTypeCount.forEach { (cityType, countAndRatio) ->
            bodyBuilder.append("-- ${cityType.toPretty()}\t${countAndRatio.count}\t${countAndRatio.ratio}")
            bodyBuilder.append("\n")
        }

        bodyBuilder.append("### Catégorie socio-professionnelle\t#\t%")
        bodyBuilder.append("\n")
        demographicInfo.jobCategoryCount.forEach { (jobCategory, countAndRatio) ->
            bodyBuilder.append("-- ${jobCategory.toPretty()}\t${countAndRatio.count}\t${countAndRatio.ratio}")
            bodyBuilder.append("\n")
        }

        bodyBuilder.append("### Fréquence de vote\t#\t%")
        bodyBuilder.append("\n")
        demographicInfo.voteFrequencyCount.forEach { (frequency, countAndRatio) ->
            bodyBuilder.append("-- ${frequency.toPretty()}\t${countAndRatio.count}\t${countAndRatio.ratio}")
            bodyBuilder.append("\n")
        }

        bodyBuilder.append("### Fréquence d'engagement sur le terrain\t#\t%")
        bodyBuilder.append("\n")
        demographicInfo.publicMeetingFrequencyCount.forEach { (frequency, countAndRatio) ->
            bodyBuilder.append("-- ${frequency.toPretty()}\t${countAndRatio.count}\t${countAndRatio.ratio}")
            bodyBuilder.append("\n")
        }

        bodyBuilder.append("### Fréquence d'engagement en ligne\t#\t%")
        bodyBuilder.append("\n")
        demographicInfo.consultationFrequencyCount.forEach { (frequency, countAndRatio) ->
            bodyBuilder.append("-- ${frequency.toPretty()}\t${countAndRatio.count}\t${countAndRatio.ratio}")
            bodyBuilder.append("\n")
        }

        return bodyBuilder.toString()
    }

    private fun buildDemographicHeaders(demographicInfo: DemographicInfo): String {
        val headerBuilder = StringBuilder()

        headerBuilder.append("### Genre")
        headerBuilder.append("\t")
        demographicInfo.genderCount.forEach { (gender, _) ->
            headerBuilder.append("-- ${gender.toPretty()}")
            headerBuilder.append("\t")
        }

        headerBuilder.append("### Année de naissance")
        headerBuilder.append("\t")
        demographicInfo.yearOfBirthCount.forEach { (yearOfBirth, _) ->
            headerBuilder.append("-- ${yearOfBirth.toPretty()}")
            headerBuilder.append("\t")
        }

        headerBuilder.append("### Département ou collectivité d'outre mer")
        headerBuilder.append("\t")
        demographicInfo.departmentCount.forEach { (department, _) ->
            headerBuilder.append("-- ${department.toPretty()}")
            headerBuilder.append("\t")
        }

        headerBuilder.append("### Habite en milieu")
        headerBuilder.append("\t")
        demographicInfo.cityTypeCount.forEach { (cityType, _) ->
            headerBuilder.append("-- ${cityType.toPretty()}")
            headerBuilder.append("\t")
        }

        headerBuilder.append("### Catégorie socio-professionnelle")
        headerBuilder.append("\t")
        demographicInfo.jobCategoryCount.forEach { (jobCategory, _) ->
            headerBuilder.append("-- ${jobCategory.toPretty()}")
            headerBuilder.append("\t")
        }

        headerBuilder.append("### Fréquence de vote")
        headerBuilder.append("\t")
        demographicInfo.voteFrequencyCount.forEach { (frequency, _) ->
            headerBuilder.append("-- ${frequency.toPretty()}")
            headerBuilder.append("\t")
        }

        headerBuilder.append("### Fréquence d'engagement sur le terrain")
        headerBuilder.append("\t")
        demographicInfo.publicMeetingFrequencyCount.forEach { (frequency, _) ->
            headerBuilder.append("-- ${frequency.toPretty()}")
            headerBuilder.append("\t")
        }

        headerBuilder.append("### Fréquence d'engagement en ligne")
        headerBuilder.append("\t")
        demographicInfo.consultationFrequencyCount.forEach { (frequency, _) ->
            headerBuilder.append("-- ${frequency.toPretty()}")
            headerBuilder.append("\t")
        }

        return headerBuilder.toString()
    }

    private fun buildDemographicValues(globalDemographicInfo: DemographicInfo, choiceDemographicInfo: DemographicInfo): String {
        val valuesBuilder = StringBuilder()

        valuesBuilder.append("\t")
        globalDemographicInfo.genderCount.forEach { (gender, _) ->
            valuesBuilder.append("${choiceDemographicInfo.genderCount[gender]?.ratio ?: 0}")
            valuesBuilder.append("\t")
        }

        valuesBuilder.append("\t")
        globalDemographicInfo.yearOfBirthCount.forEach { (yearOfBirth, _) ->
            valuesBuilder.append("${choiceDemographicInfo.yearOfBirthCount[yearOfBirth]?.ratio ?: 0}")
            valuesBuilder.append("\t")
        }

        valuesBuilder.append("\t")
        globalDemographicInfo.departmentCount.forEach { (department, _) ->
            valuesBuilder.append("${choiceDemographicInfo.departmentCount[department]?.ratio ?: 0}")
            valuesBuilder.append("\t")
        }

        valuesBuilder.append("\t")
        globalDemographicInfo.cityTypeCount.forEach { (cityType, _) ->
            valuesBuilder.append("${choiceDemographicInfo.cityTypeCount[cityType]?.ratio ?: 0}")
            valuesBuilder.append("\t")
        }

        valuesBuilder.append("\t")
        globalDemographicInfo.voteFrequencyCount.forEach { (frequency, _) ->
            valuesBuilder.append("${choiceDemographicInfo.voteFrequencyCount[frequency]?.ratio ?: 0}")
            valuesBuilder.append("\t")
        }

        valuesBuilder.append("\t")
        globalDemographicInfo.publicMeetingFrequencyCount.forEach { (frequency, _) ->
            valuesBuilder.append("${choiceDemographicInfo.publicMeetingFrequencyCount[frequency]?.ratio ?: 0}")
            valuesBuilder.append("\t")
        }

        valuesBuilder.append("\t")
        globalDemographicInfo.consultationFrequencyCount.forEach { (frequency, _) ->
            valuesBuilder.append("${choiceDemographicInfo.consultationFrequencyCount[frequency]?.ratio ?: 0}")
            valuesBuilder.append("\t")
        }

        return valuesBuilder.toString()
    }

    private fun Gender?.toPretty() = when (this) {
        Gender.MASCULIN -> "Homme"
        Gender.FEMININ -> "Femme"
        Gender.AUTRE -> "Autre"
        null -> "N/A"
    }

    private fun Int?.toPretty() = when (this) {
        null -> "N/A"
        else -> toString()
    }

    private fun Department?.toPretty() = when (this) {
        Department.AIN_1 -> "1 - Ain"
        Department.AISNE_2 -> "2 - Aisne"
        Department.ALLIER_3 -> "3 - Allier"
        Department.ALPESDEHAUTEPROVENCE_4 -> "4 - Alpes-de-Haute-Provence"
        Department.HAUTESALPES_5 -> "5 - Hautes-Alpes"
        Department.ALPESMARITIMES_6 -> "6 - Alpes-Maritimes"
        Department.ARDECHE_7 -> "7 - Ardèche"
        Department.ARDENNES_8 -> "8 - Ardennes"
        Department.ARIEGE_9 -> "9 - Ariège"
        Department.AUBE_10 -> "10 - Aube"
        Department.AUDE_11 -> "11 - Aude"
        Department.AVEYRON_12 -> "12 - Aveyron"
        Department.BOUCHESDURHONE_13 -> "13 - Bouches-du-Rhône"
        Department.CALVADOS_14 -> "14 - Calvados"
        Department.CANTAL_15 -> "15 - Cantal"
        Department.CHARENTE_16 -> "16 - Charente"
        Department.CHARENTEMARITIME_17 -> "17 - Charente-Maritime"
        Department.CHER_18 -> "18 - Cher"
        Department.CORREZE_19 -> "19 - Corrèze"
        Department.CORSEDUSUD_2A -> "2A - Corse-du-Sud"
        Department.HAUTECORSE_2B -> "2B - Haute-Corse"
        Department.COTEDOR_21 -> "21 - Côte-d'Or"
        Department.COTESDARMOR_22 -> "22 - Côtes-d'Armor"
        Department.CREUSE_23 -> "23 - Creuse"
        Department.DORDOGNE_24 -> "24 - Dordogne"
        Department.DOUBS_25 -> "25 - Doubs"
        Department.DROME_26 -> "26 - Drôme"
        Department.EURE_27 -> "27 - Eure"
        Department.EUREETLOIR_28 -> "28 - Eure-et-Loir"
        Department.FINISTERE_29 -> "29 - Finistère"
        Department.GARD_30 -> "30 - Gard"
        Department.HAUTEGARONNE_31 -> "31 - Haute-Garonne"
        Department.GERS_32 -> "32 - Gers"
        Department.GIRONDE_33 -> "33 - Gironde"
        Department.HERAULT_34 -> "34 - Hérault"
        Department.ILLEETVILAINE_35 -> "35 - Ille-et-Vilaine"
        Department.INDRE_36 -> "36 - Indre"
        Department.INDREETLOIRE_37 -> "37 - Indre-et-Loire"
        Department.ISERE_38 -> "38 - Isère"
        Department.JURA_39 -> "39 - Jura"
        Department.LANDES_40 -> "40 - Landes"
        Department.LOIRETCHER_41 -> "41 - Loir-et-Cher"
        Department.LOIRE_42 -> "42 - Loire"
        Department.HAUTELOIRE_43 -> "43 - Haute-Loire"
        Department.LOIREATLANTIQUE_44 -> "44 - Loire-Atlantique"
        Department.LOIRET_45 -> "45 - Loiret"
        Department.LOT_46 -> "46 - Lot"
        Department.LOTETGARONNE_47 -> "47 - Lot-et-Garonne"
        Department.LAZERE_48 -> "48 - Lozère"
        Department.MAINEETLOIRE_49 -> "49 - Maine-et-Loire"
        Department.MANCHE_50 -> "50 - Manche"
        Department.MARNE_51 -> "51 - Marne"
        Department.HAUTEMARNE_52 -> "52 - Haute-Marne"
        Department.MAYENNE_53 -> "53 - Mayenne"
        Department.MEURTHEETMOSELLE_54 -> "54 - Meurthe-et-Moselle"
        Department.MEUSE_55 -> "55 - Meuse"
        Department.MORBIHAN_56 -> "56 - Morbihan"
        Department.MOSELLE_57 -> "57 - Moselle"
        Department.NIEVRE_58 -> "58 - Nièvre"
        Department.NORD_59 -> "59 - Nord"
        Department.OISE_60 -> "60 - Oise"
        Department.ORNE_61 -> "61 - Orne"
        Department.PASDECALAIS_62 -> "62 - Pas-de-Calais"
        Department.PUYDEDOME_63 -> "63 - Puy-de-Dôme"
        Department.PYRENEESATLANTIQUES_64 -> "64 - Pyrénées-Atlantiques"
        Department.HAUTESPYRENEES_65 -> "65 - Hautes-Pyrénées"
        Department.PYRENEESORIENTALES_66 -> "66 - Pyrénées-Orientales"
        Department.BASRHIN_67 -> "67 - Bas-Rhin"
        Department.HAUTRHIN_68 -> "68 - Haut-Rhin"
        Department.RHONE_69 -> "69 - Rhône"
        Department.HAUTESAONE_70 -> "70 - Haute-Saône"
        Department.SAONEETLOIRE_71 -> "71 - Saône-et-Loire"
        Department.SARTHE_72 -> "72 - Sarthe"
        Department.SAVOIE_73 -> "73 - Savoie"
        Department.HAUTESAVOIE_74 -> "74 - Haute-Savoie"
        Department.PARIS_75 -> "75 - Paris"
        Department.SEINEMARITIME_76 -> "76 - Seine-Maritime"
        Department.SEINEETMARNE_77 -> "77 - Seine-et-Marne"
        Department.YVELINES_78 -> "78 - Yvelines"
        Department.DEUXSEVRES_79 -> "79 - Deux-Sèvres"
        Department.SOMME_80 -> "80 - Somme"
        Department.TARN_81 -> "81 - Tarn"
        Department.TARNETGARONNE_82 -> "82 - Tarn-et-Garonne"
        Department.VAR_83 -> "83 - Var"
        Department.VAUCLUSE_84 -> "84 - Vaucluse"
        Department.VENDEE_85 -> "85 - Vendée"
        Department.VIENNE_86 -> "86 - Vienne"
        Department.HAUTEVIENNE_87 -> "87 - Haute-Vienne"
        Department.VOSGES_88 -> "88 - Vosges"
        Department.YONNE_89 -> "89 - Yonne"
        Department.TERRITOIREDEBELFORT_90 -> "90 - Territoire de Belfort"
        Department.ESSONNE_91 -> "91 - Essonne"
        Department.HAUTSDESEINE_92 -> "92 - Hauts-de-Seine"
        Department.SEINESAINTDENIS_93 -> "93 - Seine-Saint-Denis"
        Department.VALDEMARNE_94 -> "94 - Val-de-Marne"
        Department.VALDOISE_95 -> "95 - Val-d'Oise"
        Department.GUADELOUPE_971 -> "971 - Guadeloupe"
        Department.MARTINIQUE_972 -> "972 - Martinique"
        Department.GUYANE_973 -> "973 - Guyane"
        Department.LAREUNION_974 -> "974 - La Réunion"
        Department.MAYOTTE_976 -> "976 - Mayotte"
        Department.NOUVELLECALEDONIE_988 -> "988 - Nouvelle Calédonie"
        Department.POLYNESIEFRANCAISE_987 -> "987 - Polynésie Française"
        Department.SAINTBARTHELEMY_977 -> "977 - Saint-Barthélémy"
        Department.SAINTMARTIN_978 -> "978 - Saint-Martin"
        Department.SAINTPIERREETMIQUELON_975 -> "975 - Saint-Pierre-et-Miquelon"
        Department.TERRESAUSTRALESETANTARCTIQUESFRANCAISES_984 -> "984 - Terres australes et antarctiques françaises"
        Department.WALLISETFUTUNA_986 -> "986 - Wallis-et-Futuna"
        Department.ETRANGER_99 -> "Etranger - hors de France"
        null -> "N/A"
    }

    private fun CityType?.toPretty() = when (this) {
        CityType.RURAL -> "Rural"
        CityType.URBAIN -> "Urbain"
        CityType.AUTRE -> "Autre"
        null -> "N/A"
    }

    private fun JobCategory?.toPretty() = when (this) {
        JobCategory.AGRICULTEUR -> "Agriculteurs"
        JobCategory.ARTISAN -> "Artisans, commerçants, chefs d'entreprise"
        JobCategory.CADRE -> "Cadres"
        JobCategory.PROFESSION_INTERMEDIAIRE -> "Professions intermediaires"
        JobCategory.EMPLOYE -> "Employés"
        JobCategory.OUVRIER -> "Ouvriers"
        JobCategory.ETUDIANTS -> "Etudiants"
        JobCategory.RETRAITES -> "Retraités"
        JobCategory.AUTRESOUSANSACTIVITEPRO -> "Autres / Sans activité profesionnelle"
        JobCategory.UNKNOWN -> "Ne sait pas"
        null -> "N/A"
    }

    private fun Frequency?.toPretty() = when (this) {
        Frequency.SOUVENT -> "Souvent"
        Frequency.PARFOIS -> "Parfois"
        Frequency.JAMAIS -> "Jamais"
        null -> "N/A"
    }

}