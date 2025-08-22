package com.julien.mouellic.realestatemanager.domain.usecase.realestatetype

import com.julien.mouellic.realestatemanager.data.repository.RealEstateTypeRepository
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType
import javax.inject.Inject

class GetAllEstateTypesUseCase @Inject constructor(
    private val realEstateTypeRepository: RealEstateTypeRepository
) {
    suspend operator fun invoke(): Result<List<RealEstateType>> {
        return try {
            Result.success(realEstateTypeRepository.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}