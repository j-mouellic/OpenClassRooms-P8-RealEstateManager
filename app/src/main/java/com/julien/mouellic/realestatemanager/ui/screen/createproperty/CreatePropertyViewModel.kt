package com.julien.mouellic.realestatemanager.ui.screen.createproperty

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julien.mouellic.realestatemanager.domain.model.Agent
import com.julien.mouellic.realestatemanager.domain.model.Commodity
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType
import com.julien.mouellic.realestatemanager.domain.model.Location
import com.julien.mouellic.realestatemanager.domain.model.Picture
import com.julien.mouellic.realestatemanager.domain.model.Property
import com.julien.mouellic.realestatemanager.domain.usecase.agent.GetAllAgentsUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.commodity.GetAllCommoditiesUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.estatetype.GetAllEstateTypesUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.GetFullPropertyUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.InsertEasyPropertyUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.InsertPropertyUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.UpdatePropertyUseCase
import com.julien.mouellic.realestatemanager.ui.form.converter.FormConverter
import com.julien.mouellic.realestatemanager.ui.form.formater.FormFormater
import com.julien.mouellic.realestatemanager.ui.form.state.FieldState
import com.julien.mouellic.realestatemanager.ui.form.state.InstantFieldState
import com.julien.mouellic.realestatemanager.ui.form.state.LocationFormState
import com.julien.mouellic.realestatemanager.ui.form.validator.FormValidator
import com.julien.mouellic.realestatemanager.utils.BitmapUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import javax.inject.Inject

@HiltViewModel
class CreatePropertyViewModel @Inject constructor(
    private val insertEasyPropertyUseCase: InsertEasyPropertyUseCase,
    private val updateEasyPropertyUseCase: UpdateEasyPropertyUseCase,
    private val getAllEstateTypesUseCase: GetAllEstateTypesUseCase,
    private val getAllCommoditiesUseCase: GetAllCommoditiesUseCase,
    private val getAllAgentsUseCase: GetAllAgentsUseCase,
    private val getPropertyWithDetailsUseCase: GetPropertyWithDetailsUseCase,
    private val formValidator : FormValidator,
    private val formConverter: FormConverter,
    private val formFormater : FormFormater
) : ViewModel() {

    companion object {
        private const val TAG = "CreatePropertyViewModel"

        private const val NAME_IS_REQUIRED = true
        private const val NAME_MIN = 1
        private const val NAME_MAX = 100

        private const val DESCRIPTION_IS_REQUIRED = false
        private const val DESCRIPTION_MIN = 1
        private const val DESCRIPTION_MAX = 1000

        private const val SURFACE_IS_REQUIRED = false
        private const val SURFACE_MIN = 1.0
        private const val SURFACE_MAX = 10000.0

        private const val NB_ROOMS_IS_REQUIRED = false
        private const val NB_ROOMS_MIN = 1
        private const val NB_ROOMS_MAX = 100

        private const val NB_BATHROOMS_IS_REQUIRED = false
        private const val NB_BATHROOMS_MIN = 0
        private const val NB_BATHROOMS_MAX = 100

        private const val NB_BEDROOMS_IS_REQUIRED = false
        private const val NB_BEDROOMS_MIN = 0
        private const val NB_BEDROOMS_MAX = 100

        private const val PRICE_ALLOW_IS_REQUIRED = false
        private const val PRICE_MIN = 1.0
        private const val PRICE_MAX = 10000000.0

        private const val APARTMENT_NUMBER_IS_REQUIRED = false
        private const val APARTMENT_NUMBER_MIN = 1
        private const val APARTMENT_NUMBER_MAX = 100000

        private const val SALE_DATE_IS_REQUIRED = false
        private const val ENTRY_DATE_IS_REQUIRED = false
        private val SALE_DATE_MIN : Instant? = null
        private val SALE_DATE_MAX : Instant? = null
        private val ENTRY_DATE_MIN : Instant? = null
        private val ENTRY_DATE_MAX : Instant? = null

        private const val LOCATION_STREET_IS_REQUIRED = true
        private const val LOCATION_STREET_MIN = 1
        private const val LOCATION_STREET_MAX = 200

        private const val LOCATION_NUMBER_IS_REQUIRED = false
        private const val LOCATION_NUMBER_MIN = 1
        private const val LOCATION_NUMBER_MAX = 100000

        private const val LOCATION_POSTAL_CODE_IS_REQUIRED = true
        private const val LOCATION_POSTAL_CODE_MIN = 1
        private const val LOCATION_POSTAL_CODE_MAX = 10

        private const val LOCATION_CITY_IS_REQUIRED = true
        private const val LOCATION_CITY_MIN = 1
        private const val LOCATION_CITY_MAX = 200

        private const val LOCATION_COUNTRY_IS_REQUIRED = true
        private const val LOCATION_COUNTRY_MIN = 1
        private const val LOCATION_COUNTRY_MAX = 200

        private const val LOCATION_LONGITUDE_IS_REQUIRED = false
        private const val LOCATION_LONGITUDE_MIN = -180.0
        private const val LOCATION_LONGITUDE_MAX = 180.0

        private const val LOCATION_LATITUDE_IS_REQUIRED = false
        private const val LOCATION_LATITUDE_MIN = -180.0
        private const val LOCATION_LATITUDE_MAX = 180.0

        private const val THUMBNAIL_MAX_WIDTH = 400
        private const val THUMBNAIL_MAX_HEIGHT = 400

        private const val CONTENT_MAX_WIDTH = 1000
        private const val CONTENT_MAX_HEIGHT = 1000
    }

    private val _uiState = MutableStateFlow<CreatePropertyUIState>(
        CreatePropertyUIState.FormState(
            name = FieldState("", true),
            description = FieldState("", true),
            surface = FieldState("", true),
            nbRooms = FieldState("", true),
            nbBathrooms = FieldState("", true),
            nbBedrooms = FieldState("", true),
            price = FieldState("", true),
            entryDate = InstantFieldState(null, true),
            saleDate = InstantFieldState(null, true),
            apartmentNumber = FieldState("", true),
            location = LocationFormState(
                street = FieldState("", true),
                number = FieldState("", true),
                postalCode = FieldState("", true),
                city = FieldState("", true),
                country = FieldState("", true),
                longitude = FieldState("", true),
                latitude = FieldState("", true)
            ),
            isFormValid = false,
            pictures = emptyList()
        )
    )
    val uiState: StateFlow<CreatePropertyUIState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val estateTypes = getAllEstateTypesUseCase()
            val commodities = getAllCommoditiesUseCase()
            val agents = getAllAgentsUseCase()

            if(estateTypes.isSuccess && commodities.isSuccess && agents.isSuccess) {
                val currentState = getFormState()
                _uiState.value = currentState.copy(
                    allEstateTypes = estateTypes.getOrNull() ?: emptyList(),
                    allCommodities = commodities.getOrNull() ?: emptyList(),
                    allAgents = agents.getOrNull() ?: emptyList()
                )
            } else {
                _uiState.value = CreatePropertyUIState.Error(sError = "Unable to fetch data", formState = getFormState())
            }

        }
    }

    private fun getFormState(): CreatePropertyUIState.FormState {
        return when (val currentState = _uiState.value) {
            is CreatePropertyUIState.IsLoading -> currentState.formState
            is CreatePropertyUIState.Error -> currentState.formState
            is CreatePropertyUIState.FormState -> currentState
            else -> throw IllegalStateException("Unknown state")
        }
    }

    fun updateFieldValue(fieldName: String, instant: Instant?){
        viewModelScope.launch {
            val currentState = getFormState()
            val updatedState = when (fieldName) {
                "entryDate" -> currentState.copy(entryDate = formValidator.validateInstant(instant, ENTRY_DATE_MIN, ENTRY_DATE_MAX, ENTRY_DATE_IS_REQUIRED))
                "saleDate" -> currentState.copy(saleDate = formValidator.validateInstant(instant, SALE_DATE_MIN, SALE_DATE_MAX, SALE_DATE_IS_REQUIRED))
                else -> throw IllegalArgumentException("Unknown field name")
            }
            val isFormValid = isFormValid(updatedState)
            _uiState.value = updatedState.copy(isFormValid = isFormValid)
        }
    }

    fun updateFieldValue(fieldName: String, newValue: String) {
        viewModelScope.launch {
            val currentState = getFormState()
            val updatedState = when (fieldName) {
                "name" -> currentState.copy(name = formValidator.validateString(newValue, NAME_MIN, NAME_MAX, NAME_IS_REQUIRED))
                "description" -> currentState.copy(description = formValidator.validateString(newValue, DESCRIPTION_MIN, DESCRIPTION_MAX, DESCRIPTION_IS_REQUIRED))
                "surface" -> currentState.copy(surface = formValidator.validateDouble(newValue, SURFACE_MIN, SURFACE_MAX, SURFACE_IS_REQUIRED))
                "nbRooms" -> currentState.copy(nbRooms = formValidator.validateInteger(newValue, NB_ROOMS_MIN, NB_ROOMS_MAX, NB_ROOMS_IS_REQUIRED))
                "nbBathrooms" -> currentState.copy(nbBathrooms = formValidator.validateInteger(newValue,NB_BATHROOMS_MIN, NB_BATHROOMS_MAX, NB_BATHROOMS_IS_REQUIRED))
                "nbBedrooms" -> currentState.copy(nbBedrooms = formValidator.validateInteger(newValue,NB_BEDROOMS_MIN, NB_BEDROOMS_MAX, NB_BEDROOMS_IS_REQUIRED))
                "price" -> currentState.copy(price = formValidator.validateDouble(newValue, PRICE_MIN, PRICE_MAX, PRICE_ALLOW_IS_REQUIRED))
                "apartmentNumber" -> currentState.copy(apartmentNumber = formValidator.validateInteger(newValue,APARTMENT_NUMBER_MIN, APARTMENT_NUMBER_MAX, APARTMENT_NUMBER_IS_REQUIRED))
                "location.street" -> currentState.copy(location = currentState.location.copy(street = formValidator.validateString(newValue, LOCATION_STREET_MIN, LOCATION_STREET_MAX, LOCATION_STREET_IS_REQUIRED)))
                "location.number" -> currentState.copy(location = currentState.location.copy(number = formValidator.validateInteger(newValue,LOCATION_NUMBER_MIN, LOCATION_NUMBER_MAX, LOCATION_NUMBER_IS_REQUIRED)))
                "location.postalCode" -> currentState.copy(location = currentState.location.copy(postalCode = formValidator.validateString(newValue, LOCATION_POSTAL_CODE_MIN, LOCATION_POSTAL_CODE_MAX, LOCATION_POSTAL_CODE_IS_REQUIRED)))
                "location.city" -> currentState.copy(location = currentState.location.copy(city = formValidator.validateString(newValue, LOCATION_CITY_MIN, LOCATION_CITY_MAX, LOCATION_CITY_IS_REQUIRED)))
                "location.country" -> currentState.copy(location = currentState.location.copy(country = formValidator.validateString(newValue, LOCATION_COUNTRY_MIN, LOCATION_COUNTRY_MAX, LOCATION_COUNTRY_IS_REQUIRED)))
                "location.longitude" -> currentState.copy(location = currentState.location.copy(longitude = formValidator.validateDouble(newValue, LOCATION_LONGITUDE_MIN, LOCATION_LONGITUDE_MAX, LOCATION_LONGITUDE_IS_REQUIRED)))
                "location.latitude" -> currentState.copy(location = currentState.location.copy(latitude = formValidator.validateDouble(newValue, LOCATION_LATITUDE_MIN, LOCATION_LATITUDE_MAX, LOCATION_LATITUDE_IS_REQUIRED)))
                else -> throw IllegalArgumentException("Unknown field name")
            }
            val isFormValid = isFormValid(updatedState)
            _uiState.value = updatedState.copy(isFormValid = isFormValid)
        }
    }

    private fun validateAll(){
        var currentState = getFormState()

        currentState = currentState.copy(
            name = formValidator.validateString(currentState.name.value, NAME_MIN, NAME_MAX, NAME_IS_REQUIRED),
            description = formValidator.validateString(currentState.description.value, DESCRIPTION_MIN, DESCRIPTION_MAX, DESCRIPTION_IS_REQUIRED),
            surface = formValidator.validateDouble(currentState.surface.value, SURFACE_MIN, SURFACE_MAX, SURFACE_IS_REQUIRED),
            nbRooms = formValidator.validateInteger(currentState.nbRooms.value,NB_ROOMS_MIN, NB_ROOMS_MAX, NB_ROOMS_IS_REQUIRED),
            nbBathrooms = formValidator.validateInteger(currentState.nbBathrooms.value,NB_BATHROOMS_MIN, NB_BATHROOMS_MAX, NB_BATHROOMS_IS_REQUIRED),
            nbBedrooms = formValidator.validateInteger(currentState.nbBedrooms.value,NB_BEDROOMS_MIN, NB_BEDROOMS_MAX, NB_BEDROOMS_IS_REQUIRED),
            price = formValidator.validateDouble(currentState.price.value, PRICE_MIN, PRICE_MAX, PRICE_ALLOW_IS_REQUIRED),
            entryDate = formValidator.validateInstant(currentState.entryDate.value, ENTRY_DATE_MIN, ENTRY_DATE_MAX, ENTRY_DATE_IS_REQUIRED),
            saleDate = formValidator.validateInstant(currentState.saleDate.value, SALE_DATE_MIN, SALE_DATE_MAX, SALE_DATE_IS_REQUIRED ),
            apartmentNumber = formValidator.validateInteger(currentState.apartmentNumber.value,APARTMENT_NUMBER_MIN, APARTMENT_NUMBER_MAX, APARTMENT_NUMBER_IS_REQUIRED),
            location = currentState.location.copy(
                street = formValidator.validateString(currentState.location.street.value, LOCATION_STREET_MIN, LOCATION_STREET_MAX, LOCATION_STREET_IS_REQUIRED),
                number = formValidator.validateInteger(currentState.location.number.value,LOCATION_NUMBER_MIN, LOCATION_NUMBER_MAX, LOCATION_NUMBER_IS_REQUIRED),
                postalCode = formValidator.validateString(currentState.location.postalCode.value, LOCATION_POSTAL_CODE_MIN, LOCATION_POSTAL_CODE_MAX, LOCATION_POSTAL_CODE_IS_REQUIRED),
                city = formValidator.validateString(currentState.location.city.value, LOCATION_CITY_MIN, LOCATION_CITY_MAX, LOCATION_CITY_IS_REQUIRED),
                country = formValidator.validateString(currentState.location.country.value, LOCATION_COUNTRY_MIN, LOCATION_COUNTRY_MAX, LOCATION_COUNTRY_IS_REQUIRED),
                longitude = formValidator.validateDouble(currentState.location.longitude.value, LOCATION_LONGITUDE_MIN, LOCATION_LONGITUDE_MAX, LOCATION_LONGITUDE_IS_REQUIRED),
                latitude = formValidator.validateDouble(currentState.location.latitude.value, LOCATION_LATITUDE_MIN, LOCATION_LATITUDE_MAX, LOCATION_LATITUDE_IS_REQUIRED)
            )
        )

        currentState = currentState.copy(isFormValid = isFormValid(currentState))
        _uiState.value = currentState
    }

    private fun isFormValid(updatedState: CreatePropertyUIState. FormState): Boolean {
        Log.d(TAG, "name: ${updatedState.name.isValid}")
        Log.d(TAG, "description: ${updatedState.description.isValid}")
        Log.d(TAG, "surface: ${updatedState.surface.isValid}")
        Log.d(TAG, "nbRooms: ${updatedState.nbRooms.isValid}")
        Log.d(TAG, "nbBathrooms: ${updatedState.nbBathrooms.isValid}")
        Log.d(TAG, "nbBedrooms: ${updatedState.nbBedrooms.isValid}")
        Log.d(TAG, "price: ${updatedState.price.isValid}")
        Log.d(TAG, "entryDate: ${updatedState.entryDate.isValid}")
        Log.d(TAG, "saleDate: ${updatedState.saleDate.isValid}")
        Log.d(TAG, "apartmentNumber: ${updatedState.apartmentNumber.isValid}")
        Log.d(TAG, "location.street: ${updatedState.location.street.isValid}")
        Log.d(TAG, "location.number: ${updatedState.location.number.isValid}")
        Log.d(TAG, "location.postalCode: ${updatedState.location.postalCode.isValid}")
        Log.d(TAG, "location.city: ${updatedState.location.city.isValid}")
        Log.d(TAG, "location.country: ${updatedState.location.country.isValid}")
        Log.d(TAG, "location.longitude: ${updatedState.location.longitude.isValid}")
        Log.d(TAG, "location.latitude: ${updatedState.location.latitude.isValid}")
        Log.d(TAG, "selectedAgent: ${updatedState.selectedAgent != null}")
        Log.d(TAG, "selectedEstateType: ${updatedState.selectedEstateType != null}")

        return  updatedState.name.isValid &&
                updatedState.description.isValid &&
                updatedState.surface.isValid &&
                updatedState.nbRooms.isValid &&
                updatedState.nbBathrooms.isValid &&
                updatedState.nbBedrooms.isValid &&
                updatedState.price.isValid &&
                updatedState.entryDate.isValid &&
                updatedState.saleDate.isValid &&
                updatedState.apartmentNumber.isValid &&
                updatedState.location.street.isValid &&
                updatedState.location.number.isValid &&
                updatedState.location.postalCode.isValid &&
                updatedState.location.city.isValid &&
                updatedState.location.country.isValid &&
                updatedState.location.longitude.isValid &&
                updatedState.location.latitude.isValid &&
                updatedState.selectedAgent != null &&
                updatedState.selectedEstateType != null
    }

    fun saveProperty() {
        viewModelScope.launch {
            validateAll()
            val currentState = getFormState()
            if (currentState.isFormValid) {
                val property = Property(
                    id = if(editModeIDLoaded > 0) editModeIDLoaded else null,
                    name = currentState.name.value,
                    description = formConverter.toString(currentState.description.value),
                    surface = formConverter.toDouble(currentState.surface.value),
                    nbRooms = formConverter.toInt(currentState.nbRooms.value),
                    nbBathrooms = formConverter.toInt(currentState.nbBathrooms.value),
                    nbBedrooms = formConverter.toInt(currentState.nbBedrooms.value),
                    price = formConverter.toDouble(currentState.price.value),
                    isSold = false,
                    creationDate = Instant.now(),
                    entryDate = currentState.entryDate.value,
                    saleDate = currentState.saleDate.value,
                    apartmentNumber = formConverter.toInt(currentState.apartmentNumber.value),
                    type = currentState.selectedEstateType!!,
                    location = Location(
                        id = null,
                        street = currentState.location.street.value,
                        number = formConverter.toInt(currentState.location.number.value),
                        postalCode = currentState.location.postalCode.value,
                        city = currentState.location.city.value,
                        country = currentState.location.country.value,
                        longitude = formConverter.toDouble(currentState.location.longitude.value),
                        latitude = formConverter.toDouble(currentState.location.latitude.value)
                    ),
                    agent = currentState.selectedAgent!!,
                    commodities = currentState.selectedCommodities,
                    pictures = currentState.pictures
                )

                _uiState.value = CreatePropertyUIState.IsLoading(formState = getFormState())

                try {
                    if(editModeIDLoaded > 0){
                        updateEasyPropertyUseCase(property)
                        _uiState.value = CreatePropertyUIState.Success(propertyId = editModeIDLoaded)
                    } else {
                        val propertyId = insertEasyPropertyUseCase(property)
                        _uiState.value = CreatePropertyUIState.Success(propertyId = propertyId)
                    }
                } catch (e: Exception) {
                    _uiState.value = CreatePropertyUIState.Error(sError = e.message, formState = getFormState())
                }
            }
        }
    }

    fun addPictures(bitmaps: List<Bitmap>) {
        viewModelScope.launch {
            val newPictures = bitmaps.mapIndexed { index, bitmap ->
                Picture(
                    id = null,
                    description = null,
                    content = BitmapUtils.resize(
                        bitmap,
                        CONTENT_MAX_WIDTH,
                        CONTENT_MAX_HEIGHT
                    ),
                    thumbnailContent = BitmapUtils.resize(
                        bitmap,
                        THUMBNAIL_MAX_WIDTH,
                        THUMBNAIL_MAX_HEIGHT
                    ),
                    order = index
                )
            }
            var currentState = getFormState()
            val pictures = currentState.pictures + newPictures
            val rearrangePictures = pictures.mapIndexed { index, picture ->
                picture.copy(order = index)
            }
            currentState = currentState.copy(pictures = rearrangePictures)
            val isFormValid = isFormValid(currentState)
            _uiState.value = currentState.copy(isFormValid = isFormValid)
        }
    }

    fun deletePicture(picture: Picture) {
        viewModelScope.launch {
            var currentState = getFormState()
            val updatedPictures = currentState.pictures.filter { it != picture }
            currentState = currentState.copy(pictures = updatedPictures)
            val isFormValid = isFormValid(currentState)
            _uiState.value = currentState.copy(isFormValid = isFormValid)
        }
    }

    private fun logPicturesOrder(){
        // DEBUG ONLY
        val currentState = getFormState()
        val pictures = currentState.pictures.map {
            val content = it.content
            val description : String = content.width.toString() + "x" + content.height.toString()
            Picture(
                id = it.id,
                description = description,
                content = BitmapUtils.create(1, 1),
                thumbnailContent = BitmapUtils.create(1, 1),
                order = it.order
            )
        }
        for(picture in pictures){
            Log.d(TAG, "Picture: ${picture.description} - Order: ${picture.order}")
        }
    }

    fun movePictureUp(picture: Picture) {
        viewModelScope.launch {
            val currentState = getFormState()
            val index = currentState.pictures.indexOf(picture)
            if (index > 0) {
                val newPictures = currentState.pictures.toMutableList()
                newPictures.removeAt(index)
                newPictures.add(index - 1, picture)
                val rearrangePictures = newPictures.mapIndexed { idx, picture ->
                    picture.copy(order = idx)
                }
                _uiState.value = currentState.copy(pictures = rearrangePictures)
            }
            logPicturesOrder()
        }
    }

    fun movePictureDown(picture: Picture) {
        viewModelScope.launch {
            val currentState = getFormState()
            val index = currentState.pictures.indexOf(picture)
            if (index < currentState.pictures.size - 1) {
                val newPictures = currentState.pictures.toMutableList()
                newPictures.removeAt(index)
                newPictures.add(index + 1, picture)
                val rearrangePictures = newPictures.mapIndexed { idx, picture ->
                    picture.copy(order = idx)
                }
                _uiState.value = currentState.copy(pictures = rearrangePictures)
            }
            logPicturesOrder()
        }
    }

    fun updateSelectedAgent(agent: Agent) {
        viewModelScope.launch {
            var currentState = getFormState()
            currentState = currentState.copy(selectedAgent = agent)
            val isFormValid = isFormValid(currentState)
            _uiState.value = currentState.copy(isFormValid = isFormValid)
        }
    }

    fun updateSelectedEstateType(it: EstateType) {
        viewModelScope.launch {
            var currentState = getFormState()
            currentState = currentState.copy(selectedEstateType = it)
            val isFormValid = isFormValid(currentState)
            _uiState.value = currentState.copy(isFormValid = isFormValid)
        }
    }

    fun updateSelectedCommodities(it: List<Commodity>) {
        viewModelScope.launch {
            var currentState = getFormState()
            currentState = currentState.copy(selectedCommodities = it)
            val isFormValid = isFormValid(currentState)
            _uiState.value = currentState.copy(isFormValid = isFormValid)
        }
    }

    private var editModeIDLoaded = 0L
    fun loadForEditing(propertyId: Long?) {
        if (propertyId != null && propertyId != editModeIDLoaded) {
            editModeIDLoaded = propertyId
            viewModelScope.launch {
                _uiState.value = CreatePropertyUIState.IsLoading(formState = getFormState())
                val property = getFullPropertyUseCase(propertyId)
                if (property != null) {
                    val location : LocationFormState? = if (property.location != null){
                        val propertyLocation = property.location
                        LocationFormState(
                            street = FieldState(propertyLocation.street, true),
                            number = FieldState(formFormater.formatInt(propertyLocation.number), true),
                            postalCode = FieldState(propertyLocation.postalCode, true),
                            city = FieldState(propertyLocation.city, true),
                            country = FieldState(propertyLocation.country, true),
                            longitude = FieldState(formFormater.formatDouble(propertyLocation.longitude), true),
                            latitude = FieldState(formFormater.formatDouble(propertyLocation.latitude), true)
                        )
                    } else {
                        null
                    }
                    val currentState = getFormState()
                    _uiState.value = currentState.copy(
                        name = FieldState(property.name, true),
                        description = FieldState(formFormater.formatString(property.description), true),
                        surface = FieldState(formFormater.formatDouble(property.surface), true),
                        nbRooms = FieldState(formFormater.formatInt(property.nbRooms), true),
                        nbBathrooms = FieldState(formFormater.formatInt(property.nbBathrooms), true),
                        nbBedrooms = FieldState(formFormater.formatInt(property.nbBedrooms), true),
                        price = FieldState(formFormater.formatDouble(property.price), true),
                        entryDate = InstantFieldState(property.entryDate, true),
                        saleDate = InstantFieldState(property.saleDate, true),
                        apartmentNumber = FieldState(formFormater.formatInt(property.apartmentNumber), true),
                        location = location!!,
                        selectedAgent = property.agent,
                        selectedEstateType = property.type,
                        selectedCommodities = property.commodities,
                        pictures = property.pictures.sortedBy { it.order }.mapIndexed { index, picture ->
                            picture.copy(order = index)
                        }
                    )
                    validateAll()
                } else {
                    _uiState.value = CreatePropertyUIState.Error(sError = "Unable to fetch data", formState = getFormState())
                }
            }
        }
    }

}

