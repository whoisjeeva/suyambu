package io.gopiper.piper.engine

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebStorage
import kotlinx.coroutines.*
import io.gopiper.piper.cheese.Browser
import io.gopiper.piper.cheese.js
import io.gopiper.piper.engine.model.Func
import io.gopiper.piper.engine.model.HTMLElement
import io.gopiper.piper.engine.statements.*
import io.gopiper.piper.model.Pipe
import io.gopiper.piper.util.C
import io.pipend.open.hiper.Hiper
import me.gumify.hiper.util.WeeDB
import me.gumify.hiper.util.onUiThread
import me.gumify.hiper.util.toast
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception


// 871 6340 3444
// 245256

class Piper(
    val context: Context,
    val browser: Browser,
    val onScriptComplete: () -> Unit,
    val onRegisterLog: (scriptId: String, log: String) -> Unit,
    val isHeadless: Boolean = false,
    val scriptId: String
) {
    companion object {
        const val VERSION = 1
    }

    val hiper = Hiper.getInstance()
    val wee = WeeDB(context.applicationContext)


    var loopFlow: String? = null
    var currentFunctionName: String? = null
    var isFunctionReturn: Boolean? = null
    var functionReturnValue: Any? = null

    val VARIABLE_STACK = HashMap<String, Any?>()
    val FUNCTION_STACK = ArrayList<Func>()
    var isTerminated = false
    private val isDoNotWait = wee.getBoolean(C.SETTING_DO_NOT_WAIT_FOR_LOADING)
    private val isSkipError = wee.getBoolean(C.SETTING_SKIP_ERROR)
    var canShowErrorAlert = true
    var lastClickedElement: HTMLElement? = null

    fun loadUrl(url: String) {
        browser.currentTab.value?.loadUrl(url)
    }

    suspend fun downloadFromUrl(url: String) {
        var isDone = false
        browser.currentTab.value?.webView?.js("""
            var el = document.createElement("a");
            el.href = "$url";
            el.setAttribute("download", true);
            el.click();
        """.trimIndent()) {
            isDone = true
        }
        while (!isDone) {
            if (isTerminated) break
            delay(100)
        }

//        val response = hiper.head(url)
//        val fileName = URLUtil.guessFileName(url, response.headers["content-disposition"], response.headers["content-type"])
//        val downloadmanager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
//        val uri: Uri = Uri.parse(url)
//        onUiThread {
//            val request = DownloadManager.Request(uri)
//            request.setMimeType(response.headers["content-type"])
//            val cookies = CookieManager.getInstance().getCookie(url)
//            request.addRequestHeader("cookie", cookies)
//            request.addRequestHeader("User-Agent", browser.currentTab.value?.webView?.settings?.userAgentString)
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
//            downloadmanager?.enqueue(request)
//        }
    }

    suspend fun findFunction(name: String?): Func? {
        if (name == null) return null
        for (f in FUNCTION_STACK) {
            if (f.name == name) return f
        }
        return null
    }

    fun showError(statement: JSONObject, error: Exception) {
        if (!isSkipError) {
            isTerminated = true
            if (!isHeadless && canShowErrorAlert) {
                onUiThread {
                    val dialog = AlertDialog.Builder(context)
                    dialog.setTitle("Error")
                    dialog.setMessage(
                        """
                ${statement.getString("cmd").uppercase()}
                
                Error: $error
            """.trimIndent()
                    )
                    dialog.setPositiveButton("OK") { _, _ -> }
                    dialog.show()
                }
            }
        }
        val log = "${statement["cmd"].toString().uppercase()}\n$error"
        onRegisterLog(scriptId, log)
    }

    suspend fun init(code: String) {
        if (wee.getBoolean(C.SETTING_CLEAR_BROWSER_BEFORE_EX)) {
            WebStorage.getInstance().deleteAllData()
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        }
        val isSuccess = initFunctions(code)
        delay(1000)
        if (isSuccess) {
            execute(code)
        }
        onScriptComplete()
    }

    private suspend fun initFunctions(code: String): Boolean {
        val statements: JSONArray
        try {
            statements = JSONArray(code)
        } catch (e: Exception) {
            Log.d("hello", "Error: $e")
            onUiThread {
                val o = JSONObject()
                o.put("cmd", "INIT_FUNCTIONS")
                showError(o, Exception("SyntaxError: There might be some invalid blocks"))
            }
            return false
        }
        for (i in 0 until statements.length()) {
            val statement = statements.getJSONObject(i)
            val cmd = statement.getString("cmd")
            if (cmd == "procedures_defnoreturn") {
                executeProceduresDefnoreturn(statement) { showError(statement, it) }
            } else if (cmd == "procedures_defreturn") {
                executeProceduresDefreturn(statement) { showError(statement, it) }
            }
        }
        return true
    }

    suspend fun execute(sourceCode: String) {
        val statements = JSONArray(sourceCode)
        for (i in 0 until statements.length()) {
            if (isTerminated) break

            if (loopFlow == "BREAK") {
                break
            } else if (loopFlow == "CONTINUE") {
                continue
            }

            if (isFunctionReturn != null) break

            val statement = statements.getJSONObject(i)
            executeStatement(statement)
        }
    }

    suspend fun executeStatement(statement: JSONObject): Any? {
        if (!isDoNotWait) {
            while (browser.currentTab.value?.isLoading == true) delay(100)
        }
        if (!statement.has("cmd")) {
            return statement
        }

        return when (statement.getString("cmd")) {
            // Browser
            "go_to_website" -> {
                lastClickedElement = null
                val ret = executeGoToWebsite(statement) { showError(statement, it) }
                if (!isDoNotWait) {
                    delay(1000)
                }
                return ret
            }
            "download_from_url" -> executeDownloadFromUrl(statement) { showError(statement, it) }
            "set_user_agent" -> executeSetUserAgent(statement) { showError(statement, it) }
            "custom_user_agent" -> executeCustomUserAgent(statement) { showError(statement, it) }
            "execute_javascript" -> executeExecuteJavascript(statement) { showError(statement, it) }
            "new_tab" -> executeNewTab(statement) { showError(statement, it) }
            "switch_tab" -> executeSwitchTab(statement) { showError(statement, it) }
            "close_tab" -> executeCloseTab(statement) { showError(statement, it) }

            // HTML
            "html_elements" -> executeHtmlElements(statement) { showError(statement, it) }
            "html_elements_by_selector" -> executeHtmlElementsBySelector(statement) { showError(statement, it) }
            "wait_for_elements" -> executeWaitForElements(statement) { showError(statement, it) }
            "attribute_of" -> executeAttributeOf(statement) { showError(statement, it) }
            "text_of" -> executeTextOf(statement) { showError(statement, it) }
            "tag_name_of" -> executeTagNameOf(statement) { showError(statement, it) }
            "is_html_element_exist" -> executeIsHtmlElementExist(statement) { showError(statement, it) }
            "page_source" -> executePageSource(statement) { showError(statement, it) }

            // Events
            "click" -> executeClick(statement) { showError(statement, it) }
            "type" -> executeType(statement) { showError(statement, it) }
            "press_key" -> executePressKey(statement) { showError(statement, it) }
            "wait_for" -> executeWaitFor(statement) { showError(statement, it) }
            "scroll" -> executeScroll(statement) { showError(statement, it) }

            // Logic
            "controls_if" -> executeControlsIf(statement) { showError(statement, it) }
            "logic_compare" -> executeLogicCompare(statement) { showError(statement, it) }
            "logic_operation" -> executeLogicOperation(statement) { showError(statement, it) }
            "logic_negate" -> executeLogicNegate(statement) { showError(statement, it) }
            "logic_boolean" -> executeLogicBoolean(statement) { showError(statement, it) }
            "logic_null" -> null
            "logic_ternary" -> executeLogicTernary(statement) { showError(statement, it) }

            // Loops
            "controls_repeat_ext" -> executeControlsRepeatExt(statement) { showError(statement, it) }
            "controls_whileUntil" -> executeControlsWhileUntil(statement) { showError(statement, it) }
            "controls_for" -> executeControlsFor(statement) { showError(statement, it) }
            "controls_forEach" -> executeControlsForEach(statement) { showError(statement, it) }
            "controls_flow_statements" -> executeControlsFlowStatement(statement) { showError(statement, it) }

            // Math
            "math_number" -> executeMathNumber(statement) { showError(statement, it) }
            "math_arithmetic" -> executeMathArithmetic(statement) { showError(statement, it) }
            "math_single" -> executeMathSingle(statement) { showError(statement, it) }
            "math_trig" -> executeMathTrig(statement) { showError(statement, it) }
            "math_constant" -> executeMathConstant(statement) { showError(statement, it) }
            "math_number_property" -> executeMathNumberProperty(statement) { showError(statement, it) }
            "convert_text_to_number" -> executeConvertTextToNumber(statement) { showError(statement, it) }
            "math_round" -> executeMathRound(statement) { showError(statement, it) }
            "math_on_list" -> executeMathOnList(statement) { showError(statement, it) }
            "math_modulo" -> executeMathModulo(statement) { showError(statement, it) }
            "math_constrain" -> executeMathConstrain(statement) { showError(statement, it) }
            "math_random_int" -> executeMathRandomInt(statement) { showError(statement, it) }
            "math_random_float" -> executeMathRandomFloat(statement) { showError(statement, it) }

            // Text
            "text" -> executeText(statement) { showError(statement, it) }
            "text_join" -> executeTextJoin(statement) { showError(statement, it) }
            "text_append" -> executeTextAppend(statement) { showError(statement, it) }
            "text_length" -> executeTextLength(statement) { showError(statement, it) }
            "text_isEmpty" -> executeTextIsEmpty(statement) { showError(statement, it) }
            "convert_to_text" -> executeConvertToText(statement) { showError(statement, it) }
            "text_indexOf" -> executeTextIndexOf(statement) { showError(statement, it) }
            "text_charAt" -> executeTextCharAt(statement) { showError(statement, it) }
            "text_getSubstring" -> executeTextGetSubstring(statement) { showError(statement, it) }
            "text_changeCase" -> executeTextChangeCase(statement) { showError(statement, it) }
            "text_trim" -> executeTextTrim(statement) { showError(statement, it) }
            "text_print" -> executeTextPrint(statement) { showError(statement, it) }
            "prompt" -> executePrompt(statement) { showError(statement, it) }

            // Lists
            "lists_create_with" -> executeListsCreateWith(statement) { showError(statement, it) }
            "lists_chooser" -> executeListsChooser(statement) { showError(statement, it) }
            "lists_repeat" -> executeListsRepeat(statement) { showError(statement, it) }
            "lists_length" -> executeListsLength(statement) { showError(statement, it) }
            "lists_isEmpty" -> executeListsIsEmpty(statement) { showError(statement, it) }
            "lists_indexOf" -> executeListsIndexOf(statement) { showError(statement, it) }
            "lists_getIndex" -> executeListsGetIndex(statement) { showError(statement, it) }
            "lists_setIndex" -> executeListsSetIndex(statement) { showError(statement, it) }
            "lists_getSublist" -> executeListsGetSublist(statement) { showError(statement, it) }
            "lists_split" -> executeListsSplit(statement) { showError(statement, it) }
            "lists_regex" -> executeListsRegex(statement) { showError(statement, it) }
            "lists_sort" -> executeListsSort(statement) { showError(statement, it) }

            // Variables
            "variables_set" -> executeVariablesSet(statement) { showError(statement, it) }
            "variables_get" -> executeVariablesGet(statement) { showError(statement, it) }
            "math_change" -> executeMathChange(statement) { showError(statement, it) }

            // Functions
//            "procedures_defnoreturn" -> executeProceduresDefnoreturn(statement) { showError(statement, it) }
            "procedures_callnoreturn" -> executeProceduresCallnoreturn(statement) { showError(statement, it) }
            "procedures_ifreturn" -> executeProceduresIfreturn(statement) { showError(statement, it) }
//            "procedures_defreturn" -> executeProceduresDefreturn(statement) { showError(statement, it) }
            "procedures_callreturn" -> executeProceduresCallreturn(statement) { showError(statement, it) }

            // Utils
            "utils_tryCatch" -> executeUtilsTryCatch(statement) { showError(statement, it) }

            else -> null
        }
    }

    suspend fun executeStatement(statement: String): Any? {
        val obj: JSONObject
        try {
            obj = JSONObject(statement)
        } catch (e: Exception) {
            return statement
        }
        return executeStatement(obj)
    }
}