import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let source = ""
        let tab = await this.browser.getCurrentTab()
        let tmp = await chrome.scripting.executeScript({
            target: { tabId: tab.id },
            func: () => {
                return document.documentElement.outerHTML
            },
            world: "MAIN"
        })
        source = tmp[0].result
        return source
    } catch (e) {
        onError(e)
        return ""
    }
}
