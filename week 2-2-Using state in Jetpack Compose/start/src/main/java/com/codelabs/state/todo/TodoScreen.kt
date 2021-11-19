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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
    currentlyEditing: TodoItem?,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit,
    onStartEdit: (TodoItem) -> Unit,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit
) {
    Column {
        val enableTopSelection = (currentlyEditing == null) // NULL값이라면 상단에 ADD, 아니라면 수정중. 다른 UI 표시
        TodoItemInputBackground(elevate = enableTopSelection) {
            // enableTopSelection이라면 TodoItemEntryInput을, 아니라면 Text를 띄운다.
            if(enableTopSelection) {
                TodoItemEntryInput(onItemComplete = onAddItem)
                //TodoItemEntryInput은 Stateful하지만, ViewModel로 State Hoisting을 하고 있으므로 상관 없다.
            } else {
                Text(text = "Editing Item",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(16.dp)
                        .fillMaxWidth())

            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) { todo ->
                if(currentlyEditing?.id == todo.id) {
                    TodoItemInlineEditor(
                        item = currentlyEditing,
                        onEditItemChange = onEditItemChange,
                        onEditDone = onEditDone,
                        onRemoveItem = { onRemoveItem(todo) }
                    )
                } else {
                    TodoRow(
                        todo = todo,
                        onItemClicked = { onStartEdit(it) },
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
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
 * 2-2 - 11. Reuse stateless Composables :
 * 이전에 Stateful한 TodoItemInput을 Stateless하게 만들었으므로 이 Stateless한 Composable을 사용할 수 있게된다.
 * Stateful : 내부적으로 상태를 직접 가지고 있는 Composable. 대부분 remember를 사용한다.
 * Stateless : 내부적으로 상태를 직접 가지지 않는 Composable
 */
//
@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: () -> Unit
) = TodoItemInput( // TodoItemInput은 Stateless하므로 재사용이 가능하다.
    text = item.task,
    onTextChange = {onEditItemChange(item.copy(task = it))},
    icon = item.icon,
    onIconChange = {onEditItemChange(item.copy(icon = it))},
    submit = onEditDone,
    iconVisible = true
//copy() 메서드는 Data Class에서 사용가능하다. 해당 인스턴스를 복사한 후에 파라미터에 있는 값을 변경해준다.
)

/**
 * 6. State In Compose
 * stateful composable : 시간이 지남에 따라 변하는 State를 소유한 Composable. 값의 중복을 막을 수 있다.
 *
 * MutableState : MutableLiveData와 유사하다. 그러나 런타임에 MutableState는 통합이 된다.
 * 1. val state = remember { mutableStateOf(default) }
 * 2. var value by remember { mutableStateOf(default) }
 * 3. val (value, setValue) = remember { mutableStateOf(default) }
 *
 * TodoItemInput ->(state) TodoInputTextField
 * TodoInputTextField ->(event) TodoItemInput
 * 의 방식이 되어야 한다. TodoItemInput이 Parent, TodoInputTextField이 Child.
 * TodoInputTextField의 State를 부모에 넘겨줘야 한다. 이를 State Hoisting이라고 한다.
 * State Hoisting의 특징
 * 1. 값이 복제될 필요 없이, 해당 State의 값의 원천이 하나만 존재하게 된다.
 * 2. 캡슐화 : 다른 Composable들이 Event를 보내와도 해당 State는 특정 Composable만 Control이 가능하다.
 * 3. Shareable : Hosited State는 Immutable Value로 다른 Composable들과 공유가 가능하다.
 * 4. Interceptable : 부모는 Child의 State가 바뀌기 전에 이벤트들을 무시하거나 수정할 수 있다.
 * 5. Decoupled : Child의 State가 모든 곳에서 저장될 수 있다. 예시로 Room DB를 써서 해당 상태로 돌아오는 것이 가능하다.
 *
 * 2 - 2 - 8. Extracting stateless Composables :
 * https://developer.android.com/codelabs/jetpack-compose-state?authuser=4&continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fcompose%3Fhl%3Den%26authuser%3D4%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-state&hl=en#7
 * 참조. Stateful한 Composable을 Stateless하게 만든다.
 * 이 예시에서는 TodoItemInput ->(Event) TodoScreen였는데, TodoItemInput을 나누어서
 * TodoItemEntryInput ->(Event) TodoItemInput로 만들었다.
 * 수정 기능을 추가하기 위함이다.
 *
 * 2 - 2 - 9. Use State In ViewModel :
 * 1. State가 호이스팅되기 위해서는 최소한 해당 State를 사용하는 최소단위 부모에게까지 호이스팅 되어야 한다.
 * 2. State는 최소한 값이 수정되는 최대 레벨까지 호이스팅 되어져야 한다.
 * 3. 만일 같은 이벤트에서 호이스팅되는 State가 두 가지 이상이라면, 동시에 호이스팅 되어져야 한다.
 */
@Composable
fun TodoItemEntryInput(onItemComplete: (TodoItem) -> Unit) { // Stateful
    val (text, setText) = remember { mutableStateOf("")}
    val (icon, setIcon) = remember { mutableStateOf( TodoIcon.Default) }
    val iconVisible = text.isNotBlank() // iconVisible은 text에 Mapping되어 있기 때문에 상관없다.
    //val iconsVisible: LiveData<Boolean> = textLiveData.map { it.isNotBlank() } 의 형태와 같음.

    //코드 재사용을 위해 아래와같이 Lambda의 형태로 만들어준다.
    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default) // 아이콘 선택을 기본 네모 모양으로 Reset해준다.
        setText("")
    }

    //람다를 넘겨주는 방식은 Compose에서 Event를 특정짓는 일반적인 방법이다.
    TodoItemInput(
        text = text, // State Hoist
        onTextChange = setText,
        icon = icon, // State Hoist
        onIconChange = setIcon,
        submit = submit,
        iconVisible = iconVisible
    )
}

@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) -> Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    submit: () -> Unit,
    iconVisible: Boolean
) { // Stateless,
    Column {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        )
        {
            //아래와 같은 구조에서는 text를 TodoInputTextField, TodoEditButton에서 사용했다.
            // State Hoist의 Shareable한 특징이다.
            TodoInputText(
                text = text,
                onTextChange = onTextChange,
                Modifier
                    .weight(1F)
                    .padding(end = 8.dp),
                onImeAction = submit // Callback
                // 키보드 관련해서 TextField는, (TodoComponents에 선언된 TodoInputText에 아래가 설정돼 있다.)
                // 1. keyboardOptions : Ime 액션이 끝났다는 것을 보여주는 데 사용
                // 2. keyboardActions : 특정 Ime 작업에 대한 응답으로 트리거할 작업을 지정하는데 사용됨. 여기서는 submit 콜백이 사용됐다
            )
            TodoEditButton(
                onClick = submit, // Callback
                text = "Add",
                modifier = Modifier.align(Alignment.CenterVertically),
                enabled = text.isNotBlank()
            )
        }
        if (iconVisible) {
            AnimatedIconRow(
                icon = icon,
                onIconChange = onIconChange,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TodoInputTextField(text: String, onTextChange: (String) -> Unit, modifier: Modifier) {
    //val (text, setText) = remember { mutableStateOf("") State Hoisting을 하려면 뷰 내부에서 호출하면 안된다.
    TodoInputText(text = text, onTextChange = onTextChange, modifier)
    //위 방식은 remember를 사용하지만, 자기 자신의 메모리에 사용하고 있다.
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
    TodoScreen(items, null, {}, {}, {}, {}, {}) // Preview는 이런 식으로 수정하면 됨.(Signature가 바뀌어도 Compile하지 않으므로,)
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todo = remember { generateRandomTodoItem() }
    TodoRow(todo = todo, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}
