package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyRepository
import javax.inject.Inject

class DeletePropertyUseCase @Inject constructor(private val propertyRepository: PropertyRepository) {
    suspend operator fun invoke(propertyId: Long) {
        propertyRepository.deleteById(propertyId)
    }
}