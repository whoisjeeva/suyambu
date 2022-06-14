import { stringify, sleep } from "../util"



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

        if (els.length === 0) {
            throw new Error("No elements found")
        }

        let attr = pointer.attr.toLowerCase()
        let op = pointer.op
        let attrValues = []

        let tab = await this.browser.getCurrentTab()
        switch(op) {
            case "FIRST":
                await waitForElement.call(this, tab.id, els[0])
                els = [els[0]]
                break
            case "LAST":
                await waitForElement.call(this, tab.id, els[els.length - 1])
                els = [els[els.length - 1]]
                break
            default:
                for (let el of els) {
                    await waitForElement.call(this, tab.id, el)
                }
        }

        for (let el of els) {
            let xpath = el.attrs.xpath || "null"
            let attrValue = await chrome.scripting.executeScript({
                target: { tabId: tab.id },
                func: (attr, xpath, el) => {
                    let foundEl = document.querySelectorAll(el.selector)[el.elIndex]
                    if (!foundEl) {
                        foundEl = document.evaluate(xpath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue
                    }
                    if (!foundEl) return null
                    foundEl.style.boxShadow = "0 0 0 2px yellow"
                    if (attr === "src") {
                        return foundEl.currentSrc
                    } else if (attr === "action") {
                        return foundEl.action
                    } else if (attr === "href") {
                        return foundEl.href
                    }
                    return foundEl.getAttribute(attr)
                },
                args: [attr, xpath, el],
                world: "MAIN"
            })
            let value = attrValue[0].result
            if (value) {                
                attrValues.push(attrValue[0].result)
            }
        }

        switch(op) {
            case "FIRST":
                return attrValues[0]
            case "LAST":
                return attrValues[attrValues.length - 1]
            default:
                return attrValues
        }
    } catch (e) {
        onError(e)
        return null
    }
}
