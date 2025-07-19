package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "property_commodity",
    foreignKeys = [
        ForeignKey(
            entity = PropertyDTO::class,
            parentColumns = ["id"],
            childColumns = ["property_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CommodityDTO::class,
            parentColumns = ["id"],
            childColumns = ["commodity_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["property_id", "commodity_id"],
    indices = [
        Index(value = ["property_id"]),
        Index(value = ["commodity_id"])
    ]
)
data class PropertyCommodityCrossRef(
    val propertyId: Long,
    val commodityId: Long
)
