/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelabs.state.util.generateRandomTodoItem
import kotlin.random.Random

/**
 * Stateless component that is responsible for the entire todo screen.
 *
 * @param items (state) list of [TodoItem] to display
 * @param onAddItem (event) request an item be added
 * @param onRemoveItem (event) request an item be removed
 */
@Composable
fun TodoScreen(
    items: List<TodoItem>,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit
) {
    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) {
                TodoRow(
                    todo = it,
                    onItemClicked = { onRemoveItem(it) },
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
        }

        // For quick testing, a random item generator button
        Button(
            onClick = { onAddItem(generateRandomTodoItem()) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text("Add random item")
        }
    }
}

/**
 * Stateless composable that displays a full-width [TodoItem].
 *
 * @param todo item to show
 * @param onItemClicked (event) notify caller that the row was clicked
 * @param modifier modifier for this element
 *
 * Recomposition : 새로운 Input을 가지고 Composable을 호출한 후 컴포즈 트리를 업데이트 하는 것이다.
 * Composable을 Recomposing할 때, side-effect가 발생할 수 있다.(모든 View를 다시 Recomposing)
 * 따라서 우리는 값을 Compose Tree에 기억해둘 필요가 있다. remember을 사용하면 Compose Tree에 값을 기록할 수 있다.
 * Remember로 기록된 값들은 값들이 호출된 Composable이 사라지면 같이 사라진다.
 * Composable들은 idempotent해서 Recomposition시에 Side-Effect가 없어야 한다.
 * 만일 Remember값이 다른 곳에서 쓰이고 값이 바뀔 수 있다면, 위치는 Parameter.
 * 아니라면, 위치는 메서드 내부.
 * 하지만 LazyColumn에서 리스트가 많아지고 화면에서 벗어난 목록들은 Remember값이 사라진다.
 * 이 문제를 해결하려면 State와 State Hoisting이 필요하다.
 */
@Composable
fun TodoRow(todo: TodoItem, onItemClicked: (TodoItem) -> Unit,
            modifier: Modifier = Modifier,
            iconAlpha: Float = remember(todo.id) { randomTint()}) {
            //iconAlpha를 매개변수로 넘김으로써, 값을 특정지을 수 있게된다.
    Row(
        modifier = modifier
            .clickable { onItemClicked(todo) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(todo.task)
        // randomTint()는 0.3 ~ 0.9 사이의 값을 Return한다.
        //val iconAlpha = randomTint() 이건 값을 기록하지 않는다.

        //val iconAlpha: Float = remember(todo.id) { randomTint()} 이 값은 저장은 되지만 특정 값을 넣을 순 없다.
        //remember를 사용해서 값을 기록한다. todoList에서 각 행의 id값이 key가 된다. remember(키 값) { 계산값 }
        Icon(
            imageVector = todo.icon.imageVector,
            tint = LocalContentColor.current.copy(alpha = iconAlpha),
            //LocalContentColor는 아이콘, 그림과 같은 것들에 색을 줄 수 있다. Surface같은 컴포저블들에 의해 바뀐다.
            contentDescription = stringResource(id = todo.icon.contentDescription)
        )
    }
}

private fun randomTint(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    TodoScreen(items, {}, {})
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todo = remember { generateRandomTodoItem() }
    TodoRow(todo = todo, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}
