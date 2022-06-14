import { stringify } from "../util"


function isJson(text) {
    try {
        console.log(JSON.parse(text))
        return true
    } catch (e) {
        return false
    }
}


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let input = stringify(await this.executeStatement(pointer.input))
        let op = pointer.op
        
        if (op === "URL") {
            await chrome.downloads.download({ url: input })
        } else {
            let blobUrl
            if (isJson(input)) {
                blobUrl = 'data:application/json,' + encodeURI(input)
            } else {
                blobUrl = 'data:text/plain,' + encodeURI(input)
            }
            await chrome.downloads.download({ url: blobUrl })
        }
    } catch (e) {
        onError(e)
    }
}
