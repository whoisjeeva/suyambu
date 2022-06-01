import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let defaultValue = await this.executeStatement(stringify(pointer.value))
        let message = await this.executeStatement(stringify(pointer.message))

        let tab = await this.browser.getCurrentTab()

        let value = await chrome.scripting.executeScript({
            target: { tabId: tab.id },
            func: (message, defaultValue) => {
                return prompt(message, defaultValue)
            },
            args: [message, defaultValue],
            world: "MAIN"
        })
        return value[0].result
    } catch (e) {
        onError(e)
    }
    return null
}
