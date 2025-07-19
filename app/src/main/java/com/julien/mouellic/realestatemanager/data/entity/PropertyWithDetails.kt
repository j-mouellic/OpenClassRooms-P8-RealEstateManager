package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PropertyWithDetails(
    @Embedded val propertyEntity: PropertyDTO,

    @Relation(
        parentColumn = "agent_id",
        entityColumn = "id"
    )
    val agent: AgentDTO?,

    @Relation(
        parentColumn = "location_id",
        entityColumn = "id"
    )
    val location: LocationDTO?,

    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val pictures: List<PictureDTO>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PropertyCommodityCrossRef::class,
            parentColumn = "property_id",
            entityColumn = "commodity_id"
        )
    )
    val commodities: List<CommodityDTO>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PropertyRealEstateTypeCrossRef::class,
            parentColumn = "property_id",
            entityColumn = "real_estate_type_id"
        )
    )
    val realEstateTypes: List<RealEstateTypeDTO>?,
)
