import { stringify } from "../util"


async function waitForElement(tabId, el, tries = 0) {
    if (this.isTerminated) return false

    let els = await chrome.scripting.executeScript({
        target: { tabId: tabId },
        func: el => {
            return document.querySelectorAll(el.selector)[el.elIndex]
        },
        args: [el],
        world: "MAIN"
    })
    els = els[0].result
    if (tries > 10 && !els) {
        return false
    }
    if (!els) {
        return waitForElement.call(this, tabId, el, tries + 1)
    }
    return true
}

export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let els = await this.executeStatement(stringify(pointer.elements))
        if (!(els instanceof Array)) {
            els = [els]
        }

        let op = pointer.op
        let tab = await this.browser.getCurrentTab()
        
        for (let el of els) {
            let isFound = true
            if (op === "WAIT") {
                isFound = waitForElement.call(this, tab.id, el)
            }
            console.log("element: ", el)
            let xpath = el.attrs.xpath || "null"

            if (isFound) {
                await chrome.scripting.executeScript({
                    target: { tabId: tab.id },
                    func: (xpath, el) => {
                        let foundEl = document.querySelectorAll(el.selector)[el.elIndex]
                        if (!foundEl) {
                            foundEl = document.evaluate(xpath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue
                        }
                        if (foundEl.tagName.toLowerCase() === "svg") {
                            foundEl.parentElement.style.boxShadow = "0 0 0 2px yellow"
                            foundEl.parentElement.focus()
                            foundEl.parentElement.click()
                        } else {
                            foundEl.style.boxShadow = "0 0 0 2px yellow"
                            foundEl.focus()
                            foundEl.click()
                        }
                    },
                    args: [xpath, el],
                    world: "MAIN"
                })
            } else {
                onError(new Error("HTMLElement not found"))
            }
        }
    } catch (e) {
        console.log(e)
        onError(e)
    }
}
