package io.gopiper.piper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dagger.hilt.android.AndroidEntryPoint
import io.gopiper.piper.ui.theme.PiperTheme
import io.gopiper.piper.ui.theme.ThemeContent
import kotlinx.coroutines.launch
import me.gumify.hiper.util.WeeDB
import org.json.JSONArray
import javax.inject.Inject

@AndroidEntryPoint
class ListsChooserActivity : ComponentActivity() {
    @Inject
    lateinit var wee: WeeDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonArray = JSONArray(intent?.getStringExtra("jsonArray") ?: "[]")
        val key = intent?.getStringExtra("key").toString()
        val items = ArrayList<String>()
        for (i in 0 until jsonArray.length()) {
            items.add(jsonArray[i].toString())
        }

        wee.remove(key)

        setContent {
            ThemeContent {
                PiperTheme {
                    Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
                        val scope = rememberCoroutineScope()

                        Dialog(onDismissRequest = {}) {
                            Surface(color = MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(10.dp)) {
                                LazyColumn {
                                    item {
                                        Text(
                                            text = "Choose an option",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(20.dp)
                                        )
                                    }

                                    itemsIndexed(items) { i, item ->
                                        Text(
                                            text = item,
                                            modifier = Modifier
                                                .clickable {
                                                    scope.launch {
                                                        wee.put(key, i)
                                                    }
                                                    finish()
                                                }
                                                .fillMaxWidth()
                                                .padding(20.dp, 10.dp)
                                        )
                                    }

                                    item {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp), horizontalArrangement = Arrangement.End) {
                                            TextButton(onClick = { finish() }) {
                                                Text(
                                                    text = "Cancel",
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}