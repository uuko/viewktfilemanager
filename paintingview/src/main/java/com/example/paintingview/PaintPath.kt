package com.example.ktforfilemanager.paintview

import android.graphics.Path



data class PaintPath(var currentColor: Int=0, var emboss: Boolean=false, var blur: Boolean=false, var paintWidth: Float=0F, var mPath: Path)
