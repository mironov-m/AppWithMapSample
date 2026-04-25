package ru.mironov.appwithmapsample.core.android

import android.content.Context
import android.content.Intent
import android.net.Uri

fun searchInBrowser(context: Context, query: String) {
    val uri = Uri.parse("https://www.google.com/search?q=${Uri.encode(query)}")
    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}