import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let value1 = await this.executeStatement(stringify(pointer.value1))
        let value2 = await this.executeStatement(stringify(pointer.value2))

        return value1 % value2
    } catch (e) {
        onError(e)
    }

    return null
}
