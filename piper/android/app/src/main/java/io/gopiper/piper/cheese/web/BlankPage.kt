package io.gopiper.piper.cheese.web

import android.webkit.WebResourceResponse
import java.io.ByteArrayInputStream

object BlankPage {
    private val data: ByteArrayInputStream
        get() = ByteArrayInputStream("""<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="viewport" content="width=device-width, initial-scale=1.0"><title>about:blank</title><style>body{background-color: #fff;color: #31302e;}.icon{width: 40px;display: inline-block;}.wrap{text-align: center;position: absolute;top: 50%;left: 50%;transform: translate(-50%, -50%);}a{color: #F2BE4B;text-decoration: none;display: inline-block;padding: 0px 20px;border-radius: 4px;background-color: rgba(242, 190, 75, 0.1);}</style></head><body><div class="wrap"><div class="icon"><svg class="svg-icon" viewBox="0 0 20 20"><path fill="#31302e" d="M3.183,9.381H0.704v1.239h2.479V9.381z M2.989,16.135l0.876,0.877l1.752-1.754l-0.876-0.875L2.989,16.135zM17.012,3.866l-0.877-0.876l-1.752,1.752l0.875,0.876L17.012,3.866z M10.62,0.705H9.38v2.479h1.239V0.705z M5.618,4.742L3.865,2.989L2.989,3.866l1.753,1.752L5.618,4.742z M14.383,15.258l1.752,1.754l0.877-0.877l-1.754-1.752L14.383,15.258zM9.38,19.297h1.239v-2.48H9.38V19.297z M16.816,9.381v1.239h2.479V9.381H16.816z M10,5.042c-2.738,0-4.958,2.22-4.958,4.958c0,2.738,2.22,4.959,4.958,4.959c2.738,0,4.958-2.221,4.958-4.959C14.958,7.263,12.738,5.042,10,5.042z M10,13.727c-2.058,0-3.726-1.668-3.726-3.727c0-2.058,1.668-3.726,3.726-3.726c2.059,0,3.727,1.668,3.727,3.726C13.727,12.059,12.059,13.727,10,13.727z"></path></svg></div><p><pre>Blank Page</pre></p><br><br></div></body></html>""".toByteArray())
    val response: WebResourceResponse
        get() = WebResourceResponse("text/html", "utf-8", data)
}