import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let list = await this.executeStatement(stringify(statement.pointer))
        
        let tab = await this.browser.getCurrentTab()
        let index = await chrome.scripting.executeScript({
            target: { tabId: tab.id },
            func: list => {
                let str = ""
                for (let i = 0; i < list.length; i++) {
                    str += (i + 1) + ". " + list[i]
                    if (i < list.length - 1) {
                        str += "\n"
                    }
                }
                let index = prompt("Enter the index of the item to select:\n" + str, "1")
                return parseInt(index)
            },
            args: [list],
            world: "MAIN"
        })
        return index[0].result
    } catch (e) {
        onError(e)
    }
}
