package com.siddharthchordia.travelassistant.model

data class AttractionDetails(
    val xid: String?,
    val name: String?,
    val address: Address?,
    val rate: String?,
    val wikidata: String?,
    val kinds: String?,
    val sources: Sources?,
    val otm: String?,
    val wikipedia: String?,
    val image: String?,
    val preview: Preview?,
    val wikipedia_extracts: WikipediaExtracts?,
    val point: Point?
) {
    val description: String
        get() = wikipedia_extracts?.text.orEmpty()
}


data class Address(
    val city: String?,
    val road: String?,
    val state: String?,
    val country: String?,
    val postcode: String?,
    val country_code: String?,
    val house_number: String?,
    val city_district: String?,
    val state_district: String?
)

data class Sources(
    val geometry: String?,
    val attributes: List<String>?
)

data class Preview(
    val source: String?,
    val height: Int?,
    val width: Int?
)

data class WikipediaExtracts(
    val title: String?,
    val text: String?,
    val html: String?
)

