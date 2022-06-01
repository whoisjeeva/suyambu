import { $ } from "../dom"

class HomePage {
    constructor(selector) {
        this.root = $(selector).css({ width: "250px" })

        chrome.runtime.onMessage.addListener(async (message, sender, sendResponse) => {
            sendResponse({ message: "OK" })
            console.log("Got ur message")
        })

    }

    scriptItem(title, fileName) {
        let el = $(`<div class="script-item">
            <div class="script-item-title">${title}</div>
            <div class="script-item-file-name">${fileName}</div>
        </div>`)
        return el
    }

    toolbar(title) {
        let el = $(`<div class="toolbar">
            <div class="toolbar-title">${title}</div>

        </div>`)
    }

    render() {
        // chrome.runtime.sendMessage({ event: "start", code: "[]" })

        let scriptItemContainer = $(`<div class="script-item-container"></div>`)
        scriptItemContainer.append(this.scriptItem("Hello", "hello.piper"))
        this.root.append(scriptItemContainer)
    }
}


export default HomePage
