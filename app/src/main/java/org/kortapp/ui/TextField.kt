package org.kortapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map


object DigitsOnlyTransformation : InputTransformation {
    override val keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    override fun TextFieldBuffer.transformInput() {
        if (!asCharSequence().isDigitsOnly()) {
            revertAllChanges()
        }
    }
}

@Composable
fun TextFieldTrailingIcon(state: TextFieldState) {
    Icon(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(false),
            onClick = {
                if (state.text.isNotEmpty()) {
                    state.clearText()
                }
            }
        ),
        imageVector = Icons.Default.Clear,
        contentDescription = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTextField(
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    isError: Boolean = false,
    onValueChanged: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    cursorBrush: Brush = SolidColor(colors.cursorColor),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    inputTransformation: InputTransformation? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    state: TextFieldState = rememberTextFieldState(),
    shape: Shape = TextFieldDefaults.shape,
    trailingIcon: @Composable (() -> Unit)? = { TextFieldTrailingIcon(state) },
    interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    }
) {

    LaunchedEffect(Unit) {
        snapshotFlow { state.text }
            .map {
                val text = it.toString()
                onValueChanged(text)
            }.collect()
    }

    BasicTextField(
        modifier = modifier,
        state = state,
        keyboardOptions = keyboardOptions,
        interactionSource = interactionSource,
        lineLimits = lineLimits,
        readOnly = readOnly,
        cursorBrush = cursorBrush,
        textStyle = TextStyle.Default.copy(color = colors.focusedTextColor),
        inputTransformation = inputTransformation,
        decorator = { innerTextField: @Composable () -> Unit ->
            TextFieldDefaults.DecorationBox(
                isError = isError,
                value = state.text.toString(),
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                suffix = suffix,
                label = label,
                placeholder = placeholder,
                contentPadding = TextFieldPadding,
                trailingIcon = trailingIcon,
                leadingIcon = leadingIcon,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        shape = shape,
                        focusedBorderThickness = FocusedBorderThickness,
                        unfocusedBorderThickness = UnfocusedBorderThickness
                    )
                }
            )
        }
    )
}

val UnfocusedBorderThickness = 0.5.dp
val FocusedBorderThickness = 0.5.dp
private val TextFieldPadding = PaddingValues(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)

@Composable
@Preview
fun TextFieldPreview() {
    Column(Modifier.padding(48.dp)) {
        val text = rememberTextFieldState("hello world")
        RegularTextField(state = text, onValueChanged = { _ -> })
    }
}