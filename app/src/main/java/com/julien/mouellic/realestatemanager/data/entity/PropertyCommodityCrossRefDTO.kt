package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.ColumnInfo
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
data class PropertyCommodityCrossRefDTO(
    @ColumnInfo(name = "property_id")
    val propertyId: Long,

    @ColumnInfo(name = "commodity_id")
    val commodityId: Long
)
