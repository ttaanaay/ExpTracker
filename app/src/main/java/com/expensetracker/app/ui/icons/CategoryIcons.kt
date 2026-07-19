package com.expensetracker.app.ui.icons

import androidx.annotation.DrawableRes
import com.expensetracker.app.R

/**
 * ชุดไอคอนหมวดหมู่ทั้งหมด 140 แบบ (ไฟล์อยู่ที่ res/drawable, ตั้งชื่อ ic_xxx.png)
 * category.icon ในฐานข้อมูลเก็บเป็นชื่อ key ตรงนี้ (String) เช่น "ic_food"
 * แทนที่จะเก็บเป็น emoji แบบเดิม
 */
object CategoryIcons {

    /** ใช้เป็นไอคอน fallback เมื่อหาชื่อไม่เจอ หรือหมวดหมู่ไม่มีไอคอน */
    const val DEFAULT: String = "ic_package"

    /** รายชื่อไอคอนทั้งหมด เรียงตามหมวดหมู่ในภาพต้นฉบับ ใช้แสดงใน icon picker */
    val ALL: List<String> = listOf(
        "ic_account_protect",
        "ic_add_circle",
        "ic_alarm",
        "ic_analytics",
        "ic_announcement",
        "ic_atm",
        "ic_beauty",
        "ic_bicycle",
        "ic_bird",
        "ic_book",
        "ic_briefcase",
        "ic_bus",
        "ic_cactus",
        "ic_calendar",
        "ic_camera",
        "ic_car",
        "ic_car_insurance",
        "ic_cart",
        "ic_cash",
        "ic_cash_income",
        "ic_cat",
        "ic_charity",
        "ic_chart_growth",
        "ic_chart_pie",
        "ic_chat",
        "ic_checklist",
        "ic_clothing",
        "ic_cloud_sync",
        "ic_coffee",
        "ic_coins",
        "ic_credit_card",
        "ic_database",
        "ic_dining",
        "ic_discount",
        "ic_dislike",
        "ic_dog",
        "ic_donation",
        "ic_drink",
        "ic_eco_home",
        "ic_education",
        "ic_electricity_bill",
        "ic_email",
        "ic_error",
        "ic_fastfood",
        "ic_favorite",
        "ic_fish",
        "ic_fitness",
        "ic_flight",
        "ic_flowers",
        "ic_folder",
        "ic_food",
        "ic_fuel",
        "ic_gaming",
        "ic_gardening",
        "ic_gas_bill",
        "ic_gift",
        "ic_gift_alt",
        "ic_global",
        "ic_goal_target",
        "ic_haircare",
        "ic_happy",
        "ic_health",
        "ic_health_insurance",
        "ic_home",
        "ic_hotel",
        "ic_idea",
        "ic_income",
        "ic_insurance",
        "ic_internet",
        "ic_investment",
        "ic_key",
        "ic_key_alt",
        "ic_lab",
        "ic_laptop",
        "ic_laundry",
        "ic_like",
        "ic_lock",
        "ic_luggage",
        "ic_medical_record",
        "ic_medicine",
        "ic_memorial",
        "ic_mobile_payment",
        "ic_mortgage",
        "ic_motorbike",
        "ic_movie",
        "ic_movie_snack",
        "ic_music_headphones",
        "ic_music_note",
        "ic_office_building",
        "ic_online_course",
        "ic_package",
        "ic_painting",
        "ic_parking",
        "ic_pending",
        "ic_percent_off",
        "ic_pet",
        "ic_phone",
        "ic_photo",
        "ic_pizza",
        "ic_plant",
        "ic_printer",
        "ic_reading",
        "ic_receipt",
        "ic_refund",
        "ic_repair",
        "ic_road_toll",
        "ic_sad",
        "ic_safe",
        "ic_savings",
        "ic_savings_goal",
        "ic_screwdriver",
        "ic_search",
        "ic_secure",
        "ic_secure_folder",
        "ic_settings",
        "ic_ship",
        "ic_shoes",
        "ic_shopping_bag",
        "ic_skincare",
        "ic_smartwatch",
        "ic_spa",
        "ic_stationery",
        "ic_success",
        "ic_support",
        "ic_tax",
        "ic_taxi",
        "ic_theme_toggle",
        "ic_thinking",
        "ic_timer",
        "ic_tools",
        "ic_traffic_signal",
        "ic_train",
        "ic_transfer",
        "ic_tv",
        "ic_vacation",
        "ic_verified",
        "ic_wallet",
        "ic_water_bill",
        "ic_wedding",
        "ic_wellness",
    )

    /** แปลงชื่อไอคอน (String) เป็น drawable resource id สำหรับ painterResource(...) */
    @DrawableRes
    fun resolve(name: String?): Int = when (name) {
        "ic_account_protect" -> R.drawable.ic_account_protect
        "ic_add_circle" -> R.drawable.ic_add_circle
        "ic_alarm" -> R.drawable.ic_alarm
        "ic_analytics" -> R.drawable.ic_analytics
        "ic_announcement" -> R.drawable.ic_announcement
        "ic_atm" -> R.drawable.ic_atm
        "ic_beauty" -> R.drawable.ic_beauty
        "ic_bicycle" -> R.drawable.ic_bicycle
        "ic_bird" -> R.drawable.ic_bird
        "ic_book" -> R.drawable.ic_book
        "ic_briefcase" -> R.drawable.ic_briefcase
        "ic_bus" -> R.drawable.ic_bus
        "ic_cactus" -> R.drawable.ic_cactus
        "ic_calendar" -> R.drawable.ic_calendar
        "ic_camera" -> R.drawable.ic_camera
        "ic_car" -> R.drawable.ic_car
        "ic_car_insurance" -> R.drawable.ic_car_insurance
        "ic_cart" -> R.drawable.ic_cart
        "ic_cash" -> R.drawable.ic_cash
        "ic_cash_income" -> R.drawable.ic_cash_income
        "ic_cat" -> R.drawable.ic_cat
        "ic_charity" -> R.drawable.ic_charity
        "ic_chart_growth" -> R.drawable.ic_chart_growth
        "ic_chart_pie" -> R.drawable.ic_chart_pie
        "ic_chat" -> R.drawable.ic_chat
        "ic_checklist" -> R.drawable.ic_checklist
        "ic_clothing" -> R.drawable.ic_clothing
        "ic_cloud_sync" -> R.drawable.ic_cloud_sync
        "ic_coffee" -> R.drawable.ic_coffee
        "ic_coins" -> R.drawable.ic_coins
        "ic_credit_card" -> R.drawable.ic_credit_card
        "ic_database" -> R.drawable.ic_database
        "ic_dining" -> R.drawable.ic_dining
        "ic_discount" -> R.drawable.ic_discount
        "ic_dislike" -> R.drawable.ic_dislike
        "ic_dog" -> R.drawable.ic_dog
        "ic_donation" -> R.drawable.ic_donation
        "ic_drink" -> R.drawable.ic_drink
        "ic_eco_home" -> R.drawable.ic_eco_home
        "ic_education" -> R.drawable.ic_education
        "ic_electricity_bill" -> R.drawable.ic_electricity_bill
        "ic_email" -> R.drawable.ic_email
        "ic_error" -> R.drawable.ic_error
        "ic_fastfood" -> R.drawable.ic_fastfood
        "ic_favorite" -> R.drawable.ic_favorite
        "ic_fish" -> R.drawable.ic_fish
        "ic_fitness" -> R.drawable.ic_fitness
        "ic_flight" -> R.drawable.ic_flight
        "ic_flowers" -> R.drawable.ic_flowers
        "ic_folder" -> R.drawable.ic_folder
        "ic_food" -> R.drawable.ic_food
        "ic_fuel" -> R.drawable.ic_fuel
        "ic_gaming" -> R.drawable.ic_gaming
        "ic_gardening" -> R.drawable.ic_gardening
        "ic_gas_bill" -> R.drawable.ic_gas_bill
        "ic_gift" -> R.drawable.ic_gift
        "ic_gift_alt" -> R.drawable.ic_gift_alt
        "ic_global" -> R.drawable.ic_global
        "ic_goal_target" -> R.drawable.ic_goal_target
        "ic_haircare" -> R.drawable.ic_haircare
        "ic_happy" -> R.drawable.ic_happy
        "ic_health" -> R.drawable.ic_health
        "ic_health_insurance" -> R.drawable.ic_health_insurance
        "ic_home" -> R.drawable.ic_home
        "ic_hotel" -> R.drawable.ic_hotel
        "ic_idea" -> R.drawable.ic_idea
        "ic_income" -> R.drawable.ic_income
        "ic_insurance" -> R.drawable.ic_insurance
        "ic_internet" -> R.drawable.ic_internet
        "ic_investment" -> R.drawable.ic_investment
        "ic_key" -> R.drawable.ic_key
        "ic_key_alt" -> R.drawable.ic_key_alt
        "ic_lab" -> R.drawable.ic_lab
        "ic_laptop" -> R.drawable.ic_laptop
        "ic_laundry" -> R.drawable.ic_laundry
        "ic_like" -> R.drawable.ic_like
        "ic_lock" -> R.drawable.ic_lock
        "ic_luggage" -> R.drawable.ic_luggage
        "ic_medical_record" -> R.drawable.ic_medical_record
        "ic_medicine" -> R.drawable.ic_medicine
        "ic_memorial" -> R.drawable.ic_memorial
        "ic_mobile_payment" -> R.drawable.ic_mobile_payment
        "ic_mortgage" -> R.drawable.ic_mortgage
        "ic_motorbike" -> R.drawable.ic_motorbike
        "ic_movie" -> R.drawable.ic_movie
        "ic_movie_snack" -> R.drawable.ic_movie_snack
        "ic_music_headphones" -> R.drawable.ic_music_headphones
        "ic_music_note" -> R.drawable.ic_music_note
        "ic_office_building" -> R.drawable.ic_office_building
        "ic_online_course" -> R.drawable.ic_online_course
        "ic_package" -> R.drawable.ic_package
        "ic_painting" -> R.drawable.ic_painting
        "ic_parking" -> R.drawable.ic_parking
        "ic_pending" -> R.drawable.ic_pending
        "ic_percent_off" -> R.drawable.ic_percent_off
        "ic_pet" -> R.drawable.ic_pet
        "ic_phone" -> R.drawable.ic_phone
        "ic_photo" -> R.drawable.ic_photo
        "ic_pizza" -> R.drawable.ic_pizza
        "ic_plant" -> R.drawable.ic_plant
        "ic_printer" -> R.drawable.ic_printer
        "ic_reading" -> R.drawable.ic_reading
        "ic_receipt" -> R.drawable.ic_receipt
        "ic_refund" -> R.drawable.ic_refund
        "ic_repair" -> R.drawable.ic_repair
        "ic_road_toll" -> R.drawable.ic_road_toll
        "ic_sad" -> R.drawable.ic_sad
        "ic_safe" -> R.drawable.ic_safe
        "ic_savings" -> R.drawable.ic_savings
        "ic_savings_goal" -> R.drawable.ic_savings_goal
        "ic_screwdriver" -> R.drawable.ic_screwdriver
        "ic_search" -> R.drawable.ic_search
        "ic_secure" -> R.drawable.ic_secure
        "ic_secure_folder" -> R.drawable.ic_secure_folder
        "ic_settings" -> R.drawable.ic_settings
        "ic_ship" -> R.drawable.ic_ship
        "ic_shoes" -> R.drawable.ic_shoes
        "ic_shopping_bag" -> R.drawable.ic_shopping_bag
        "ic_skincare" -> R.drawable.ic_skincare
        "ic_smartwatch" -> R.drawable.ic_smartwatch
        "ic_spa" -> R.drawable.ic_spa
        "ic_stationery" -> R.drawable.ic_stationery
        "ic_success" -> R.drawable.ic_success
        "ic_support" -> R.drawable.ic_support
        "ic_tax" -> R.drawable.ic_tax
        "ic_taxi" -> R.drawable.ic_taxi
        "ic_theme_toggle" -> R.drawable.ic_theme_toggle
        "ic_thinking" -> R.drawable.ic_thinking
        "ic_timer" -> R.drawable.ic_timer
        "ic_tools" -> R.drawable.ic_tools
        "ic_traffic_signal" -> R.drawable.ic_traffic_signal
        "ic_train" -> R.drawable.ic_train
        "ic_transfer" -> R.drawable.ic_transfer
        "ic_tv" -> R.drawable.ic_tv
        "ic_vacation" -> R.drawable.ic_vacation
        "ic_verified" -> R.drawable.ic_verified
        "ic_wallet" -> R.drawable.ic_wallet
        "ic_water_bill" -> R.drawable.ic_water_bill
        "ic_wedding" -> R.drawable.ic_wedding
        "ic_wellness" -> R.drawable.ic_wellness
        else -> R.drawable.ic_package
    }
}
