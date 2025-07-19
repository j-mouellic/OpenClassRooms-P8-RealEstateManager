package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "property_real_estate_type",
    foreignKeys = [
        ForeignKey(
            entity = PropertyDTO::class,
            parentColumns = ["id"],
            childColumns = ["property_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RealEstateTypeDTO::class,
            parentColumns = ["id"],
            childColumns = ["real_estate_type_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["property_id", "real_estate_type_id"],
    indices = [
        Index(value = ["property_id"]),
        Index(value = ["real_estate_type_id"])
    ]
)
data class PropertyRealEstateTypeCrossRef(
    val propertyId: Long,
    val realEstateTypeId: Long
)

