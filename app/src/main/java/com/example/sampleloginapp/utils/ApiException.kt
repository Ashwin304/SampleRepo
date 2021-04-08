package com.example.sampleloginapp.utils

import android.os.Message
import java.io.IOException
import java.lang.Exception

class ApiException(message: String): IOException(message)
class NoInternetException(message: String): IOException(message)