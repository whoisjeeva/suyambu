import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let els = await this.executeStatement(stringify(statement.pointer))
        if (!(els instanceof Array)) {
            els = [els]
        }

        let tab = await this.browser.getCurrentTab()
        let out = false
        for (let el of els) {
            let xpath = el.attrs.xpath || "null"
            let isExist = await chrome.scripting.executeScript({
                target: { tabId: tab.id },
                func: (xpath, el) => {
                    let foundEl = document.querySelectorAll(el.selector)[el.elIndex]
                    if (!foundEl) {
                        el = document.evaluate(xpath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue
                    }
                    return (el !== undefined && el !== null)
                },
                args: [xpath, el],
                world: "MAIN"
            })
            out = isExist[0].result
            if (!out) break
        }
        return out
    } catch (e) {
        onError(e)
        return false
    }
}
