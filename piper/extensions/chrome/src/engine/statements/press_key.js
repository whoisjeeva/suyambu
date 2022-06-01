import { stringify, sleep } from "../util"


export default async function(statement, onError) {
    try {
        let tab = await this.browser.getCurrentTab()
        await chrome.debugger.attach({ tabId: tab.id }, "1.2")
        switch(statement.pointer) {
            case "ENTER":
                await chrome.debugger.sendCommand({ tabId: tab.id }, "Input.dispatchKeyEvent", {
                    type: "char",
                    text: String.fromCharCode(13),
                    modifiers: 0
                })
                break
            case "SHIFT":
                await chrome.debugger.sendCommand({ tabId: tab.id }, 'Input.dispatchKeyEvent', { 
                    type: "char",
                    text: String.fromCharCode(16), 
                    modifiers: 0
                })
                break
            case "CTRL":
                await chrome.debugger.sendCommand({ tabId: tab.id }, 'Input.dispatchKeyEvent', { 
                    type: "char",
                    text: String.fromCharCode(17), 
                    modifiers: 0
                })
                break
            case "CAPS_LOCK":
                await chrome.debugger.sendCommand({ tabId: tab.id }, 'Input.dispatchKeyEvent', { 
                    type: "char",
                    text: String.fromCharCode(20), 
                    modifiers: 0
                })
                break
            case "TAB":
                await chrome.debugger.sendCommand({ tabId: tab.id }, "Input.dispatchKeyEvent", {
                    type: "char",
                    text: String.fromCharCode(9),
                    modifiers: 0
                })
                break
            case "ESC":
                await chrome.debugger.sendCommand({ tabId: tab.id }, 'Input.dispatchKeyEvent', { 
                    type: "char",
                    text: String.fromCharCode(27), 
                    modifiers: 0
                })
                break
        }
        await chrome.debugger.detach({ tabId: tab.id })
        await sleep(1000)
    } catch (e) {
        console.log(e)
        onError(e)
    }
}
