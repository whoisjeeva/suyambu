import { stringify, sleep } from "../util"



async function waitForElement(tabId, el) {
    let foundEl = await chrome.scripting.executeScript({
        target: { tabId: tabId },
        func: el => {
            let foundEl = document.querySelectorAll(el.selector)[el.elIndex]
            return foundEl
        },
        args: [el],
        world: "MAIN"
    })
    if (!foundEl[0].result) {
        return waitForElement.call(this, tabId, el)
    }
    return true
}


export default async function(statement, onError) {
    try {
        let obj = await this.executeStatement(stringify(statement.pointer))
        console.log(obj)
        let els
        if (obj instanceof Array) {
            els = obj
        } else {
            els = [obj]
        }

        let tab = await this.browser.getCurrentTab()
        for (let el of els) {
            await waitForElement.call(this, tab.id, el)
        }
    } catch (e) {
        onError(e)
    }
}
