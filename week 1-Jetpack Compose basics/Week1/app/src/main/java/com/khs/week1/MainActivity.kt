package com.khs.week1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khs.week1.ui.theme.Week1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Week1Theme {
                MyApp()
            }
        }
    }
}

/**
 * Week1 - 5 컴포저블 재사용 : 코드중복을 방지시킬 수 있다.
 * Week1 - 6 열과 행 만들기 : 리스트를 통해서 뷰에 열 쌓기
 * Week1 - 6 열과 행 만들기 : 만들 뷰의 바깥쪽에 패딩 주는 법. 이곳에서는 뷰의 안쪽에는 패딩이 주어지지 않는다.
 * */
@Composable
private fun MyApp(names: List<String> = listOf("World", "Compose")) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            Greeting(name = name)
        }
    }

//    Surface(color = MaterialTheme.colors.background) {
//        Greeting("Android")
//    }
}

/**
 * Week1 - 4 UI 조정 : surface는 배경색을 의미한다.
 * Week1 - 4 UI 조정 : Modifier에서는 뷰에대한 여러가지 속성을 설정할 수 있다. apply로 할 필요없이 .(dot)로 계속해서 만들어주면 된다.
 * Week1 - 6 열과 행 만들기 : 열 만들기
 * Week1 - 6 열과 행 만들기 : 만들 뷰의 안쪽과 수평방향에 패딩 주는 법
 * Week1 - 6 열과 행 만들기 : 버튼 생성하기. Row가 Horizontal LinearLayout, Column이 Vertical LinearLayout이다.
 * */
@Composable
private fun Greeting(name: String) {
    Surface(color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(1F)) {
                Text(text = "Hello,")
                Text(text = name)
            }
            OutlinedButton(
                onClick = {
                    //TODO
                }
            ) {
                Text(text = "Show Less")
            }
            
        }
        //Text(text = "Hello $name!", modifier = Modifier.padding(24.dp))
    }
}

/**
 * Week1 - 6 열과 행 만들기 : widthDp를 통해서 가로크기를 정할 수 있다.
 * */
@Preview(showBackground = true, name = "Text Preview", widthDp = 320) // @Preview 어노테이션을 사용해서 미리볼 수 있다.
@Composable
fun DefaultPreview() {
    Week1Theme {
        MyApp()
    }
}
