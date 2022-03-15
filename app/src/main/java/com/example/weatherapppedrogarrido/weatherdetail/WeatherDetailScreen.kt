package com.example.weatherapppedrogarrido.weatherdetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapppedrogarrido.R
import com.example.weatherapppedrogarrido.data.models.WeatherListEntry
import com.example.weatherapppedrogarrido.util.Utils
import kotlin.math.abs
import kotlin.math.floor


@Composable
fun WeatherDetailScreen(
    cityInfo: WeatherListEntry,
    navController: NavController,
    topPadding: Dp = 20.dp,
    weatherImageSize: Dp = 200.dp,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(bottom = 16.dp)
    ) {
        WeatherDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        WeatherDetailSection(
            weatherInfo = cityInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + weatherImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(Utils.getImageDrawable(cityInfo.description)),
                contentDescription = cityInfo.description,
                modifier = Modifier
                    .size(weatherImageSize)
                    .offset(y = topPadding)
            )
        }
    }
}

@Composable
fun WeatherDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun WeatherDetailSection(
    weatherInfo: WeatherListEntry,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${weatherInfo.cityName} ",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(
                modifier = Modifier
                    .size(12.dp, 1.dp)
            )
            Text(
                text = weatherInfo.tempCurr.toString().substringBefore("."),
                color = MaterialTheme.colors.onSurface,
                fontSize = 30.sp
            )
            Image(
                painter = painterResource(R.drawable.ic_celsius),
                contentDescription = "Celsius",
                Modifier.size(20.dp, 20.dp),
            )
        }
        WeatherDetailDataSection(
            weatherTempMax = weatherInfo.tempMax,
            weatherTempMin = weatherInfo.tempMin,
            weatherWindSpeed = weatherInfo.windSpeed,
            weatherHumidity = weatherInfo.humidity
        )
    }
}

@Composable
fun WeatherDetailDataSection(
    weatherTempMin: Double,
    weatherTempMax: Double,
    weatherWindSpeed: Double,
    weatherHumidity: Int,
    sectionHeight: Dp = 80.dp
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            WeatherDetailDataItem(
                dataValue = weatherHumidity,
                dataUnit = stringResource(id = R.string.humidity),
                dataIcon = painterResource(id = R.drawable.ic_humidity),
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .size(1.dp, sectionHeight)
                    .background(Color.LightGray)
            )
            WeatherDetailDataItem(
                dataValue = floor(weatherWindSpeed).toInt(),
                dataUnit = stringResource(id = R.string.wind_speed),
                dataIcon = painterResource(id = R.drawable.ic_wind),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            WeatherDetailDataItem(
                dataValue = weatherTempMax.toInt(),
                dataUnit = stringResource(id = R.string.max_temp),
                dataIcon = painterResource(id = R.drawable.ic_high_temperature),
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .size(1.dp, sectionHeight)
                    .background(Color.LightGray)
            )
            WeatherDetailDataItem(
                dataValue = weatherTempMin.toInt(),
                dataUnit = stringResource(id = R.string.min_temp),
                dataIcon = painterResource(id = R.drawable.ic_low_temperature),
                modifier = Modifier.weight(1f)
            )
        }
    }

}

@Composable
fun WeatherDetailDataItem(
    dataValue: Int,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Image(painter = dataIcon, contentDescription = null)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataUnit$dataValue",
            color = MaterialTheme.colors.onSurface
        )
    }
}
