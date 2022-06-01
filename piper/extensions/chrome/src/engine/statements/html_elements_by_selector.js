import { stringify, sleep } from "../util"



async function waitForElement(tabId, selector, tries = 0) {
    if (this.isTerminated) return false

    let els = await chrome.scripting.executeScript({
        target: { tabId: tabId },
        func: (selector) => {
            return document.querySelectorAll(selector)
        },
        args: [selector],
        world: "MAIN"
    })
    if (tries > 10 && els.length === 0) {
        return false
    }
    if (els.length === 0) {
        return waitForElement.call(this, tabId, selector, tries + 1)
    }
    return true
}


export default async function(statement, onError) {
    try {
        let tab = await this.browser.getCurrentTab()
        let selector = statement.pointer
        if (selector.includes('"')) {
            onError(new Error("Double quotes not allowed in selector"))
            return []
        }
        let isFound = await waitForElement.call(this, tab.id, selector)
        if(!isFound) {
            onError(new Error("HTMLElement not found"))
            return []
        }

        let els = await chrome.scripting.executeScript(
            {
                target: { tabId: tab.id },
                func: selector => {
                    let out = []
                    let els = document.querySelectorAll(selector)
                    for (let i = 0; i < els.length; i++) {
                        out.push({ selector: `body ${selector}`, elIndex: i, attrs: [] })
                    }
                    return out
                },
                args: [selector],
                world: "MAIN"
            }
        )

        return els[0].result
    } catch (e) {
        onError(e)
        return []
    }
}
