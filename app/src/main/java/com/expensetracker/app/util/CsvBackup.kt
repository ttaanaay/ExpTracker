package com.expensetracker.app.util

import android.content.Context
import android.net.Uri
import com.expensetracker.app.data.local.entity.TransactionEntity
import com.expensetracker.app.data.model.TransactionType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * Simple CSV backup/restore for transactions.
 * Columns: id,type,amount,categoryId,dateEpochDay,timeMinutes,note,createdAtMillis
 * Note field is quoted and internal quotes/commas are escaped.
 */
object CsvBackup {

    private const val HEADER = "id,type,amount,categoryId,dateEpochDay,timeMinutes,note,createdAtMillis"

    fun export(context: Context, uri: Uri, transactions: List<TransactionEntity>) {
        context.contentResolver.openOutputStream(uri)?.use { out ->
            OutputStreamWriter(out, Charsets.UTF_8).use { writer ->
                writer.appendLine(HEADER)
                transactions.forEach { t ->
                    writer.appendLine(
                        listOf(
                            t.id,
                            t.type.name,
                            t.amount,
                            t.categoryId ?: "",
                            t.dateEpochDay,
                            t.timeMinutes,
                            escapeCsv(t.note),
                            t.createdAtMillis
                        ).joinToString(",")
                    )
                }
            }
        }
    }

    fun import(context: Context, uri: Uri): List<TransactionEntity> {
        val result = mutableListOf<TransactionEntity>()
        context.contentResolver.openInputStream(uri)?.use { input ->
            BufferedReader(InputStreamReader(input, Charsets.UTF_8)).use { reader ->
                val lines = reader.readLines()
                if (lines.isEmpty()) return emptyList()
                lines.drop(1).forEach { line ->
                    if (line.isBlank()) return@forEach
                    val cols = parseCsvLine(line)
                    if (cols.size < 8) return@forEach
                    result.add(
                        TransactionEntity(
                            id = 0, // let Room auto-generate to avoid id collisions on restore
                            type = runCatching { TransactionType.valueOf(cols[1]) }.getOrDefault(TransactionType.EXPENSE),
                            amount = cols[2].toDoubleOrNull() ?: 0.0,
                            categoryId = cols[3].toLongOrNull(),
                            dateEpochDay = cols[4].toLongOrNull() ?: 0L,
                            timeMinutes = cols[5].toIntOrNull() ?: 0,
                            note = unescapeCsv(cols[6]),
                            createdAtMillis = cols[7].toLongOrNull() ?: System.currentTimeMillis()
                        )
                    )
                }
            }
        }
        return result
    }

    private fun escapeCsv(value: String): String {
        val needsQuoting = value.contains(',') || value.contains('"') || value.contains('\n')
        val escaped = value.replace("\"", "\"\"")
        return if (needsQuoting) "\"$escaped\"" else escaped
    }

    private fun unescapeCsv(value: String): String =
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value.substring(1, value.length - 1).replace("\"\"", "\"")
        } else value

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            when {
                c == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        current.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                c == ',' && !inQuotes -> {
                    result.add(current.toString())
                    current.clear()
                }
                else -> current.append(c)
            }
            i++
        }
        result.add(current.toString())
        return result
    }
}
