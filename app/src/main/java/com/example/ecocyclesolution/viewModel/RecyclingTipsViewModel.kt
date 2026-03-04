package com.example.ecocyclesolution.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.ecocyclesolution.model.RecyclingTip

class RecyclingTipsViewModel : ViewModel() {

    val tips = mutableStateListOf(

        RecyclingTip(
            "Separate Waste",
            "Separate biodegradable and non-biodegradable waste."
        ),

        RecyclingTip(
            "Clean Before Recycling",
            "Wash plastic and glass before recycling."
        ),

        RecyclingTip(
            "Reuse Plastic Bags",
            "Reuse bags to reduce pollution."
        ),

        RecyclingTip(
            "Compost Organic Waste",
            "Convert food waste into fertilizer."
        ),

        RecyclingTip(
            "Recycle Electronics",
            "Dispose e-waste properly."
        )
    )
}