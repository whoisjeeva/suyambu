import { $ } from "../dom"

class ElementSelectorPage {
    constructor(selector) {
        this.root = $(selector).css({ width: "250px" })
    }

    render() {
        let container = $(`<div class="element-selector-container"></div>`)

        let recordButton = $(`<button class="button">Start recording</button>`)
        let nearByButton = $(`<button class="button">Switch to nearby element</button>`)
        let parentButton = $(`<button class="button">Switch to parent element</button>`)
        let similarButton = $(`<button class="button">Find similar elements</button>`)
        let confirmButton = $(`<button class="button">Confirm</button>`)
        let recordMessage = $(`<div class="record-message">After you click on the 'Start recording' button, right click to select or deselect an element.</div>`).css({ textAlign: "center" })
        
        container.append(recordButton)
        container.append(nearByButton)
        container.append(parentButton)
        container.append(similarButton)
        container.append(confirmButton)
        container.append(recordMessage)

        chrome.storage.sync.get("is_record_injected", data => {
            if (data.is_record_injected) {
                recordButton.remove()
                recordMessage.remove()
            } else {
                nearByButton.remove()
                parentButton.remove()
                similarButton.remove()
                confirmButton.remove()
            }
        })


        recordButton.on("click", async e => {
            let tabs = await chrome.tabs.query({ active: true, currentWindow: true })
            await chrome.scripting.executeScript(
                {
                    target: { tabId: tabs[0].id },
                    files: ["element_selector.js"],
                    world: "MAIN"
                }
            )
            await chrome.scripting.executeScript({
                target: { tabId: tabs[0].id },
                func: () => {
                    window.elementFinder.init()
                },
                world: "MAIN",
            })
            window.close()
            chrome.storage.sync.set({ is_record_injected: true })
        })


        nearByButton.on("click", async e => {
            let tabs = await chrome.tabs.query({ active: true, currentWindow: true })
            await chrome.scripting.executeScript({
                target: { tabId: tabs[0].id },
                func: () => {
                    let tag = prompt("Enter a tag name")
                    return window.elementFinder.findNearestElement(tag)
                },
                world: "MAIN"
            })
            window.close()
        })


        parentButton.on("click", async e => {
            let tabs = await chrome.tabs.query({ active: true, currentWindow: true })
            await chrome.scripting.executeScript({
                target: { tabId: tabs[0].id },
                func: () => {
                    return window.elementFinder.switchParenElement()
                },
                world: "MAIN",
            })
            window.close()
        })


        similarButton.on("click", async e => {
            let tabs = await chrome.tabs.query({ active: true, currentWindow: true })
            await chrome.scripting.executeScript({
                target: { tabId: tabs[0].id },
                func: () => {
                    return window.elementFinder.addSimilarElements()
                },
                world: "MAIN",
            })
            window.close()
        })


        confirmButton.on("click", e => {
            chrome.runtime.sendMessage({ event: "editor_tab" }, async message => {
                let tabs = await chrome.tabs.query({ active: true, currentWindow: true })
                let elements = await chrome.scripting.executeScript({
                    target: { tabId: tabs[0].id },
                    func: () => {
                        return JSON.stringify(window.elementFinder.elements)
                    },
                    world: "MAIN",
                })

                await chrome.scripting.executeScript({
                    target: { tabId: message.tabId },
                    func: elements => {
                        Gumify.updateLastBlock(btoa(elements))
                        document.querySelector(".overlay").style.display = "none"
                        document.querySelector(".element-selector-dialog").style.display = "none"
                        window.saveWorkspace()
                    },
                    args: [elements[0].result],
                    world: "MAIN"
                })
                await chrome.tabs.reload(tabs[0].id)
                await chrome.tabs.update(message.tabId, { active: true })

                // chrome.tabs.sendMessage(message.tabId, { event: "confirm", elements: elements[0].result })
                window.close()
                chrome.storage.sync.set({ is_record_injected: false })
            })
        })

        

        this.root.append(container)
    }
}


export default ElementSelectorPage
