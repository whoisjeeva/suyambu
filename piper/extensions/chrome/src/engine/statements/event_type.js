import { stringify, sleep } from "../util"


export default async function(statement, onError) {
    try {
        let value = await this.executeStatement(stringify(statement.pointer))
        let tab = await this.browser.getCurrentTab()

        await chrome.debugger.attach({ tabId: tab.id }, "1.2")
        for (let c of value) {
            await chrome.debugger.sendCommand({ tabId: tab.id }, "Input.dispatchKeyEvent", {
                type: "char",
                text: c,
                modifiers: 0
            })
        }
        await chrome.debugger.detach({ tabId: tab.id })
    } catch (e) {
        console.log(e)
        onError(e)
    }
}
