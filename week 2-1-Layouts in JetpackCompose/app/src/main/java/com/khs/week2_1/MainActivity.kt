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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import com.khs.week2_1.ui.theme.Week2_1Theme
import kotlinx.coroutines.launch

/**
 * 4. 슬롯 API : content: @Composable () -> Unit를 통해서 컴포저블을 정의할 수 있다.
 * 쉽게 말해 빈 공간들에 개발자가 원하는 Content를 넣을 수 있는 곳을 의미한다.
 *
 * 9. 뷰는 오른쪽에서 왼쪽 방향으로 갱신된다.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Week2_1Theme {
                Surface(color = MaterialTheme.colors.background) {
                    LayoutsCodelab()
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
 * 만일 재사용할 수 있는 Composable에 Padding을 주고 싶다면?
 * 1. 선언한 곳의 Modifier에서 수정
 * 2. 호출하는 곳의 Modifier에서 수정
 * 을 할 수 있다. 만일 한정적으로 사용되는 곳이라면 1을, 여러 곳에서 사용하는 곳이라면 2의 방법을 쓰면 된다.
 *
 * 만일 Chaining Method가 보이지 않는다면 .then()을 사용해보면 된다.
 *
 */
val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

/**
 * LayoutDirection을 사용하면 컴포저블의 레이아웃 방향을 변경할수 있다.
 * 원래는 오른쪽에서 왼쪽이지만, 레이아웃 방향을 바꾸면 해당 방향으로 랜더링되지 않는다.
 * placeRelative : 오른쪽에서 왼쪽으로 compasable을 place한다.
 * place 혹은 layoutDirection 설정 : 오른쪽에서 왼쪽이 아님.
 */
@Composable
fun BodyContent(modifier: Modifier = Modifier) {4
    //Row의 사이즈는 처음 200 x 200에서 수직 32dp, 수평 32dp가 추가되어서 232 x 232가 된다.
    Row(
        modifier = modifier
            .background(color = Color.LightGray)
            .size(200.dp)
            //.padding(16.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        //padding, scroll 수정자의 속성이 size 속성에 강제된다.
        // 따라서 StaggerdGrid도 200dp로 제한된다.
        StaggeredGrid {
            for(topic in topics) {
                //여기있는 8dp는 전체(Row) 사이즈에는 추가되지 않는다.
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }

//    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
//        StaggeredGrid(modifier = modifier, rows = 3) {
//            for(topic in topics) {
//                Chip(modifier = Modifier.padding(8.dp), text = topic)
//            }
//        }
//    }

//    MyOwnColumn(modifier.padding(8.dp)) {
//        Text("MyOwnColumn")
//        Text("places items")
//        Text("vertically.")
//        Text("We've done it by hand!")
//    }

//    Column(modifier = Modifier) {
//        Text(text = "Hi there!")
//        Text(text = "Thanks for going through the Layouts codelab")
//    }

}
/**
 * 11. Intrinsics(규제)
 * 컴포즈에서는 자식의 크기를 꼭 한번만 measure해야한다. 두번 할 경우 런타임 예외가 발생한다.
 * 만일 자식의 크기가 measure되기전에 자식에 대한 정보를 얻고 싶다면 Intrinsics이 도와줄 수 있다.
 * Composable에게 intrinsicWidth, intrinsicHeight를 요청할 수 있다.
 * 1. (min|max)IntrinsicWidth : content를 적합하게 그리기 위해, 주어진 너비에서 min / max 너비를 찾는다.
 * 2. (min|max)IntrinsicHeight : content를 적합하게 그리기 위해, 주어진 높이에서 min / max 너비를 찾는다.
 * 예를들어, Text에 대한 minIntrinsicHeight에서 무한한 수와 함께 호출했다면, Text의 높이를 return할 것이다.
 *
 * 수정자에서 Density.(min|max)Intrinsic(Width|Height)Of를 Override할 수 있다.
 */
@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    //Row에 IntrinsicSize.Min의 높이를 적용하면, 자식들을 나타낼 최소 높이로 레이아웃의 높이가 설정이 된다.
    //여기서는 Text의 높이가 최소 높이가 된다.
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )
        Divider(color = Color.Black, modifier = Modifier
            .fillMaxHeight()
            .width(1.dp))
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),
            text = text2
        )
    }
}

@Preview
@Composable
fun TwoTextPreview() {
    Week2_1Theme {
        Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}

/**
 * 10. 제약 레이아웃 : 복잡한 뷰 구성을 가진 화면을 구성할 때 좋았다.
 * 하지만 컴포즈에서는 제약 레이아웃은 권장되지 않는다.
 * 1. 다른 뷰 참조는 createRefs()(createRef()) 메서드를 사용해서 만들어진다. 제약 레이아웃에 있는 각 Composable들은 연관된 참조를 가지고 있어야 한다.
 * 2. 제약(Constraints)은 매개변수로써 참조를 만들고 제약을 lambda형태로 만들 수 있게 해주는 constrainAS를 사용하는 수정자를 통해서 제공된다.
 * 3. 제약은 linkTo 혹은 다른 메서드들에 의해 특정된다.
 * 4. parent는 제약을 특정하기위해 사용되어지는 존재하는 참조이다.(ConstraintsLayout composable 그 자체)
 *
 * 사이즈는 최초에는 wrap contentdl이다. 이를 수정하려면 fillMaxSize 또는 size 속성을 이용해서 수정해야 한다.
 */
@Composable
fun DecoupledConstraintLayout() {
    //제약 레이아웃에서 컴포저블과 분리된 상태로 유지하려면 어떻게 해야할 까?
    //일반적인 예시로는 화면 구성을 기반으로 제약 조건을  쉽게 변경하거나, 2개의 제약 조건 세트 간의 애니메이션을 적용하는 것이다.
    //할 수 있는 방법은 1. 제약 레이아웃에 매개변수로 constraints를 넘겨주기. / 2. layoutId를 사용해서 ConstraintSet에서 만들어진 참조들을 할당하기.
    BoxWithConstraints {
        val constraints = if(maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp)
        } else {
            decoupledConstraints(margin = 32.dp)
        }

        ConstraintLayout(constraints) {
            Button(onClick = { /*Do Something*/ },
                Modifier.layoutId("button")
            ) {
                Text(text = "Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}

@Composable
fun ConstraintLayoutContent() {
//    ConstraintLayout {
//        //제약 레이아웃에서는 텍스트가 길면 화면에서 짤릴수가 있다.
//        val text = createRef()
//
//        val guideLine = createGuidelineFromStart(fraction = 0.5F)
//        Text("This is a very very very very very very very long text",
//            Modifier.constrainAs(text) {
//                linkTo(start = guideLine, end = parent.end)
//                //화면에 짤리지 않게 하기 위해 아래 속성을 추가한다.
//                width = Dimension.preferredWrapContent
//
//                //Dimension 관련 속성에는 다음과 같은 것들이 있다.
//                //1. preferredWrapContent : 레이아웃은 부모의 제약 조건 내에서 wrap content이다.
//                //2. wrap content : 제약 조건 범위 밖이라도 콘텐츠를 줄바꿈 한다.
//                //3. fillToConstraints : 레이아웃이 제약에 정의된 공간을 채우기위해 dimension으로 확장된다.
//                //4. preferredValue  : 레이아웃이 제약 조건을 갖추기 위해 고정된 dp값을 갖는다.
//                //5. value : 제약 조건에 상관 없는 고정된 dp값을 갖는다.
//                //또한 특정 Dimension은 Dimension.preferredWrapContent.atLeast(100.dp)과 같은 속성을 가질 수도 있다.
//            }
//        )
//    }

//    ConstraintLayout {
//        val (button1, button2, text) = createRefs()
//
//        Button(
//            onClick = {/* DO Something */},
//            modifier = Modifier.constrainAs(button1) {
//                top.linkTo(parent.top, margin = 16.dp)
//            }
//        ) {
//            Text("Button1")
//        }
//        Text(
//            "Text", modifier = Modifier.constrainAs(text) {
//                top.linkTo(button1.bottom, margin = 16.dp)
//
//                centerAround(button1.end)
//            }
//        )
//        //DSL은 가이드라인, 배리어, chain을 제공한다.
//        val barrier = createEndBarrier(button1, text) // button1의 끝, text의 끝에 barrier를 만들어 놓는다.
//        //배리어는 ConstraintsLayout의 body에 생긴다. constrainAs에서 생기는 것이 아니다.
//
//        Button(
//            onClick = {/* DO Something */},
//            modifier = Modifier.constrainAs(button2) {
//                top.linkTo(parent.top, margin = 16.dp)
//                start.linkTo(barrier)
//                // linkTo를 배리어에 연결지을 수 있다.
//            }
//        ) {
//            Text("Button2")
//        }
//    }

//    ConstraintLayout {
//        val (button, text) = createRefs()
//
//        Button(
//            onClick = {/* DO Something */},
//
//            //'button'에 대한 참조를 수정자의 constrainAS 속성을 통해서 설정한다. 부모(ConstraintsLayout)의 위로 linkTo를 한다.
//            modifier = Modifier.constrainAs(button) {
//                top.linkTo(parent.top, margin = 16.dp)
//            }
//        ) {
//            Text("Button")
//        }
//
//        Text(
//            "Text", modifier = Modifier.constrainAs(text) {
//                top.linkTo(button.bottom, margin = 16.dp)
//
//                centerHorizontallyTo(parent) // 부모 기준 가운데로 정렬시킨다.
//            }
//        )
//    }
}

@Preview
@Composable
fun ConstraintLayoutContentPreview() {
    Week2_1Theme {
        ConstraintLayoutContent()
    }
}

/**
 * 9. hood 아래에 있는 레이아웃 수정자(modifier)
 */

//어떻게 수정자를 구현하는지에 대한 내용
@Stable
fun Modifier.padding(all: Dp) = this.then(
    PaddingModifier(start = all, top = all, end = all, bottom = all, rtlAware = true)
)

//세부 구현사항
private class PaddingModifier(
    val start: Dp = 0.dp,
    val top: Dp = 0.dp,
    val end: Dp = 0.dp,
    val bottom: Dp = 0.dp,
    val rtlAware: Boolean
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val horizontal = start.roundToPx() + end.roundToPx()
        val vertical = top.roundToPx() + bottom.roundToPx()

        //offset 메서드는 더해주는 값이다. -된 좌표 값이 placeable한지 확인한다.
        val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))

        val width = constraints.constrainWidth(placeable.width + horizontal)
        val height = constraints.constrainHeight(placeable.height + vertical)
        // 너비는 원래 너비 + 수평 패딩 값, 높이는 원래 높이 + 수직 패딩 값이 된다.

        return layout(width = width, height = height) {
            if(rtlAware) {
                placeable.placeRelative(start.roundToPx(), top.roundToPx())
            } else {
                placeable.place(start.roundToPx(), top.roundToPx())
            }
        }
    }
}

/**
 * 8. 복잡한 사용자 지정 레이아웃
 */
@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row (modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box (
                Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text)
        }
    }
}
@Preview
@Composable
fun ChipPreview() {
    Week2_1Theme {
        Chip(text = "Hi there")
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(modifier = modifier, content = content) {
            measurables, constraints ->
        //자녀는 한 번만 측정 가능.

        val rowWidths = IntArray(rows) { 0 } // 각 행의 너비를 기록

        val rowHeights = IntArray(rows) { 0 } // 각 행의 높이를 기록

        //측정된 자식들의 리스트
        val placeables = measurables.mapIndexed {
                index, measurable ->

            // 자식 측정
            val placeable = measurable.measure(constraints = constraints)

            // 각 행의 최대 너비와 높이를 저장한다.
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }

        // 그리드의 너비는 가장 긴 너비이다.
        val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // 그리드의 높이는 각 행마다 가장 큰 원소들의 합이다. (각 행마다 가장 큰 원소가 그 행의 높이이므로)
        val height = rowHeights.sumOf { it }.coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        //각 뷰가 그려질 높이는 높이를 계속 중첩시켜서 더해주는 것과 같다.
        val rowY = IntArray(rows) { 0 }
        for(i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }

        layout(width, height) {
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed {index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

/**
 * 7. 커스텀 레이아웃 만들어보기
 * layout Modifier을 사용한다.
 * Compose에서는 자식의 크기를 한 번만 잰다.
 * measurable : 측정되어야하고 놓여져야 할 Child
 * constraints : child의 최소, 최대 너비와 높이
 *
 * 두 가지 Preview의 차이 : 뷰의 위에서 시작한 Padding과, Baseline에서 시작한 Padding
 */
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    //커스텀 레이아웃 속성
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) {
            measurables, constraints ->

        // 자식의 뷰들을 더 제한하지말고, 주어진 constraints에서 측정해야 한다.
        // 자식의 List
        val placeables = measurables.map {
            // 각 child를 측정한다.
                measurable ->
            measurable.measure(constraints)
        }

        var yPosition = 0

        //레이아웃을 가능한 크게 만든다.
        layout(constraints.maxWidth, constraints.maxHeight) {
            //child들을 놓는다.
            placeables.forEach{placeable ->
                placeable.placeRelative(x = 0, y = yPosition)

                yPosition += placeable.height
            }
        }
    }
}

@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    Week2_1Theme {
        Text(text = "Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}
@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    Week2_1Theme {
        Text(text = "Hi there!", Modifier.padding(top = 32.dp))
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout {measurable, constraints ->
        //처음 해야할 것은 composable의 크기를 측정하는 것이다.
        val placeable = measurable.measure(constraints = constraints) // 이 메서드로 composable이 (x, y)에 놓여질 수 있는지 여부를 check한다.

        //Composable이 FirstBaseLine이 있는지 여부를 확인한다.
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseLine = placeable[FirstBaseline]

        val placeableY = firstBaselineToTop.roundToPx() - firstBaseLine // FirstBaseLine을 제외시켜놓는다.
        val height = placeable.height + placeableY //패딩이 포함된 Composable의 높이.
        layout(placeable.width, height) {
            //Composable이 놓여질 곳
            placeable.placeRelative(0, placeableY)
        }
    }
)

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