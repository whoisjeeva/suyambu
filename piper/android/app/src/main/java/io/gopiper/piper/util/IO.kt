package io.gopiper.piper.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import io.gopiper.piper.core.encode
import me.gumify.hiper.util.onUiThread
import me.gumify.hiper.util.toast
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

object IO {
    fun readFromAsset(context: Context, filePath: String): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(context.assets.open(filePath), "UTF-8"))

            // do reading, usually loop until end of file reading
            var line: String?
            var s = ""
            while (reader.readLine().also { line = it } != null) {
                s += line
            }
            return s
        } catch (e: IOException) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    //log the exception
                }
            }
        }
        return null
    }

    fun copyToSdCard(context: Context, file: File, sdCardLocation: String, mimeType: String = "*/*") {
        thread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, file.name)
                values.put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                values.put(MediaStore.Images.Media.RELATIVE_PATH, sdCardLocation)
                val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)
                val descriptor =
                    context.contentResolver.openFileDescriptor(uri!!, "w") //"w" specify's write mode
                val fileDescriptor: FileDescriptor = descriptor!!.fileDescriptor
                val dataInputStream: InputStream = FileInputStream(file.absolutePath)

                val output = FileOutputStream(fileDescriptor)
                val buf = ByteArray(4096)
                var bytesRead = dataInputStream.read(buf)
                var copiedBytes = bytesRead.toLong()
                while (bytesRead > 0) {
                    output.write(buf, 0, bytesRead)
                    copiedBytes += bytesRead
                    bytesRead = dataInputStream.read(buf)
                }
                dataInputStream.close()
                output.close()
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(sdCardLocation).absolutePath
                val destination = RandomAccessFile("$dir${File.separator}${file.name}", "rw")
                destination.seek(0)

                var fis: FileInputStream? = null
                try {
                    fis = FileInputStream(file)
                    val bytes = ByteArray(4096)
                    var bytesRead = fis.read(bytes)
                    var copiedBytes = bytesRead.toLong()
                    while (bytesRead != -1) {
                        destination.write(bytes, 0, bytesRead)
                        copiedBytes += bytesRead
                        bytesRead = fis.read(bytes)
                    }
                } catch (e: Exception) {

                } finally {
                    destination.close()
                    fis?.close()
                }
            }

            if (file.exists()) file.delete()

            onUiThread {
                context.toast("File written to $sdCardLocation directory")
            }
        }
    }



    fun writeToSdCard(
        context: Context,
        fileName: String,
        content: String,
        sdCardLocation: String,
        mimeType: String = "*/*",
        onComplete: () -> Unit
    ) {
        thread {
            val encoded = content.encode().toByteArray()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, fileName)
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                values.put(MediaStore.Images.Media.RELATIVE_PATH, sdCardLocation)
                val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)
                val descriptor =
                    context.contentResolver.openFileDescriptor(uri!!, "w") //"w" specify's write mode
                val fileDescriptor: FileDescriptor = descriptor!!.fileDescriptor

                val output = FileOutputStream(fileDescriptor)
                output.write(encoded)
                output.close()
            } else {
                val dir = Environment.getExternalStoragePublicDirectory(sdCardLocation).absolutePath
                val destination = RandomAccessFile("$dir${File.separator}${fileName}", "rw")
                destination.seek(0)

                try {
                    destination.write(encoded)
                } catch (e: Exception) {

                } finally {
                    destination.close()
                }
            }
            onUiThread {
                context.toast("File written to $sdCardLocation/$fileName", true)
                onComplete()
            }
        }
    }


    fun readUri(context: Context, uri: Uri): String? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val text = StringBuilder()
            val reader = BufferedReader(
                InputStreamReader(
                    inputStream,
                    Charset.forName(StandardCharsets.UTF_8.name())
                )
            )
            var c: Int
            while (reader.read().also { c = it } != -1) {
                text.append(c.toChar())
            }
            return text.toString()
        } catch (e: Exception) {
            return null
        }
    }
}