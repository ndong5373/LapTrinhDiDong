package com.example.myapplication.ui.State

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.log

@SuppressLint("UnrememberedMutableState")
@Composable
fun Loginscreens(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Log.e("Frank", "Loginscreen strar" )
        Wellcome()
        var email by rememberSaveable {
            mutableStateOf("")
        }

       OutlinedTextField(email, onValueChange = {
           email=it
       })
        Log.e("Frank", "Loginscreen end " )
    }

}
@Composable
fun Wellcome(){
    Log.e("Frank", "Wellcome star ")
    Text(text="Login to your account")
    Log.e("Frank", "Wellcome end" )
}