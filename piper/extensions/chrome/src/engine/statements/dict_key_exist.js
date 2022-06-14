import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let dict = await this.executeStatement(stringify(pointer.dict))
        let key = await this.executeStatement(stringify(pointer.key))

        return Object.keys(dict).includes(key)
    } catch (e) {
        onError(e)
    }

    return null
}
