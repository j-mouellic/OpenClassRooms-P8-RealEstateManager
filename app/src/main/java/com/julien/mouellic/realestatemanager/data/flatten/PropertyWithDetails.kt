package com.julien.mouellic.realestatemanager.data.flatten

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation
import com.julien.mouellic.realestatemanager.data.entity.AgentDTO
import com.julien.mouellic.realestatemanager.data.entity.CommodityDTO
import com.julien.mouellic.realestatemanager.data.entity.LocationDTO
import com.julien.mouellic.realestatemanager.data.entity.PictureDTO
import com.julien.mouellic.realestatemanager.data.entity.PropertyCommodityCrossRefDTO
import com.julien.mouellic.realestatemanager.data.entity.PropertyDTO
import com.julien.mouellic.realestatemanager.data.entity.RealEstateTypeDTO
import com.julien.mouellic.realestatemanager.data.mapper.PropertyWithDetailsMapper
import com.julien.mouellic.realestatemanager.domain.model.Property

/**
 * Complete representation of a Property with all related entities.
 *
 * - Used for detailed views where all information about a property is needed.
 * - Combines PropertyDTO with:
 *   - AgentDTO
 *   - LocationDTO
 *   - RealEstateTypeDTO
 *   - Pictures (1:N)
 *   - Commodities (N:N via PropertyCommodityCrossRefDTO)
 *
 * Room annotations:
 * - @Embedded: includes the PropertyDTO fields directly
 * - @Relation: fetches related entities automatically
 * - @Relation with Junction: handles the N:N relationship for commodities
 *
 * Functions:
 * - `toModel()`: Converts the full DTO with all relations into the domain model `Property`.
 */
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
        parentColumn = "real_estate_type_id",
        entityColumn = "id"
    )
    val realEstateType: RealEstateTypeDTO?,

    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val pictures: List<PictureDTO>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PropertyCommodityCrossRefDTO::class,
            parentColumn = "property_id",
            entityColumn = "commodity_id"
        )
    )
    val commodities: List<CommodityDTO>,
){
    @Ignore
    fun toModel(): Property {
        return PropertyWithDetailsMapper().dtoToModel(this)
    }
}