package core.data.logger

import android.util.Log
import io.github.dmitrytsyvtsyn.algosortinganimations.core.logger.Logger

class AndroidLogcatLogger : Logger {

    override fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

}