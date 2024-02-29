package com.example.edistynytmobiiliohjelmointiprojekti.model

import com.google.gson.annotations.SerializedName

data class RentalItemsState(
    val categoryName: String = "",
    val list: List<RentalItemsByCategory> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

data class RentalItemsByCategory(
    @SerializedName("rental_item_id")
    val rentalItemId: Int = 0,
    @SerializedName("rental_item_name")
    val rentalItemName: String = ""
)

data class RentalItemsRes(
    val rentalItems: List<RentalItemsByCategory> = emptyList()
)

data class RentalItemPostReq(
    @SerializedName("rental_item_name")
    val rentalItemName: String = "",
    @SerializedName("rental_item_description")
    val rentalItemDesc: String = "",
    @SerializedName("serial_number")
    val rentalItemSerial: String = ""
)

/*
data class RentalItem(
    @SerializedName("rental_item_id")
    val rentalItemId: Int = 0,
    @SerializedName("created_by_user")
    val createdByUser: User = User(),
    @SerializedName("category_category")
    val category: CategoryItem = CategoryItem(),
    @SerializedName("serial_number")
    val serialNumber: String = "",
    @SerializedName("rental_item_name")
    val rentalItemName: String = "",
    @SerializedName("created_at")
    val createdAt: Date,
    @SerializedName("rental_item_state_rental_item_state")
    val rentalState: RentalState = RentalState(),
    @SerializedName("rental_item_description")
    val rentalItemDesc: String = "",
    @SerializedName("deleted_at")
    val deletedAt: Date,
)
*/
