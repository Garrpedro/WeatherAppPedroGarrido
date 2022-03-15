package com.example.weatherapppedrogarrido.weatherlist

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherapppedrogarrido.R
import com.example.weatherapppedrogarrido.data.models.WeatherListEntry
import com.example.weatherapppedrogarrido.util.Utils
import com.google.gson.Gson

@Composable
fun WeatherListScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_app),
                contentDescription = "Weather",
                modifier = Modifier
                    .size(150.dp, 150.dp)
                    .align(CenterHorizontally)
            )
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

            }
            Spacer(modifier = Modifier.height(16.dp))
            WeatherList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text, onValueChange = {
                text = it
                onSearch(it)
            }, maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun WeatherList(
    navController: NavController,
    viewModel: WeatherListViewModel = hiltViewModel()
) {
    val weatherList by remember { viewModel.weatherList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(contentPadding = PaddingValues(16.dp))
    {
        val itemCount = if (weatherList.size % 2 == 0) {
            weatherList.size / 2
        } else {
            weatherList.size / 2 + 1
        }
        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading) {
                viewModel.loadWeatherCities()
            }
            WeatherRow(rowIndex = it, entries = weatherList, navController = navController)
        }
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadWeatherCities()
            }
        }
    }
}

@Composable
fun WeatherEntry(
    entry: WeatherListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: WeatherListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {
                val json = Uri.encode(Gson().toJson(entry))
                navController.navigate(
                    "weather_detail_screen/$json"
                )
            }
    ) {
        Column {
            Image(
                painter = painterResource(Utils.getImageDrawable(entry.description)),
                contentDescription = "Weather",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                entry.cityName?.let {
                    Text(
                        text = it,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(
                    modifier = Modifier
                        .size(12.dp, 1.dp)
                )
                Text(
                    text = entry.tempCurr.toString().substringBefore("."),
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 26.sp
                )
                Image(
                    painter = painterResource(R.drawable.ic_celsius),
                    contentDescription = "Celsius",
                    Modifier.size(20.dp, 20.dp),
                )
            }
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TemperatureCard(
                    R.drawable.ic_high_temperature,
                    entry.tempMax.toString().substringBefore(".")
                )
                TemperatureCard(
                    R.drawable.ic_low_temperature,
                    entry.tempMin.toString().substringBefore(".")
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun TemperatureCard(temperatureDrawable: Int, maxTemp: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(temperatureDrawable),
            contentDescription = "Weather",
            modifier = Modifier
                .size(40.dp, 60.dp)
        )
        Text(
            text = maxTemp
        )
        Image(
            painter = painterResource(id = R.drawable.ic_celsius),
            contentDescription = "celsius",
            Modifier.size(12.dp, 12.dp)
        )
    }
}

@Composable
fun WeatherRow(
    rowIndex: Int,
    entries: List<WeatherListEntry?>,
    navController: NavController
) {
    Column {
        Row {
            entries[rowIndex * 2]?.let {
                WeatherEntry(
                    entry = it,
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                entries[rowIndex * 2 + 1]?.let {
                    WeatherEntry(
                        entry = it,
                        navController = navController,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}