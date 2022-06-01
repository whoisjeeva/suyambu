async function allTabs(windowId) {
    let queryOptions = { windowId: windowId }
    return await chrome.tabs.query(queryOptions)
}
const sleep = ms => new Promise(r => setTimeout(r, ms))

chrome.runtime.onMessage.addListener(
    async function(request, sender, sendResponse) {
        // sender.tab ? "from a content script:" + sender.tab.url : "from the extension"
        // sendResponse({farewell: "goodbye"})

        if (request.event == "start") {
            sendResponse({farewell: "I will take it from here."})
            let window = await chrome.windows.create({ incognito: false })
            console.log("WINDOW: ", window)
            await sleep(1000)
            await chrome.tabs.create({ url: "http://facebook.com", windowId: window.id })
            await sleep(2000)
            let tabs = await allTabs(window.id)
            console.log("TABS: ", tabs)
            chrome.tabs.update(tabs[0].id, {url: 'http://www.google.com'})
            await sleep(2000)
            chrome.tabs.update(tabs[0].id, {active: true})
            await sleep(2000)

            let result = await chrome.scripting.executeScript(
                {
                    target: { tabId: tabs[0].id },
                    func: e => {
                        alert("Hello from the background script!")
                        return "Hello from the background script!"
                    }
                }
            )
            console.log("RESULT: ", result)

            tabs = await allTabs(window.id)
            for (let tab of tabs) {
                await chrome.tabs.remove(tab.id)
            }
        }
    }
)