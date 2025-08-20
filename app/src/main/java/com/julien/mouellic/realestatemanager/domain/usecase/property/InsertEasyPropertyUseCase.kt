package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.EasyPropertyRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

class InsertEasyPropertyUseCase @Inject constructor(
    private val easyPropertyRepository: EasyPropertyRepository
) {
    suspend operator fun invoke(property: Property): Long {
        return easyPropertyRepository.insert(property)
    }
}