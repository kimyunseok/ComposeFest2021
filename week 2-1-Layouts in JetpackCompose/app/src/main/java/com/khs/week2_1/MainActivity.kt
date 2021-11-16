package com.khs.week2_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khs.week2_1.ui.theme.Week2_1Theme

/**
 * 4. 슬롯 API : content: @Composable () -> Unit를 통해서 컴포저블을 정의할 수 있다.
 * 쉽게 말해 빈 공간들에 개발자가 원하는 Content를 넣을 수 있는 곳을 의미한다.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Week2_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PhotographerCard()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Week2_1Theme {
        Greeting("Android")
    }
}

/**
 * Week 2-1 - 3. 수정자 : 수정자에서는 순서에 따라서 뷰가 달라진다.
 * */
@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Row(
        modifier
//            .padding(16.dp)
//            .clickable(onClick = {
//                //클릭시
//            })
            //위와같은 순서로 할 경우 패딩 부분에는 온클릭이 적용되지 않는다 !
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = {
                //클릭시
            })
            .padding(16.dp)
    ) {
        Surface(modifier = Modifier.size(50.dp), shape = CircleShape, color = MaterialTheme.colors.onSurface.copy(alpha = 0.2F)) {
            //Image 들어갈 곳
        }

        Column (
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
                ){
            Text("Alfred Sisely", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                //Week 2-1 - 3. 수정자 : CompositionLocalProvider를 통해서 글자의 투명도(Alpha) 설정.
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview
@Composable
fun PhotographerCardPreview() {
    Week2_1Theme {
        PhotographerCard()
    }
}