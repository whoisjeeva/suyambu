import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let key = await this.executeStatement(stringify(pointer.key))
        let dict = await this.executeStatement(stringify(pointer.dict))
        return dict[key]
    } catch (e) {
        onError(e)
    }
    return null
}
