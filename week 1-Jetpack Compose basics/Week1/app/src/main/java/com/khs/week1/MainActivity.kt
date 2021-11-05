package com.khs.week1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khs.week1.ui.theme.Week1Theme
import kotlin.math.exp

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
 * Week1 - 8 state hoisting : state hoisting은 여러 메서드들에 의해 읽히고 수정되는 state는 공통 조상에 있어야 함을 의미한다.(여기서는 My App)
 * 이것은 state의 중복, 버그, 컴포저블의 재사용에 도움을 준다. 컴포저블의 부모에 의해 controll 되지않는 state는 hoist되어서는 안된다.
 * Week 1 - 8 메서드의 매개변수에 onContinueClicked를 넘겨줘서 버튼을 누를 때 이벤트를 전달하도록 한다.
 * */
@Composable
fun OnBoardingScreen(onContinueClicked: () -> Unit) {
    Surface {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to the Basic Codelab !")
            Button(modifier = Modifier.padding(vertical = 24.dp), onClick = onContinueClicked) {
                Text(text = "Continue")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    Week1Theme {
        OnBoardingScreen(onContinueClicked = { })
    }
}

/**
 * Week1 - 5 컴포저블 재사용 : 코드중복을 방지시킬 수 있다.
 * Week1 - 6 열과 행 만들기 : 리스트를 통해서 뷰에 열 쌓기
 * Week1 - 6 열과 행 만들기 : 만들 뷰의 바깥쪽에 패딩 주는 법. 이곳에서는 뷰의 안쪽에는 패딩이 주어지지 않는다.
 * */
@Composable
private fun MyApp(names: List<String> = listOf("World", "Compose")) {
    var shouldShowOnBoarding by remember { mutableStateOf(true)}
    //by를 사용하면 value 없이 값을 할당하거나 읽을 수 있게해준다.
    if(shouldShowOnBoarding) {
        OnBoardingScreen(onContinueClicked = {shouldShowOnBoarding = false})
    } else {
        Greetings()
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
 * Week1 - 7 컴포즈에서의 상태 : state / mutableStateOf - 특정 값을 가지고 있다가 값이 변하면 UI를 변화시켜줌. /
 * Week1 - 7 컴포즈에서의 상태 : remember - state를 기록해 둠으로써 state를 유지시켜준다. 각기 다른 UI마다 다른 값을 기록해둔다.
 * */
@Composable
private fun Greeting(name: String) {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding = if(expanded.value) 48.dp else 0.dp

    Surface(color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier
                .weight(1F)
                .padding(bottom = extraPadding)) {
                Text(text = "Hello,")
                Text(text = name)
            }
            OutlinedButton(
                onClick = {
                    expanded.value = expanded.value.not()
                }
            ) {
                Text( if(expanded.value) "Show less" else "Show More")
            }
            
        }
        //Text(text = "Hello $name!", modifier = Modifier.padding(24.dp))
    }
}

@Composable
private fun Greetings(names: List<String> = listOf("World", "Compose")) {
    Column(modifier = Modifier.padding(4.dp)) {
        for(name in names) {
            Greeting(name)
        }
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
