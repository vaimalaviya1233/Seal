package com.junkfood.seal.ui.page.download

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junkfood.seal.Downloader
import com.junkfood.seal.R
import com.junkfood.seal.ui.component.DrawerSheetSubtitle
import com.junkfood.seal.ui.component.PlaylistItem
import com.junkfood.seal.ui.component.SealModalBottomSheet
import com.junkfood.seal.ui.component.SegmentedButtonValues
import com.junkfood.seal.ui.component.SingleChoiceSegmentedButton
import com.junkfood.seal.ui.component.VideoFilterChip
import com.junkfood.seal.util.Entries
import com.junkfood.seal.util.PlaylistResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PlaylistSelectionPage(onBackPressed: () -> Unit = {}) {
    val playlistInfo by Downloader.playlistResult.collectAsStateWithLifecycle()
    PlaylistSelectionPageImpl(
        playlistInfo = playlistInfo,
        onDownloadPressed = { url, selectedItems ->
            Downloader.downloadVideoInPlaylistByIndexList(
                url = url, indexList = selectedItems
            )
            onBackPressed()
        },
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistSelectionPageImpl(
    playlistInfo: PlaylistResult,
    onDownloadPressed: (String, List<Int>) -> Unit,
    onBackPressed: () -> Unit = {}
) {
    val onDismissRequest = {
        onBackPressed()
    }
    val selectedItems = rememberSaveable(saver = listSaver<MutableList<Int>, Int>(
        save = {
            if (it.isNotEmpty()) {
                it.toList()
            } else {
                emptyList()
            }
        },
        restore = {
            it.toMutableStateList()
        }
    )) { mutableStateListOf() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showDialog by remember { mutableStateOf(false) }
    val playlistCount = playlistInfo.entries?.size ?: 0

//    BackHandler { onDismissRequest() }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = if (selectedItems.isEmpty()) stringResource(id = R.string.download_playlist) else stringResource(
                        id = R.string.selected_item_count
                    ).format(selectedItems.size),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                )
            }, navigationIcon = {
                IconButton(onClick = { onDismissRequest() }) {
                    Icon(Icons.Outlined.Close, stringResource(R.string.close))
                }
            }, actions = {
                TextButton(
                    modifier = Modifier.padding(end = 8.dp), onClick = {
                        onDownloadPressed(playlistInfo.webpageUrl.toString(), selectedItems)
                    }, enabled = selectedItems.isNotEmpty()
                ) {
                    Text(text = stringResource(R.string.start_download))
                }
            }, scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.Center
            ) {
                Divider(modifier = Modifier.fillMaxWidth())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        modifier = Modifier.selectable(selected = selectedItems.size == playlistCount && selectedItems.size != 0,
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            onClick = {
                                if (selectedItems.size == playlistCount) selectedItems.clear() else {
                                    selectedItems.clear()
                                    selectedItems.addAll(1..playlistCount)
                                }
                            }), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            modifier = Modifier.padding(16.dp),
                            checked = selectedItems.size == playlistCount && selectedItems.size != 0,
                            onCheckedChange = null
                        )
                        Text(
                            text = stringResource(R.string.select_all),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(modifier = Modifier.padding(end = 4.dp),
                        onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.PlaylistAdd,
                            contentDescription = stringResource(
                                R.string.download_range_selection
                            )
                        )
                    }
                }
            }

        }) { paddings ->
        Column(
            modifier = Modifier.padding(paddings)
        ) {
            LazyColumn {
                item {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(R.string.download_selection_desc).format(playlistInfo.title),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                itemsIndexed(items = playlistInfo.entries ?: emptyList()) { _index, entry ->
                    val index = _index + 1
                    TooltipBox(state = rememberTooltipState(),
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text(text = entry.title ?: index.toString())
                            }
                        }) {
                        PlaylistItem(modifier = Modifier.padding(horizontal = 12.dp),
                            imageModel = entry.thumbnails?.lastOrNull()?.url ?: "",
                            title = entry.title ?: index.toString(),
                            author = entry.channel ?: entry.uploader ?: playlistInfo.channel
                            ?: playlistInfo.uploader,
                            selected = selectedItems.contains(index),
                            onClick = {
                                if (selectedItems.contains(index)) selectedItems.remove(index)
                                else selectedItems.add(index)
                            })
                    }

                }
            }
        }
    }
    if (showDialog) {
        PlaylistSelectionDialog(playlistInfo = playlistInfo,
            onDismissRequest = { showDialog = false },
            onConfirm = {
                selectedItems.clear()
                selectedItems.addAll(it)
            })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PlaylistSelectionPagePreview(
    onBackPressed: () -> Unit = {}
) {
    val playlistInfo = PlaylistResult(title = "Playlist Sample", entries = buildList {
        repeat(16) {
            add(Entries(title = "Video $it", duration = it * 20.0, uploader = "Uploader $it"))
        }
    })
    var showSheet by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)


    PlaylistSelectionPageImpl(
        playlistInfo = playlistInfo,
        onDownloadPressed = { _, _ ->
            showSheet = true
            scope.launch {
                delay(50)
                sheetState.show()
            }
        },
        onBackPressed = onBackPressed
    )

    if (showSheet) {
        DownloadSettingsDialogPlaylist(sheetState = sheetState, onDismissRequest = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                showSheet = false
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadSettingsDialogPlaylist(
    modifier: Modifier = Modifier,
    sheetState: SheetState = SheetState(
        skipPartiallyExpanded = true,
        density = LocalDensity.current,
        initialValue = SheetValue.Hidden
    ),
    onDismissRequest: () -> Unit,
    horizontalPadding: PaddingValues = PaddingValues(horizontal = 28.dp),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties(),
    content: @Composable ColumnScope.() -> Unit = {},
) {
    SealModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest, sheetState = sheetState,
        horizontalPadding = PaddingValues(),
    ) {

        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                imageVector = Icons.Outlined.FileDownload,
                contentDescription = null
            )
            Text(
                text = "Download from playlist",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 12.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            DrawerSheetSubtitle(text = stringResource(id = R.string.download_type))

            var audio by remember { mutableStateOf(false) }
            var customCommand by remember { mutableStateOf(false) }
            var playlist by remember { mutableStateOf(true) }

            var selected by remember {
                mutableIntStateOf(1)
            }

            val audioSelected by remember { derivedStateOf { audio && !customCommand } }
            val videoSelected by remember { derivedStateOf { !audio && !customCommand } }
            val commandSelected by remember { derivedStateOf { customCommand } }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
//                SingleChoiceChip(
//                    label = "Audio",
//                    selected = selected == 0 && !customCommand
//                ) {}
//                SingleChoiceChip(
//                    label = "Video",
//                    selected = selected == 1 && !customCommand
//                ) {}

                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SingleChoiceSegmentedButton(
                        text = stringResource(id = R.string.audio),
                        selected = audioSelected,
                        position = SegmentedButtonValues.START
                    ) {
                        audio = true
                        customCommand = false
                    }
                    SingleChoiceSegmentedButton(
                        text = stringResource(id = R.string.video),
                        selected = videoSelected,
                        position = SegmentedButtonValues.END

                    ) {
                        audio = false
                        customCommand = false
                    }
                }
//                SingleChoiceChip(
//                    label = "Playlist",
//                    selected = selected == 2 && !customCommand
//                ) {}
//                SingleChoiceChip(label = "Command", selected = customCommand) {}

            }
            DrawerSheetSubtitle(text = stringResource(id = R.string.additional_settings))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                repeat(4) {
                    VideoFilterChip(selected = false, label = "Setting $it") {}
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onDismissRequest,
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = ButtonDefaults.IconSpacing)
                            .size(ButtonDefaults.IconSize)
                    )
                    Text(text = stringResource(id = R.string.cancel))
                }
                Button(
                    onClick = { },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier,
//                    enabled = false
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DownloadDone,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = ButtonDefaults.IconSpacing)
                            .size(ButtonDefaults.IconSize)
                    )
                    Text(text = "Download")
                }
            }
        }


    }
}