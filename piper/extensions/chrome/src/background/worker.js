import { Browser } from "./browser"
import { Piper } from "../engine/piper"


const CONFIG = {
    isElementSelection: false,
}


async function runScript(incomming) {
    // let browser = await new Window({ incognito: true, state: "minimized", width: 320 }).init(false)
    let browser = await new Browser({ incognito: incomming.incognito || false, state: incomming.state || "normal" }).init(false)
    let piper = new Piper(browser)
    try {
        await piper.init(incomming.code)
    } catch(e) {
        console.log("Error:", e)
    }
    await chrome.declarativeNetRequest.updateEnabledRulesets({ disableRulesetIds: ["mobile_user_agent_rule", "desktop_user_agent_rule"] })
}

function switchToHome() {
    CONFIG.isElementSelection = false
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
    CONFIG.isElementSelection = true
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
        case "open_piper":
            chrome.tabs.create({ active: true, url: "https://suyambu.net/piper" })
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
            runScript(message)
            break
        case "ping":
            console.log("Wake up!")
            break
    }

    sendResponse({ message: "OK" })
    // return true
})

