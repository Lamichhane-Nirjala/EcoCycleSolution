package com.example.ecocyclesolution.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecocyclesolution.viewModel.RecyclingTipsViewModel

@Composable
fun RecyclingTipsScreen(
    viewModel: RecyclingTipsViewModel = viewModel()
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF071E12))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(viewModel.tips) { tip ->

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = tip.title,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = tip.description,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}