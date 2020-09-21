package com.example.ktforfilemanager.drivehelper

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File

import java.io.IOException
import java.util.Collections
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.math.log

class DriveServiceHelper(private val mDriveService: Drive) {
    private val mExecutor = Executors.newSingleThreadExecutor()
    private lateinit var  name:String
    private lateinit var   myFile: File

    fun createFile(filePath: String, name: String): Task<String> {
        return Tasks.call(mExecutor, Callable {
            val fileMetaData = File()
            fileMetaData.name = name

            val file = java.io.File(filePath)

            val mediaContent = FileContent("application/octet-stream", file)


            try {
                myFile = mDriveService.files().create(fileMetaData, mediaContent).execute()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("nnnnnnnnnnnnn",e.toString())
            }


            if (myFile == null) {
                throw IOException("Null result when request file creation")
            }

            return@Callable myFile.id
        })
    }
    fun fileName(fileName:String):Unit{
        name = fileName
    }
}
