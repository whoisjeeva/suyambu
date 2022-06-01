import { stringify, sleep } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let tab = await this.browser.getCurrentTab()
        if (pointer === "UP") {
            await chrome.scripting.executeScript({
                target: { tabId: tab.id },
                func: () => {
                    window.scrollTo({ top: 0, behavior: 'smooth' })
                },
                world: "MAIN"
            })
        } else if (pointer === "DOWN") {
            await chrome.scripting.executeScript({
                target: { tabId: tab.id },
                func: () => {
                    window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' })
                },
                world: "MAIN"
            })
        }
    } catch (e) {
        onError(e)
    }
}
