import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = stringify(statement.pointer)
        let url = stringify(await this.executeStatement(pointer))
        console.log(url)
        await chrome.downloads.download({ url: url })
    } catch (e) {
        onError(e)
    }
}
