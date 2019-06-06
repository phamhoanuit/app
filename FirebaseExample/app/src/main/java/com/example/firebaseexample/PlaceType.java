package com.example.firebaseexample;
/**
 * Created by silent on 11/22/2017.
 */

public enum PlaceType {

    ACCOUNTING(R.drawable.ic_accounting_place_type),
    AIRPORT(R.drawable.ic_airport_place_type),
    AQUARIUM(R.drawable.ic_aquarium_place_type),
    ATM(R.drawable.ic_atm_place_type),
    BAKERY(R.drawable.ic_bakery_place_type),
    BICYCLE_STORE(R.drawable.ic_bicycle_store_place_type),
    BOWLING_ALLEY(R.drawable.ic_bowling_alley_place_type),
    BANK(R.drawable.ic_bank_place_type),
    BAR(R.drawable.ic_bar_place_type),
    BOOKSTORE(R.drawable.ic_bookstore_place_type),
    BUS_STATION(R.drawable.ic_bus_station_place_type),
    CEMETERY(R.drawable.ic_cemetery_place_type),
    CHURCH(R.drawable.ic_church_place_type),
    CAFE(R.drawable.ic_cafe_place_type),
    CAR_REPAIR(R.drawable.ic_car_repair_place_type),
    CAR_WASH(R.drawable.ic_car_wash_place_type),
    CITY_HALL(R.drawable.ic_city_hall_place_type),
    CLOTHING_STORE(R.drawable.ic_clothing_store_place_type),
    FIRE_STATION(R.drawable.ic_fire_station_place_type),
    FLORIST(R.drawable.ic_floorist_place_type),
    JEWELRY_STORE(R.drawable.ic_jewelry_store_place_type),
    LAWYER(R.drawable.ic_lawyer_place_type),
    LIBRARY(R.drawable.ic_library_place_type),
    LIQUOR_STORE(R.drawable.ic_liquor_store_place_type),
    LOCKSMITH(R.drawable.ic_locksmith_place_type),
    MEAL_DELIVER(R.drawable.ic_meal_delivery_place_type),
    COURT_HOUSE(R.drawable.ic_court_house_place_type),
    DEFAULT(R.drawable.ic_default_place_type),
    DOCTOR(R.drawable.ic_doctor_place_type),
    GAS_STATION(R.drawable.ic_gas_station_place_type),
    GYM(R.drawable.ic_gym_place_type),
    HOSPITAL(R.drawable.ic_hospital_place_type),
    MOSQUE(R.drawable.ic_mosque_place_type),
    MOVIE(R.drawable.ic_movie_place_type),
    MUSEUM(R.drawable.ic_museum_place_type),
    PARK(R.drawable.ic_park_place_type),
    PARKING(R.drawable.ic_parking_place_type),
    POLICE(R.drawable.ic_police_place_type),
    POST_OFFICE(R.drawable.ic_post_office_place_type),
    RESTAURANT(R.drawable.ic_restaurant_place_type),
    SCHOOL(R.drawable.ic_school_place_type),
    SHOPPING(R.drawable.ic_shopping_place_type),
    SPA(R.drawable.ic_spa_place_type),
    STADIUM(R.drawable.ic_stadium_place_type),
    STORE(R.drawable.ic_store_place_type),
    PET_STORE(R.drawable.ic_pet_store_place_type),
    SUBWAY(R.drawable.ic_subway_place_type),
    SUBWAY_STATION(R.drawable.ic_subway_station_place_type),
    TRAIN_STATION(R.drawable.ic_train_station_place_type),
    VETERINARY_CARE(R.drawable.ic_veterinary_care_place_type),
    SYNAGOGUE(R.drawable.ic_synagogue_place_type),
    AMUSEMENT_PARK(R.drawable.ic_amusement_park_place_type),
    ART_GALLERY(R.drawable.ic_art_gallery_place_type),
    EMBASSY(R.drawable.ic_embassy_place_type),
    LAUNDRY(R.drawable.ic_laundry_place_type),
    SALON(R.drawable.ic_salon_place_type),
    ZOO(R.drawable.ic_zoo_place_type),
    ESTABLISHMENT(R.drawable.ic_establishment_place_type);

    public final int value;


    PlaceType(int value) {
        this.value = value;
    }

    public static int getIdImageDrawable(String type1, String type2) {
        switch (type1) {
            case "accounting":
                return ACCOUNTING.value;
            case "airport":
                return AIRPORT.value;
            case "aquarium":
                return AQUARIUM.value;
            case "atm":
                return ATM.value;
            case "amusement_park":
                return AMUSEMENT_PARK.value;
            case "painter":
            case "art_gallery":
                return ART_GALLERY.value;
            case "beauty_salon":
                return SALON.value;
            case "bank":
                return BANK.value;
            case "night_club":
            case "bar":
                return BAR.value;
            case "bakery":
                return BAKERY.value;
            case "book_store":
                return BOOKSTORE.value;
            case "bus_station":
                return BUS_STATION.value;
            case "cemetery":
                return CEMETERY.value;
            case "church":
                return CHURCH.value;
            case "courthouse":
                return COURT_HOUSE.value;
            case "dentist":
            case "doctor":
                return DOCTOR.value;
            case "gas_station":
                return GAS_STATION.value;
            case "gym":
                return GYM.value;
            case "hospital":
                return HOSPITAL.value;
            case "mosque":
                return MOSQUE.value;
            case "movie_company":
            case "movie_rental":
            case "movie_theater":
                return MOVIE.value;
            case "museum":
                return MUSEUM.value;
            case "park":
                return PARK.value;
            case "parking":
                return PARKING.value;
            case "police":
                return POLICE.value;
            case "meal_takeaway":
            case "restaurant":
                return RESTAURANT.value;
            case "university":
            case "school":
                return SCHOOL.value;
            case "spa":
                return SPA.value;
            case "synagogue":
                return SYNAGOGUE.value;
            case "shopping_mall":
            case "shoe_store":
            case "home_goods_store":
            case "hardware_store":
            case "furniture_store":
            case "electronics_store":
            case "department_store":
            case "convenience_store":
            case "store":
                return SHOPPING.value;
            case "hair_care":
                return SALON.value;
            case "laundry":
                return LAUNDRY.value;
            case "zoo":
                return ZOO.value;
            case "embassy":
                return EMBASSY.value;
            case "cafe":
                return CAFE.value;
            case "car_repair":
                return CAR_REPAIR.value;
            case "car_wash":
                return CAR_WASH.value;
            case "city_hall":
                return CITY_HALL.value;
            case "clothing_store":
                return CLOTHING_STORE.value;
            case "fire_station":
                return FIRE_STATION.value;
            case "florist":
                return FLORIST.value;
            case "jewelry_store":
                return JEWELRY_STORE.value;
            case "liquor_store":
                return LIQUOR_STORE.value;
            case "lawyer":
                return LAWYER.value;
            case "library":
                return LIBRARY.value;
            case "locksmith":
                return LOCKSMITH.value;
            case "meal_delivery":
                return MEAL_DELIVER.value;
            case "pet_store":
                return PET_STORE.value;
            case "subway_station":
                return SUBWAY_STATION.value;
            case "train_station":
                return TRAIN_STATION.value;
            case "transit_station":
                return TRAIN_STATION.value;
            case "veterinary_care":
                return VETERINARY_CARE.value;
            case "stadium":
                return STADIUM.value;
            case "bicycle_store":
                return BICYCLE_STORE.value;
            case "bowling_alley":
                return BOWLING_ALLEY.value;
            case "post_office":
                return POST_OFFICE.value;
            default:
                if(type2.equals("establishment"))
                    return ESTABLISHMENT.value;
                else {
                    return DEFAULT.value;
                }
        }
    }
}
