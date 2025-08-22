package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyWithDetailsRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

class GetPropertyWithDetailsUseCase @Inject constructor(
    private val propertyWithDetailsRepository: PropertyWithDetailsRepository
) {
    suspend operator fun invoke(propertyId: Long) : Property? {
        return propertyWithDetailsRepository.getById(propertyId)
    }
}