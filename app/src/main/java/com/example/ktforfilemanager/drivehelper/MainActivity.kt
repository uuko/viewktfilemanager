package com.example.ktforfilemanager.drivehelper

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import android.widget.Toast
import android.app.ProgressDialog
import com.example.ktforfilemanager.R

import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    private var driveServiceHelper: DriveServiceHelper? = null
    private val PICK_CSV_FROM_GALLERY_REQUEST_CODE = 100
    private val filepa = ArrayList<String>()
    private val fileName = ArrayList<String>()
    private var btnChooseFile: Button? = null
    private var btnUplodFile: Button? = null
//    private val filepa = ArrayList()  //路徑名
//    private val fileName = ArrayList()  //檔案名稱

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnChooseFile = findViewById(R.id.btnChooseFile)
        requestSignIn()
        btnChooseFile?.setOnClickListener({
            it->chooseFile()
        })
        btnUplodFile=findViewById(R.id.btnUplodFile)
        btnUplodFile?.setOnClickListener({
            it->uploadFile()
        })
    }

    fun uploadFile() {

        val j = filepa.size
        if (j == 0) {
            Toast.makeText(this, "請選取資料", Toast.LENGTH_SHORT).show()
        } else {
            val progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setTitle("Uploading to Google Drive")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

            for (i in 0 until j) {
                //        driveServiceHelper.fileName(fileName.get(i));
                driveServiceHelper?.createFile(filepa.get(i), fileName.get(i))
                    ?.addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "Uploaded successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    }?.addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "Check your google api key",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("aaa", "" + e)
                    }
            }
        }
        filepa.clear()
        fileName.clear()
        //        Log.d("aaa",""+fileName.size());
    }

    private fun requestSignIn() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope(DriveScopes.DRIVE_FILE),
                Scope(DriveScopes.DRIVE_APPDATA)
            )
            .build()

        val client = GoogleSignIn.getClient(this, signInOptions)

        startActivityForResult(client.signInIntent, 400)
    }

    private fun chooseFile() {
        //https://www.jianshu.com/p/c1656748849f mimeType類型

        val mimeType = "application/octet-stream"
       // val mimeType = "text/comma-separated-values"
        //        String mimeType = "image/jpeg";
        val packageManager = this@MainActivity.packageManager
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = mimeType
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.size > 0) {
            val picker = Intent(Intent.ACTION_GET_CONTENT)
            picker.type = mimeType
            picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            //            picker.addCategory(Intent.CATEGORY_OPENABLE);
            picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            val destIntent = Intent.createChooser(picker, "選取csv檔案")
            startActivityForResult(destIntent, PICK_CSV_FROM_GALLERY_REQUEST_CODE)

        } else {
            Log.d("error", "無可用的Activity")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            400 -> if (resultCode == Activity.RESULT_OK) {
                handleSignInIntent(data)
            }

            PICK_CSV_FROM_GALLERY_REQUEST_CODE -> {
                var selectedCsv: Uri? = null
                var clipData: ClipData? = null
                if (resultCode == Activity.RESULT_OK && data != null) {
                    selectedCsv = data.data
                    if (clipData != null) {
                        catchFileInApp(selectedCsv, clipData)
                    } else if (Build.VERSION.SDK_INT >= 16 && clipData == null) {
                        clipData = data.clipData
                        catchFileInApp(selectedCsv, clipData)
                    }
                }
            }
            else -> {
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSignInIntent(data: Intent?) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnSuccessListener { googleSignInAccount ->
                val credential = GoogleAccountCredential
                    .usingOAuth2(this@MainActivity, setOf(DriveScopes.DRIVE_FILE))

                credential.selectedAccount = googleSignInAccount.account

                val googleDriveService = Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    GsonFactory(),
                    credential
                )
                    .setApplicationName("AppName")
                    .build()

                driveServiceHelper =
                    DriveServiceHelper(googleDriveService)

            }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    private fun catchFileInApp(selectedCsv: Uri?, clipData: ClipData?) {
        if (selectedCsv != null) {
            val path = FileUtil.getFileAbsolutePath(this, selectedCsv)
            //            Log.d("path1",""+path);
            //            filePath = path;
            path?.let { filepa.add(it) }

            val name = FileUtil.fileName(path)
            fileName.add(name)
            Log.d("asd",name)




            //設定檔案名稱
        } else if (clipData != null) {
            val count = clipData.itemCount
            if (count > 0) {
                val uris = arrayOfNulls<Uri>(count)
                val paths = arrayOfNulls<String>(count)
                val names = arrayOfNulls<String>(count)
                for (i in 0 until count) {
                    uris[i] = clipData.getItemAt(i).uri
                    paths[i] = FileUtil.getFileAbsolutePath(this, uris[i])
                    names[i] = FileUtil.fileName(paths[i])
                    Log.d("paee", paths[i]?.toString()!!)

                }

            }
        }
    }

}
