package br.tiagohm.restler.logic

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.ContentResolverCompat.query

fun ContentResolver.query(uri: Uri, columnName: String) = query(this, uri, null, null, null, null, null)?.use {
    val nameIndex = it.getColumnIndex(columnName)
    it.moveToFirst()
    it.getString(nameIndex)
}

fun ContentResolver.fileName(uri: Uri) = query(uri, OpenableColumns.DISPLAY_NAME)

fun ContentResolver.fileSize(uri: Uri) = query(uri, OpenableColumns.SIZE)
