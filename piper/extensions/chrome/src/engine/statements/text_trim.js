import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let value = await this.executeStatement(stringify(pointer.value))
        
        switch(stringify(pointer.action)) {
            case "LEFT":
                return value.trimLeft()
            case "RIGHT":
                return value.trimRight()
            case "BOTH":
                return value.trim()
            default:
                return value
        }
    } catch (e) {
        onError(e)
    }

    return null
}
