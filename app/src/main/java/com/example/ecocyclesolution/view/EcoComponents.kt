package com.example.ecocyclesolution.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

@Composable
fun EcoStatsCard(recycled:Int){

    Card(
        colors = CardDefaults.cardColors(Color(0xFF1B5E20)),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ){

        Column(Modifier.padding(20.dp)){

            Text("Total Recycled", color = Color.White)

            Text(
                "${recycled * 2.3} kg",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EcoActionCard(title:String,onClick:()->Unit){

    Card(
        onClick = onClick,
        modifier = Modifier
            .height(100.dp)
    ){
        Box(contentAlignment = Alignment.Center){
            Text(title)
        }
    }
}