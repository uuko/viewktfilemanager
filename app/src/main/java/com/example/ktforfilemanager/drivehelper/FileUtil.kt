package com.example.ktforfilemanager.drivehelper

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import java.io.File
import java.util.*

object FileUtil {
    /**
     * 根据Uri获取图片绝对路径
     * @param context context
     * @param uri     uri
     */
    fun getFileAbsolutePath(context: Context?, uri: Uri?): String? {
        if (context == null || uri == null) return null
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
                context,
                uri
            )
        ) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                } else if ("home".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/documents/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                val id = DocumentsContract.getDocumentId(uri)
                if (TextUtils.isEmpty(id)) {
                    return null
                }
                if (id.startsWith("raw:")) {
                    return id.substring(4)
                }
                val contentUriPrefixesToTry = arrayOf(
                    "content://downloads/public_downloads",
                    "content://downloads/my_downloads",
                    "content://downloads/all_downloads"
                )
                for (contentUriPrefix in contentUriPrefixesToTry) {
                    try {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse(contentUriPrefix),
                            java.lang.Long.valueOf(id)
                        )
                        val path = getDataColumn(
                            context,
                            contentUri,
                            null,
                            null
                        )
                        if (path != null) {
                            return path
                        }
                    } catch (ignore: Exception) {
                    }

                }
                try {
                    val path = getDataColumn(
                        context,
                        uri,
                        null,
                        null
                    )
                    if (path != null) {
                        return path
                    }
                } catch (ignore: Exception) {
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                return null
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                val contentUri: Uri
                when (type.toLowerCase(Locale.ENGLISH)) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> contentUri = MediaStore.Files.getContentUri("external")
                }
                val selection = MediaStore.MediaColumns._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(
                    context,
                    contentUri,
                    selection,
                    selectionArgs
                )
            }
        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.scheme!!, ignoreCase = true)) {
            // MediaStore (and general)
            // Return the remote address
            return if (isGooglePhotosUri(uri)) {
                uri.lastPathSegment
            } else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if (ContentResolver.SCHEME_FILE.equals(uri.scheme!!, ignoreCase = true)) {
            // File
            return uri.path
        }
        return null
    }

    /**
     * 通过游标获取当前文件路径
     * @param context       context
     * @param uri           uri
     * @param selection     selection
     * @param selectionArgs selectionArgs
     * @return 路径，未找到返回null
     */
    fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } catch (ignore: Exception) {
        } finally {
            cursor?.close()
        }
        return null
    }

    //檔案名
    fun fileName(path: String?): String {
        if (path == null || path.isEmpty()) return ""

        val file = File(path)

        return if (!file.exists() || !file.isFile) {
            ""
        } else file.name

//        String ext      = FileKit.getFileExt(filename);
        //        if( !ext.equalsIgnoreCase(".csv") ){
        //            return "";
        //        }

    }


    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}
