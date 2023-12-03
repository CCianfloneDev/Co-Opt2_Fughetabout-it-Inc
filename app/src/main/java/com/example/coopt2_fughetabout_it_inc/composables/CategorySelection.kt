package com.example.coopt2_fughetabout_it_inc.composables

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.coopt2_fughetabout_it_inc.data.Category


/**
 * Composable for creating or deleting categories.
 *
 * @param categories LiveData of categories.
 * @param onCategoryCreated Callback when a new category is created.
 * @param onCategoryDeleted Callback when a category is deleted.
 * @param onCancel Callback when the user cancels the operation.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategorySelectionScreen(
    categories: LiveData<List<Category>>,
    onCategoryCreated: (String) -> Unit,
    onCategoryDeleted: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val categoriesList = categories.observeAsState(emptyList())
    var newCategoryName by remember { mutableStateOf("") }
    var isCreatingNewCategory by remember { mutableStateOf(false) }
    var selectedCategoryName by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var expandedCatDropdown by remember { mutableStateOf(false) }
    var isEditingCategory = false

    val focusManager = LocalFocusManager.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {

                focusManager.clearFocus()
            })
        }) {


            if (isCreatingNewCategory) {

                Text(
                    text = "Create Category",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .align(Alignment.CenterHorizontally)
                )
                // Text field for entering a new category name

                TextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    label = { Text("Category Name") },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)

                )

                // Save and Cancel buttons for creating a new category
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (newCategoryName.isNotBlank()) {
                                onCategoryCreated(newCategoryName)
                                newCategoryName = ""
                            }
                        }
                    ) {
                        Text("Add")
                    }

                    Button(
                        onClick = {
                            isCreatingNewCategory = false
                            newCategoryName = ""
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            }

            if (!isEditingCategory) {
                Text(
                    text = "Select Category",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .align(Alignment.CenterHorizontally)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedCatDropdown,
                    onExpandedChange = {
                        expandedCatDropdown = !expandedCatDropdown
                    },
                    modifier = Modifier
                        .padding(horizontal = 1.dp)
                        .align(Alignment.CenterHorizontally)

                ) {

                    TextField(
                        value = selectedCategoryName,
                        onValueChange = {},
                        readOnly = true,

                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatDropdown) }
                    )

                    DropdownMenu(
                        expanded = expandedCatDropdown,
                        onDismissRequest = { expandedCatDropdown = false },

                        ) {
                        categoriesList.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategoryName = category.name
                                    selectedCategoryId = category.id
                                    expandedCatDropdown = false
                                    isCreatingNewCategory = false
                                },
                            ) {
                                Text(
                                    text = category.name,
                                    modifier = Modifier
                                        .padding(horizontal = 1.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                Text(
                    "Category chosen: $selectedCategoryName",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 16.dp, top = 5.dp)
                )
                Button(
                    onClick = {
                        selectedCategoryName = ""
                        onCategoryDeleted(selectedCategoryId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Delete")
                }
            }


            Button(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.weight(1f))
            // Button to show text field for creating a new category
            Button(
                onClick = { isCreatingNewCategory = true; isEditingCategory = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {
                Text("Create new Category")
            }
        }

    }
}