package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.julien.mouellic.realestatemanager.data.mapper.AgentMapper
import com.julien.mouellic.realestatemanager.domain.model.Agent
import com.julien.mouellic.realestatemanager.domain.model.Location

@Entity(
    tableName = "agents"
)
data class AgentDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Long? = 0,

    @ColumnInfo(name = "first_name")
    val firstName : String,

    @ColumnInfo(name = "last_name")
    val lastName : String,

    @ColumnInfo(name = "email")
    val email : String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber : String,

    @ColumnInfo(name = "real_estate_agency")
    val realEstateAgency : String
){
    @Ignore
    fun toModel(): Agent {
        return AgentMapper().dtoToModel(this)
    }
}
