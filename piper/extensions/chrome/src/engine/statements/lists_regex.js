import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let text = await this.executeStatement(stringify(pointer.text))
        let regex = await this.executeStatement(stringify(pointer.regex))
        regex = new RegExp(regex, "g")
        return [...text.matchAll(regex)]
    } catch (e) {
        onError(e)
    }

    return null
}
