package com.junkfood.seal.ui.page.downloadv2

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.junkfood.seal.R
import com.junkfood.seal.ui.component.DrawerSheetSubtitle
import com.junkfood.seal.ui.component.PasteButton
import com.junkfood.seal.ui.component.SealModalBottomSheet
import com.junkfood.seal.ui.component.SingleChoiceChip
import com.junkfood.seal.ui.component.VideoFilterChip
import com.junkfood.seal.ui.page.download.FormatPageContentPreview
import com.junkfood.seal.ui.page.download.FormatPagePreview
import com.junkfood.seal.ui.theme.SealTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "DownloadSettingsDialog"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadSettingsDialog(sheetState: SheetState, onDismissRequest: () -> Unit) {
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFormatSelection by remember {
        mutableStateOf(false)
    }

    BackHandler(enabled = showFormatSelection) {
        showFormatSelection = false
    }
    val dragHandle: @Composable() (() -> Unit) = {
        if (!showFormatSelection) BottomSheetDefaults.DragHandle()
    }
    val windowInsets = WindowInsets(0)
    SealModalBottomSheet(
        modifier = Modifier,
        onDismissRequest = onDismissRequest, sheetState = sheetState,
        horizontalPadding = PaddingValues(),
        windowInsets = windowInsets,
        dragHandle = dragHandle,
    ) {

        Log.d(
            TAG,
            "DownloadSettingsDialog: ${BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Top)}"
        )
        Log.d(
            TAG,
            "DownloadSettingsDialog: ${windowInsets}"
        )

        AnimatedContent(targetState = showFormatSelection, label = "", transitionSpec = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                .togetherWith(fadeOut(animationSpec = tween(90)))
        }) {
            if (!it) {
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
                        text = stringResource(R.string.new_task),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    DrawerSheetSubtitle(text = stringResource(id = R.string.video_url))
                    OutlinedTextField(
//                shape = MaterialTheme.shapes.extraLarge,
                        value = "https://www.example.com",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
//                label = {
//                    Text(
//                        text = stringResource(id = R.string.video_url)
//                    )
//                },
                        trailingIcon = { PasteButton() },
//                minLines = 3,
                        maxLines = 3,
                    )

                    DrawerSheetSubtitle(text = stringResource(id = R.string.download_type))

                    var audio by remember { mutableStateOf(false) }
                    var customCommand by remember { mutableStateOf(false) }
                    var playlist by remember { mutableStateOf(true) }

                    var selected by remember {
                        mutableIntStateOf(2)
                    }

                    val audioSelected by remember { derivedStateOf { audio && !customCommand } }
                    val videoSelected by remember { derivedStateOf { !audio && !customCommand } }
                    val commandSelected by remember { derivedStateOf { customCommand } }

                    /*            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
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
                                        selected = videoSelected
                                    ) {
                                        audio = false
                                        customCommand = false
                                    }
                                    SingleChoiceSegmentedButton(
                                        text = stringResource(id = R.string.commands),
                                        selected = commandSelected,
                                        position = SegmentedButtonValues.END
                                    ) {
                                        customCommand = true
                                    }
                                }*/
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        SingleChoiceChip(
                            label = "Audio",
                            selected = selected == 0 && !customCommand
                        ) {}
                        SingleChoiceChip(
                            label = "Video",
                            selected = selected == 1 && !customCommand
                        ) {}
                        SingleChoiceChip(
                            label = "Playlist",
                            selected = selected == 2 && !customCommand
                        ) {}
                        SingleChoiceChip(label = "Command", selected = customCommand) {}

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
                            onClick = { /*TODO*/ },
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
                            onClick = { showFormatSelection = true },
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                            modifier = Modifier,
//                    enabled = false
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = ButtonDefaults.IconSpacing)
                                    .size(ButtonDefaults.IconSize)
                            )
                            Text(text = "Continue")
                        }
                    }
                }
            } else {
                FormatPagePreview()
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DialogPreview() {
    var showSheet by remember {
        mutableStateOf(true)
    }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { sheetValue ->
                when (sheetValue) {
                    SheetValue.Hidden -> false
                    SheetValue.Expanded -> true
                    SheetValue.PartiallyExpanded -> true
                }
            })
    SealTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(it)) {
                Button(onClick = {
                    showSheet = true
                    scope.launch {
                        delay(50)
                        sheetState.show()
                    }
                }) {
                    Text(text = "Show sheet")
                }
                if (showSheet) {
                    DownloadSettingsDialog(sheetState = sheetState, onDismissRequest = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            showSheet = false
                        }
                    })
                }
            }
        }
    }

}