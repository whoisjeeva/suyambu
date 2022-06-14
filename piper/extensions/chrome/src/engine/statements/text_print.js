import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let value = await this.executeStatement(stringify(statement.pointer))
        if (value === Infinity) value = "Infinity"
        let tab = await this.browser.getCurrentTab()
        await chrome.scripting.executeScript({
            target: { tabId: tab.id },
            func: value => {
                return alert(value)
            },
            args: [stringify(value)],
            world: "MAIN"
        })
    } catch (e) {
        onError(e)
    }
}
