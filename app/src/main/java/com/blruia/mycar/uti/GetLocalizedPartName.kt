package com.blruia.mycar.uti

import com.blruia.mycar.R


fun getLocalizedPartName(partKey: String): Int = when (partKey) {
        "part_timing_belt" -> R.string.part_timing_belt
        "part_motor_oil" -> R.string.part_motor_oil
        "part_spark_plugs" -> R.string.part_spark_plugs
        "part_pads" -> R.string.part_pads
        "part_brake_fluid" -> R.string.part_brake_fluid
        "part_tires" -> R.string.part_tires
        else -> R.string.unknown_part
}
