package com.khs.week2_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.khs.week2_1.ui.theme.Week2_1Theme
import kotlinx.coroutines.launch

/**
 * 4. 슬롯 API : content: @Composable () -> Unit를 통해서 컴포저블을 정의할 수 있다.
 * 쉽게 말해 빈 공간들에 개발자가 원하는 Content를 넣을 수 있는 곳을 의미한다.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Week2_1Theme {
                Surface(color = MaterialTheme.colors.background) {
                    LazyList()
                }
            }

//            Week2_1Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    PhotographerCard()
//                }
//            }
        }
    }
}

/**
 * 6. 리스트
 */
@Composable
fun ImageListItem(index: Int) {
    //이미지를 스크롤해보면 알겠지만 화면에 보이는 것만 랜더링 해준다.
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(data = "https://developer.android.com/images/brand/Android_Robot.png"),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}
@Composable
fun LazyList() {
    //LazyColumn은 RecyclerView이다. 또한 화면에 보이는 목록만 렌더링하므로 성능을 향상시킨다.

    val listSize = 100
    val scrollState = rememberLazyListState() // Column은 스크롤 상태를 저장해주지 않는다.
    val coroutineScope = rememberCoroutineScope() // 스크롤 동작을 비동기로 처리하기 위한 코루틴.

    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text(text = "Scroll to the top")
            }

            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text(text = "Scroll to the Bottom")
            }
        }

        LazyColumn(state = scrollState) {
            items(100) {
                ImageListItem(it)
            }
        }
    }

}
@Composable
fun SimpleList() {
    val scrollState = rememberScrollState() // Column은 스크롤 상태를 저장해주지 않는다.

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text(text = "Item #$it")
        }
    }
}

/**
 * 5. Material Components - Scaffold : 다양한 슬롯들을 제공해주고, Material Components중에 가장 높은 레벨이다.
 */
@Composable
fun LayoutsCodelab() {
    Scaffold (
        topBar = { // Scaffold에는 topBar라는 AppBar속성이 존재한다. 슬롯 API(@Composable () -> Unit 유형)이기 때문에 우리가 원하는 컴포저블로 채울 수 있다.
            TopAppBar(
                title = {Text(text = "LayoutsCodelab")},
                actions = { // actions는 Row방향이므로 수평으로 아이콘들이 추가된다.
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
        //코드 재사용성을 높일 수 있다.
//        Column(modifier = Modifier.padding(innerPadding)) {
//            Text(text = "Hi there!")
//            Text(text = "Thanks for going through the Layouts codelab")
//        }
    }
}

/**
 * 만일 재사용할 수 있는 Composable에 Padding을 주고 싶다면?
 * 1. 선언한 곳의 Modifier에서 수정
 * 2. 호출하는 곳의 Modifier에서 수정
 * 을 할 수 있다. 만일 한정적으로 사용되는 곳이라면 1을, 여러 곳에서 사용하는 곳이라면 2의 방법을 쓰면 된다.
 *
 * 만일 Chaining Method가 보이지 않는다면 .then()을 사용해보면 된다.
 */
@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = Modifier) {
        Text(text = "Hi there!")
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

@Preview
@Composable
fun LayoutsCodelabPreview() {
    Week2_1Theme {
        LayoutsCodelab()
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