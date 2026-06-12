package com.example.trabalhodenis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trabalhodenis.ui.theme.InputBackground
import com.example.trabalhodenis.ui.theme.PrimaryBlack
import com.example.trabalhodenis.ui.theme.SecondaryGray
import java.util.Calendar

@Composable
fun CalendarScreen(
    events: List<CalendarEvent>,
    isDarkMode: Boolean = false,
    onAddEvent: (String, String, Int, Int, Int, String, String) -> Unit,
    onDeleteEvent: (Int) -> Unit
) {
    val today = Calendar.getInstance()
    val textColor = if (isDarkMode) Color.White else PrimaryBlack

    var visibleMonth by remember { mutableIntStateOf(today.get(Calendar.MONTH)) }
    var visibleYear by remember { mutableIntStateOf(today.get(Calendar.YEAR)) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val monthEvents = events.filter {
        it.month == visibleMonth && it.year == visibleYear
    }

    val selectedDayEvents = selectedDay?.let { day ->
        events.filter {
            it.day == day && it.month == visibleMonth && it.year == visibleYear
        }
    } ?: emptyList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (visibleMonth == 0) {
                            visibleMonth = 11
                            visibleYear -= 1
                        } else {
                            visibleMonth -= 1
                        }
                        selectedDay = null
                    }
                ) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = "Mês anterior",
                        tint = textColor
                    )
                }

                Text(
                    text = "${monthName(visibleMonth)} $visibleYear",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor
                )

                IconButton(
                    onClick = {
                        if (visibleMonth == 11) {
                            visibleMonth = 0
                            visibleYear += 1
                        } else {
                            visibleMonth += 1
                        }
                        selectedDay = null
                    }
                ) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Próximo mês",
                        tint = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb").forEach {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = if (isDarkMode) Color.LightGray else SecondaryGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            CalendarGrid(
                month = visibleMonth,
                year = visibleYear,
                selectedDay = selectedDay,
                events = monthEvents,
                isDarkMode = isDarkMode,
                onDayClick = { day ->
                    selectedDay = day
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedDay == null) {
                        "Eventos do mês (${monthEvents.size})"
                    } else {
                        "Eventos do dia $selectedDay (${selectedDayEvents.size})"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor
                )

                Button(
                    onClick = {
                        if (selectedDay != null) {
                            showDialog = true
                        }
                    },
                    enabled = selectedDay != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkMode) Color.White else PrimaryBlack,
                        contentColor = if (isDarkMode) PrimaryBlack else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Novo Evento", fontSize = 12.sp)
                }
            }

            if (selectedDay == null) {
                Text(
                    "Selecione um dia para adicionar ou visualizar eventos específicos.",
                    modifier = Modifier.padding(top = 8.dp),
                    color = if (isDarkMode) Color.Gray else SecondaryGray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        val listToShow = if (selectedDay == null) monthEvents else selectedDayEvents

        if (listToShow.isEmpty()) {
            item {
                Text(
                    text = if (selectedDay == null) {
                        "Nenhum evento cadastrado neste mês."
                    } else {
                        "Nenhum evento cadastrado neste dia."
                    },
                    color = if (isDarkMode) Color.Gray else SecondaryGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        items(listToShow) { event ->
            CalendarEventItem(
                event = event,
                isDarkMode = isDarkMode,
                onDelete = { onDeleteEvent(event.id) }
            )
        }
    }

    if (showDialog && selectedDay != null) {
        NewCalendarEventDialog(
            selectedDay = selectedDay!!,
            selectedMonth = visibleMonth,
            selectedYear = visibleYear,
            isDarkMode = isDarkMode,
            onDismiss = { showDialog = false },
            onAddEvent = { title, subject, time, type ->
                onAddEvent(title, subject, selectedDay!!, visibleMonth, visibleYear, time, type)
                showDialog = false
            }
        )
    }
}

@Composable
fun CalendarGrid(
    month: Int,
    year: Int,
    selectedDay: Int?,
    events: List<CalendarEvent>,
    isDarkMode: Boolean,
    onDayClick: (Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val startPadding = firstDayOfWeek - 1

    val cells = List(startPadding) { null } + (1..daysInMonth).toList()
    val rows = cells.chunked(7)

    Column {
        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                row.forEach { day ->
                    if (day == null) {
                        Spacer(modifier = Modifier.size(40.dp))
                    } else {
                        val hasEvent = events.any { it.day == day }
                        DayCell(
                            day = day,
                            isSelected = selectedDay == day,
                            hasEvent = hasEvent,
                            isDarkMode = isDarkMode,
                            onClick = { onDayClick(day) }
                        )
                    }
                }
                if (row.size < 7) {
                    repeat(7 - row.size) {
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DayCell(
    day: Int,
    isSelected: Boolean,
    hasEvent: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    val borderColor = if (isDarkMode) Color.DarkGray else Color(0xFFEEEEEE)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(
                    width = 1.dp,
                    color = if (isSelected) textColor else borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = if (isSelected) {
                        if (isDarkMode) Color(0xFF444444) else Color(0xFFE0E0E0)
                    } else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.toString(),
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )
        }

        if (hasEvent) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(3.dp)
                    .background(if (isDarkMode) Color.White else PrimaryBlack, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun CalendarEventItem(
    event: CalendarEvent,
    isDarkMode: Boolean,
    onDelete: () -> Unit
) {
    val backgroundColor = when (event.type) {
        "Prova" -> if (isDarkMode) Color(0xFF4D2C2C) else Color(0xFFFFD1D1)
        "Trabalho" -> if (isDarkMode) Color(0xFF4D412C) else Color(0xFFFFE8B3)
        else -> if (isDarkMode) Color(0xFF333333) else Color(0xFFE0E0E0)
    }

    val textColor = if (isDarkMode) Color.White else PrimaryBlack

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, if (isDarkMode) Color.DarkGray else Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Badge(
                        containerColor = if (isDarkMode) Color.White else PrimaryBlack,
                        contentColor = if (isDarkMode) PrimaryBlack else Color.White
                    ) {
                        Text(
                            event.type,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        event.subject,
                        fontSize = 12.sp,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    event.title,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Text(
                    "${event.day} de ${monthName(event.month)} de ${event.year} às ${event.time}",
                    fontSize = 12.sp,
                    color = if (isDarkMode) Color.LightGray else SecondaryGray
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Excluir evento",
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun NewCalendarEventDialog(
    selectedDay: Int,
    selectedMonth: Int,
    selectedYear: Int,
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onAddEvent: (String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Prova") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Evento", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    "Data: $selectedDay de ${monthName(selectedMonth)} de $selectedYear",
                    fontSize = 13.sp,
                    color = if (isDarkMode) Color.LightGray else SecondaryGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Título", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Ex: Prova de Matemática") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Matéria", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = subject,
                    onValueChange = { subject = it },
                    placeholder = { Text("Ex: História") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Horário", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = time,
                    onValueChange = { time = it },
                    placeholder = { Text("Ex: 14:30") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Tipo", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Prova", "Trabalho", "Estudo").forEach { option ->
                        FilterChip(
                            selected = type == option,
                            onClick = { type = option },
                            label = { Text(option) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank()) onAddEvent(title.trim(), subject.trim(), time.trim(), type) },
                colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color.White else PrimaryBlack, contentColor = if (isDarkMode) PrimaryBlack else Color.White)
            ) { Text("Adicionar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        containerColor = if (isDarkMode) Color(0xFF222222) else Color.White
    )
}

fun monthName(month: Int): String {
    return when (month) {
        0 -> "Janeiro"
        1 -> "Fevereiro"
        2 -> "Março"
        3 -> "Abril"
        4 -> "Maio"
        5 -> "Junho"
        6 -> "Julho"
        7 -> "Agosto"
        8 -> "Setembro"
        9 -> "Outubro"
        10 -> "Novembro"
        11 -> "Dezembro"
        else -> ""
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarScreenPreview() {
    CalendarScreen(
        events = listOf(
            CalendarEvent(
                id = 1,
                title = "Prova de Matemática",
                subject = "Matemática",
                day = 23,
                month = Calendar.getInstance().get(Calendar.MONTH),
                year = Calendar.getInstance().get(Calendar.YEAR),
                time = "14:30",
                type = "Prova"
            )
        ),
        isDarkMode = false,
        onAddEvent = { _, _, _, _, _, _, _ -> },
        onDeleteEvent = { _ -> }
    )
}
