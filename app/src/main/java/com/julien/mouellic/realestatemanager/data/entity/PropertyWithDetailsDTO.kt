package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.julien.mouellic.realestatemanager.data.dao.RealEstateTypeDAO
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType

data class PropertyWithDetailsDTO(
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
        parentColumn = "real_estate_type_id",
        entityColumn = "id"
    )
    val realEstateType: RealEstateTypeDTO?,

    @Relation(
        parentColumn = "picture_id",
        entityColumn = "id"
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
)
