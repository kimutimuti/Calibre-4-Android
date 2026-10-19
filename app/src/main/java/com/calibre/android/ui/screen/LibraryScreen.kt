package com.calibre.android.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calibre.android.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToBookDetail: (String) -> Unit
) {
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // 1. ファイルピッカーを定義
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // TODO: ViewModelにURIを渡してデータベースに追加する処理をここに記述
            // viewModel.importBook(it) 
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calibre Library") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                // 2. プラスボタンタップでファイルピッカーを起動（すべてのファイルタイプを対象）
                filePickerLauncher.launch("*/*") 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Book")
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(paddingValues))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(paddingValues)
            ) {
                items(books) { book ->
                    Card(
                        onClick = { onNavigateToBookDetail(book.id) },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(book.title, style = MaterialTheme.typography.titleMedium, maxLines = 2)
                            Text(book.author ?: "Unknown", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
