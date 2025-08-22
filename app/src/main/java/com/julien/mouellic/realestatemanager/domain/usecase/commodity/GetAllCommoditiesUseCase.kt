package com.julien.mouellic.realestatemanager.domain.usecase.commodity

import com.julien.mouellic.realestatemanager.data.repository.CommodityRepository
import com.julien.mouellic.realestatemanager.domain.model.Commodity
import javax.inject.Inject

class GetAllCommoditiesUseCase @Inject constructor(
    private val commodityRepository: CommodityRepository
) {
    suspend operator fun invoke(): Result<List<Commodity>> {
        return try {
            Result.success(commodityRepository.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}