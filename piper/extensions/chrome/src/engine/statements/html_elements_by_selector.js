import { xpathToCssSelectorInjector } from "../util"



async function waitForElement(tabId, args, tries = 0) {
    if (this.isTerminated) return false

    let els = await chrome.scripting.executeScript({
        target: { tabId: tabId },
        func: args => {
            document.querySelectorAll(atob(args.selector))
        },
        args: [args],
        world: "MAIN"
    })
    if (tries > 10 && els.length === 0) {
        return false
    }
    if (els.length === 0) {
        return waitForElement.call(this, tabId, args, tries + 1)
    }
    return true
}


export default async function(statement, onError) {
    try {
        let tab = await this.browser.getCurrentTab()
        let selector = statement.pointer
        // if (selector.includes('"')) {
        //     onError(new Error("Double quotes not allowed in selector"))
        //     return []
        // }

        let args = { selector: btoa(selector), isXpath: false }

        if (selector.includes("/")) {
            args.isXpath = true
            await chrome.scripting.executeScript({
                target: { tabId: tab.id },
                func: xpathToCssSelectorInjector,
                world: "MAIN"
            })

            let convertSelector = await chrome.scripting.executeScript({
                target: { tabId: tab.id },
                func: args => {
                    let selector = atob(args.selector)
                    return cssify(selector)
                },
                args: [args],
                world: "MAIN"
            })
            args.selector = btoa(convertSelector[0].result)
        }

        let isFound = await waitForElement.call(this, tab.id, args)
        if(!isFound) {
            onError(new Error("HTMLElement not found"))
            return []
        }

        let els = await chrome.scripting.executeScript(
            {
                target: { tabId: tab.id },
                func: args => {
                    let out = []
                    let selector = atob(args.selector)
                    let els = document.querySelectorAll(selector)
                    for (let i = 0; i < els.length; i++) {
                        if (args.isXpath) {
                            out.push({ selector: selector, elIndex: i, attrs: [] })
                        } else {
                            out.push({ selector: `body ${selector}`, elIndex: i, attrs: [] })
                        }
                    }
                    return out
                },
                args: [args],
                world: "MAIN"
            }
        )

        return els[0].result
    } catch (e) {
        onError(e)
        return []
    }
}
