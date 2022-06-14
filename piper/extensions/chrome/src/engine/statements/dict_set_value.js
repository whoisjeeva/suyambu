import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let dict = await this.executeStatement(stringify(pointer.dict))
        let key = await this.executeStatement(stringify(pointer.key))
        let value = await this.executeStatement(stringify(pointer.value))

        dict[key] = value
    } catch (e) {
        onError(e)
    }
}
