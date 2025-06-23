package com.amaurysdelossantos.project.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.amaurysdelossantos.project.database.enums.BookFormat
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import kotlinx.cinterop.get


@Composable
actual fun rememberDocumentManager(onResult: (SharedDocument?) -> Unit): DocumentManager {
    val documentPicker = UIDocumentPickerViewController(
        documentTypes = listOf("public.item"),
        inMode = UIDocumentPickerMode.UIDocumentPickerModeOpen
    )
    val documentDelegate = remember {
        object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentAtURL: NSURL) {
                didPickDocumentAtURL.startAccessingSecurityScopedResource()
                val data = NSData.dataWithContentsOfURL(didPickDocumentAtURL)
                didPickDocumentAtURL.stopAccessingSecurityScopedResource()
                onResult.invoke(SharedDocument(data))
                controller.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember {
        DocumentManager {
            documentPicker.setDelegate(documentDelegate)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(documentPicker, true, null)
        }
    }
}

actual class DocumentManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}

actual class SharedDocument(private val data: NSData?) {
    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? {
        return data?.let {
            val bytes = it.bytes?.reinterpret<ByteVar>() ?: return null
            val length = it.length.toInt()
            ByteArray(length) { index -> bytes[index] }
        }
    }

    actual fun bookFormat(): BookFormat? {
        val name = fileName()?.lowercase()
        return if (name == null) {
            null
        }else {
            GetBookFormat(name)
        }
    }

    actual fun fileName(): String? {
        return null
    }
}