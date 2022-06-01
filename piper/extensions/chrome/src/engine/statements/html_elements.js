import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let els = []
        for (let el of statement.pointer) {
            let xpath
            if (el["xpath"]) {
                xpath = el["xpath"]
            } else {
                xpath = "null"
            }
            els.push({
                selector: el.selector,
                elIndex: el.elIndex,
                attrs: el.attrs
            })

            let tab = await this.browser.getCurrentTab()
            if (el.isFindSimilar) {
                await chrome.scripting.executeScript(
                    {
                        target: { tabId: tab.id },
                        files: ["element_selector.js"],
                        world: "MAIN"
                    }
                )
                let sims = await chrome.scripting.executeScript({
                    target: { tabId: tab.id },
                    func: (selector, elIndex, xpath) => {
                        let currentEl = document.querySelectorAll(selector)[elIndex]
                        if (currentEl === undefined) {
                            currentEl = window.elementFinder.getElementByXpath(xpath)
                        }
                        if (currentEl) {
                            currentEl.style.boxShadow = "0 0 0 2px red"
                            let tree = window.elementFinder.findTree(currentEl)
                            return window.elementFinder.findSimilar(currentEl, tree)
                        }
                        return []
                    },
                    args: [el.selector, el.elIndex, xpath],
                    world: "MAIN"
                })

                for (let sim of sims[0].result) {
                    els.push({
                        selector: sim.selector,
                        elIndex: sim.elIndex,
                        attrs: sim.attrs
                    })
                }
            }
        }

        return els
    } catch (e) {
        onError(e)
        return []
    }
}
