package com.oookraton.chinarDebug

object OrderData {
    val orders= listOf(
        OrderItem(DateItem(2025,12,20), 20,
            "Поминальные","Синие хлопковые",
            false,false,false,false,
            listOf(1 to 20, 2 to 10, 4 to 40)),
        OrderItem(DateItem(2025,11,20), 10,
            "День рождения", "Красные льняные",
            true, true, true, false,
            listOf(2 to 5, 4 to 20))

    )
}