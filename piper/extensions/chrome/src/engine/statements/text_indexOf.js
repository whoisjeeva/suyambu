import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let value = await this.executeStatement(stringify(pointer.value))
        value = stringify(value)
        let substring = await this.executeStatement(stringify(pointer.substring))
        substring = stringify(substring)
        let indexOf = stringify(pointer.indexOf)

        if (indexOf === "FIRST") {
            return value.indexOf(substring) + 1
        } else if (indexOf === "LAST") {
            return value.lastIndexOf(substring) + 1
        }
    } catch (e) {
        onError(e)
    }

    return 0
}
