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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {

      //아래 코드를 삭제한다.
//    private var _todoItems = MutableLiveData(listOf<TodoItem>())
//    val todoItems: LiveData<List<TodoItem>> = _todoItems

    private var currentEditPosition by mutableStateOf(-1) // State는 반드시 State<T> 형태로부터 읽어져야 한다.

    // mutableStateListOf는 observable한 MutableList의 instance를 생성해준다. 이말인 즉슨, LiveData<List>를 사용함으로 얻는 Overhead를 없앨 수 있다는 뜻이다.
    // 하지만 ViewModel이 View에도 쓰이면 그 때는 LiveData를 사용하는 편이 더 좋다.
    var todoItems = mutableStateListOf<TodoItem>()
        private set
    //private set을 해주면, Setter를 오로지 이 ViewmModel 안에서만 보이도록 한다.

    val currentEditItem: TodoItem? get() = todoItems.getOrNull(currentEditPosition) // state

    fun addItem(item: TodoItem) {
        //_todoItems.value = _todoItems.value!! + listOf(item)
        todoItems.add(item)
    }

    fun removeItem(item: TodoItem) {
//        _todoItems.value = _todoItems.value!!.toMutableList().also {
//            it.remove(item)
//        }
        todoItems.remove(item)
        onEditDone() // 아이템이 삭제되면 Edit창 Close.
    }

    //이벤트 : 수정할 아이템이 선택됐을 때.
    fun onEditItemSelected(item: TodoItem) {
        currentEditPosition = todoItems.indexOf(item)
    }

    //이벤트 : 수정이 끝났을 때.
    fun onEditDone() {
        currentEditPosition = -1
    }

    //이벤트 : 투두아이템 수정, currendEditPosition에 있는 item수정.
    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {
            "You can only change an item with the same id as currentEditItem"
        }

        todoItems[currentEditPosition] = item
    }
}
