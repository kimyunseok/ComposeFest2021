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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.codelabs.state.ui.StateCodelabTheme

/**
 * State – 시간에 따라 바뀔 수 있는 모든 값
 * Event – 프로그램에서 발생한 일련의 사건(동작).
 * Unidirectional data flow – 이벤트는 위로가고 상태는 내려오는 데이터 흐름.
 * 대체로
 * 부모 ->(State) 자식
 * 자식 ->(Event) 부모
 */
class TodoActivity : AppCompatActivity() {

    private val todoViewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateCodelabTheme {
                Surface { // Surface는 배경과 글자 색상을 지정해준다.
                    TodoActivityScreen(todoViewModel)
                }
            }
        }
    }
    /**
     * TodoScreen 컴포저블과 ViewModel의 다리역할을 해준다.
     * TodoScreen에 바로 Viewmodel을 할당할 수도 있지만, 그렇게 되면 재사용성이 떨어진다.
     * List<TodoItem>과 같은 간단한 Parameter를 넘겨줌으로써 상태 호이스팅에 빠지지 않게된다.
     *
     * todoViewModel.todoItems : 뷰모델에서의 LiveData<List<TodoItem>. 라이브 데이터
     * .observeAsState : LiveData<T>를 관찰하고 State<T>로 변환함으로써, 컴포즈가 값 변화를 탐지할 수 있게 해준다.
     * listOf() : LiveData가 초기화되기 전에 null값을 피하기위한 초기값.
     * by : 코틀린 문법. observeAsState로부터 State<List<TodoItem>>를 List<TodoItem>로 바꿔준다.
     */
    @Composable
    private fun TodoActivityScreen(todoViewModel: TodoViewModel) {
        TodoScreen(
            items = todoViewModel.todoItems,
            currentlyEditing = todoViewModel.currentEditItem,
            //아래 두 개가 events up! 코틀린 람다 형태로 전달된다.
            onAddItem = todoViewModel::addItem,
            onRemoveItem = todoViewModel::removeItem,
            onStartEdit = todoViewModel::onEditItemSelected,
            onEditItemChange = todoViewModel::onEditItemChange,
            onEditDone = todoViewModel::onEditDone
        )
    }
}
