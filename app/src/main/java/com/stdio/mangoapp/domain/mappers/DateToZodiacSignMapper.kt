package com.stdio.mangoapp.domain.mappers

import com.stdio.mangoapp.domain.Mapper
import java.util.Calendar
import java.util.Date


class DateToZodiacSignMapper : Mapper<Date, String> {
    override fun map(input: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = input
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return when (month) {
            1 -> if (day < 20) {
                "Козерог"
            } else {
                "Водолей"
            }

            2 -> if (day < 18) {
                "Водолей"
            } else {
                "Рыбы"
            }

            3 -> if (day < 21) {
                "Рыбы"
            } else {
                "Овен"
            }

            4 -> if (day < 20) {
                "Овен"
            } else {
                "Телец"
            }

            5 -> if (day < 21) {
                "Телец"
            } else {
                "Близнецы"
            }

            6 -> if (day < 21) {
                "Близнецы"
            } else {
                "Рак"
            }

            7 -> if (day < 23) {
                "Рак"
            } else {
                "Лев"
            }

            8 -> if (day < 23) {
                "Лев"
            } else {
                "Дева"
            }

            9 -> if (day < 23) {
                "Дева"
            } else {
                "Весы"
            }

            10 -> if (day < 23) {
                "Весы"
            } else {
                "Скорпион"
            }

            11 -> if (day < 22) {
                "Скорпион"
            } else {
                "Стрелец"
            }

            12 -> if (day < 22) {
                "Стрелец"
            } else {
                "Козерог"
            }
            else -> ""
        }
    }
}