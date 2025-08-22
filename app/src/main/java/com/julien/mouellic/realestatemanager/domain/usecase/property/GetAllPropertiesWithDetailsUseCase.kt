package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyWithDetailsRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

class GetAllPropertiesWithDetailsUseCase @Inject constructor(
    private val propertyWithDetailsRepository: PropertyWithDetailsRepository
) {
    suspend operator fun invoke(): List<Property> {
        return propertyWithDetailsRepository.getAll()
    }
}