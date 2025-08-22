package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

class GetAllPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke() : Result<List<Property>>{
        return try {
            Result.success(propertyRepository.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}