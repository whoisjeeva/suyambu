import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let list = await this.executeStatement(stringify(pointer.list))
        let item = await this.executeStatement(stringify(pointer.item))

        switch(stringify(pointer.indexOf)) {
            case "FIRST":
                return list.indexOf(item) + 1
            case "LAST":
                return list.lastIndexOf(item) + 1
            default:
                return -1
        }
    } catch (e) {
        onError(e)
    }

    return null
}
