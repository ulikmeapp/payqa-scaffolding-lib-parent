package com.ulikme.utils.data

import com.ulikme.utils.data.enums.LocationType

open class PointLocation(
    open var coordinates: Array<Double?> = emptyArray(),
    open var type: String = LocationType.POINT.value
)