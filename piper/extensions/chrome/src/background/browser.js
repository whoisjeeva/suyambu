import { sleep } from "../engine/util"

class Browser {
    constructor(options) {
        this.options = options
        this.windowId = null
        this.window = null
        this.isWindowCreated = false
    }

    async init(useCurrent) {
        if (useCurrent === undefined || useCurrent === null) {
            useCurrent = false
        }
        this.window = await chrome.windows.getCurrent()
        if (useCurrent) {
            this.options.useCurrent = true
        }
        
        this.windowId = this.window.id
        return this
    }

    async createNewWindow() {
        if (!this.options.useCurrent && !this.isWindowCreated) {
            this.window = await chrome.windows.create(this.options)
            this.windowId = this.window.id
            this.isWindowCreated = true
        }
    }

    onClose(callback) {
        chrome.windows.onRemoved.addListener(function(windowId) {
            callback(windowId)
            chrome.windows.onRemoved.removeListener(this)
        })
    }

    close() {
        return chrome.windows.remove(this.windowId)
    }

    async loadingWait() {
        let tab = await this.getCurrentTab()
        while (tab.status === 'loading') {
            await sleep(100)
            tab = await this.getCurrentTab()
        }
    }

    async allTabs() {
        let queryOptions = { windowId: this.windowId }
        return await chrome.tabs.query(queryOptions)
    }

    async getCurrentTab() {
        let [tab] = await chrome.tabs.query({ active: true, windowId: this.windowId })
        return tab
    }

    async loadUrl(url) {
        let tab = await this.getCurrentTab()
        await chrome.tabs.update(tab.id, { url: url })
        // await sleep(500)
    }

    async switchTab(index) {
        let tabs = await this.allTabs(this.windowId)
        return await chrome.tabs.update(tabs[index].id, { active: true })
    }

    async closeTab(index) {
        let tabs = await this.allTabs(this.windowId)
        return await chrome.tabs.remove(tabs[index].id, { windowId: this.windowId })
    }

    async newTab(url) {
        if (url === undefined || url === null) {
            await chrome.tabs.create({ windowId: this.windowId })
        } else {
            await chrome.tabs.create({ url: url, windowId: this.windowId })
        }
    }

    async executeScript(func, args) {
        let tab = await this.getCurrentTab()
        return await chrome.scripting.executeScript(
            {
                target: { tabId: tab.id },
                func: func,
                args: args,
                world: "MAIN"
            }
        )
    }

    
}


export { Browser }
