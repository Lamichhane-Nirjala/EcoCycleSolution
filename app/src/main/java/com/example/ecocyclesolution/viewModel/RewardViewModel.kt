package com.example.ecocyclesolution.viewModel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.ecocyclesolution.model.RewardModel

class RewardViewModel : ViewModel() {

    var rewards =
        mutableStateListOf(

            RewardModel(
                "1",
                "10% Grocery Coupon",
                50,
                "Use at partner stores"
            ),

            RewardModel(
                "2",
                "Free Tree Plantation",
                100,
                "Plant a tree in your name"
            ),

            RewardModel(
                "3",
                "Eco Water Bottle",
                150,
                "Reusable eco bottle"
            ),

            RewardModel(
                "4",
                "Green T-Shirt",
                200,
                "EcoCycle Merchandise"
            )
        )
}