package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.julien.mouellic.realestatemanager.data.mapper.PropertyMapper
import com.julien.mouellic.realestatemanager.domain.model.Property
import org.threeten.bp.Instant

@Entity(
    tableName = "properties",
    foreignKeys = [
        ForeignKey(
            entity = AgentDTO::class,
            parentColumns = ["id"],
            childColumns = ["agent_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = LocationDTO::class,
            parentColumns = ["id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = RealEstateTypeDTO::class,
            parentColumns = ["id"],
            childColumns = ["real_estate_type_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["agent_id"]),
        Index(value = ["location_id"]),
        Index(value = ["type_id"])
    ]
)
data class PropertyDTO(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    val id: Long? = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "surfaced")
    val surface: Double?,

    @ColumnInfo(name = "numbers_of_rooms")
    val numbersOfRooms: Int?,

    @ColumnInfo(name = "numbers_of_bathrooms")
    val numbersOfBathrooms: Int?,

    @ColumnInfo(name = "numbers_of_bedrooms")
    val numbersOfBedrooms: Int?,

    @ColumnInfo(name = "price")
    val price: Double?,

    @ColumnInfo(name = "isSold")
    val isSold: Boolean,

    @ColumnInfo(name = "creation_date")
    val creationDate: Instant,

    @ColumnInfo(name = "entry_date")
    val entryDate: Instant?,

    @ColumnInfo(name = "sale_date")
    val saleDate: Instant?,

    @ColumnInfo(name = "apartment_number")
    val apartmentNumber: Int?,

    @ColumnInfo(name = "type_id")
    val typeId: Long?,

    @ColumnInfo(name = "location_id")
    val locationId: Long?,

    @ColumnInfo(name = "agent_id")
    val agentId: Long?,
){
    @Ignore
    fun toModel(): Property {
        return PropertyMapper().dtoToModel(this)
    }
}
