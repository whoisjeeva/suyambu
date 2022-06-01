import { Browser } from "./browser"
import { sleep } from "../engine/util"
import { Piper } from "../engine/piper"



async function runScript(code) {
    // console.log(message.code)

    // let browser = await new Window({ incognito: true, state: "minimized", width: 320 }).init(false)
    let browser = await new Browser({ incognito: false, state: "normal" }).init(false)
    // await window.newTab()
    // await window.loadUrl("http://youtube.com")
    
    // await window.loadingWait()

    // let tabs = await window.allTabs()
    // await window.switchTab(0)
    // await window.loadUrl('http://www.google.com')

    // await window.loadingWait()

    // let result = await window.executeScript(e => {
    //     alert("Hello from the background script!")
    //     return "Hello from the background script!"
    // })

    // window.close()

    let piper = new Piper(browser)
    try {
        await piper.init(code)
    } catch(e) {
        console.log("Error:", e)
    }
    await chrome.declarativeNetRequest.updateEnabledRulesets({ disableRulesetIds: ["mobile_user_agent_rule", "desktop_user_agent_rule"] })
}

function switchToHome() {
    chrome.action.setIcon({ 
        path: {
            "16": "icons/16.png",
            "48": "icons/48.png",
            "128": "icons/128.png"
        } 
    })
    chrome.storage.sync.set({ page: "home" })
    chrome.storage.sync.set({ is_record_injected: false })
}


function switchToElementSelection() {
    chrome.action.setIcon({ path: "icons/record.png" })
    chrome.storage.sync.set({ page: "html_elements" })

    chrome.tabs.query({active: true, currentWindow: true}, tabs => {
        chrome.storage.sync.set({ editor_tab_id: tabs[0].id })
    })
}




chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    switch (message.event) {
        case "remove_html_elements":
            sendResponse({ message: "OK" })
            switchToHome()
            break
        case "editor_tab":
            chrome.storage.sync.get("editor_tab_id", data => {
                sendResponse({ message: "OK", tabId: data.editor_tab_id })
                switchToHome()
            })
            break
    }
    return true
})



chrome.runtime.onMessageExternal.addListener((message, sender, sendResponse) => {
    switch (message.event) {
        case "html_elements":
            switchToElementSelection()
            break
        case "remove_html_elements":
            switchToHome()
            break
        case "start":
            runScript(message.code)
            break
    }

    sendResponse({ message: "OK" })
    return true
})


